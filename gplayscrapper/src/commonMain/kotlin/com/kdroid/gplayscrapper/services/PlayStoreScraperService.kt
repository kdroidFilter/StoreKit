package com.kdroid.gplayscrapper.services

import com.kdroid.gplayscrapper.constants.BASE_PLAY_STORE_URL
import com.kdroid.gplayscrapper.constants.DETAIL_PATH
import com.kdroid.gplayscrapper.model.GooglePlayApplicationInfo
import com.kdroid.gplayscrapper.model.GooglePlayCategory
import com.kdroid.gplayscrapper.utils.HtmlDecoder.unescapeHtml
import com.kdroid.gplayscrapper.utils.JsonExtensions.asDoubleOrNull
import com.kdroid.gplayscrapper.utils.JsonExtensions.asLongOrNull
import com.kdroid.gplayscrapper.utils.JsonExtensions.asStringOrNull
import com.kdroid.gplayscrapper.utils.JsonExtensions.jsonElementToBool
import com.kdroid.gplayscrapper.utils.JsonExtensions.microsToPrice
import com.kdroid.gplayscrapper.utils.JsonExtensions.nestedLookup
import com.kdroid.gplayscrapper.utils.NetworkUtils.extractJsonBlobsFromHtml
import com.kdroid.gplayscrapper.utils.NetworkUtils.fetchAppPage
import com.kdroid.gplayscrapper.utils.NetworkUtils.logger
import com.kdroid.gplayscrapper.utils.parseDataSetsFromScripts
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray

private fun extractCategories(datasets: Map<String, JsonElement>): List<GooglePlayCategory> {
    val detailJson = datasets["ds:5"] ?: return emptyList()
    val catElement = nestedLookup(detailJson, listOf(1,2,118))
    val categories = mutableListOf<GooglePlayCategory>()
    if (catElement == null) {
        // fallback
        return fallbackCategories(datasets)
    }
    extractCategoriesRecursive(catElement, categories)
    if (categories.isEmpty()) {
        return fallbackCategories(datasets)
    }
    return categories
}

private fun fallbackCategories(datasets: Map<String, JsonElement>): List<GooglePlayCategory> {
    val detailJson = datasets["ds:5"] ?: return emptyList()
    val name = nestedLookup(detailJson, listOf(1,2,79,0,0,0)).asStringOrNull()
    val id = nestedLookup(detailJson, listOf(1,2,79,0,0,2)).asStringOrNull()
    return if (name != null && id != null) listOf(GooglePlayCategory(name, id)) else emptyList()
}

private fun extractCategoriesRecursive(e: JsonElement, categories: MutableList<GooglePlayCategory>) {
    when (e) {
        is JsonArray -> {
            // According to python logic:
            // "if len(s) >=4 and type(s[0]) is str: categories.append({name: s[0], id: s[2]})"
            if (e.size >= 4 && e[0] is JsonPrimitive) {
                val name = e[0].asStringOrNull() ?: return
                val id = e.getOrNull(2)?.asStringOrNull() ?: return
                categories.add(GooglePlayCategory(name, id))
            } else {
                for (sub in e) {
                    extractCategoriesRecursive(sub, categories)
                }
            }
        }
        else -> {}
    }
}

internal fun extractComments(datasets: Map<String, JsonElement>): List<String> { //TODO Not working
    // Lists of potential datasets
    val possiblePaths = listOf("ds:8", "ds:9", "ds:13", "ds:15")
    var commentsArray: JsonElement? = null

    for (path in possiblePaths) {
        val authorPath = listOf(path, "0", "0", "1", "0")
        val versionPath = listOf(path, "0", "0", "10")
        val datePath = listOf(path, "0", "0", "5", "0")

        val authorExists = nestedLookup(datasets[path], authorPath.drop(1).map { it.toInt() }) != null
        val versionExists = nestedLookup(datasets[path], versionPath.drop(1).map { it.toInt() }) != null
        val dateExists = nestedLookup(datasets[path], datePath.drop(1).map { it.toInt() }) != null

        if (authorExists && versionExists && dateExists) {
            // If we find all the fields, we consider that this dataset is the right one
            commentsArray = nestedLookup(datasets[path], listOf(0))
            if (commentsArray != null) break
        }
    }

    if (commentsArray == null || commentsArray !is JsonArray) {
        return emptyList()
    }

    // limit ad 5 comments
    return commentsArray.take(5).mapNotNull { entry ->
        entry.jsonArray.getOrNull(4)?.asStringOrNull()
    }
}

internal fun extractHistogram(detailJson: JsonElement): List<Long> {
    // histogram is at [1,2,51,1]
    val histBase = nestedLookup(detailJson, listOf(1,2,51,1)) as? JsonArray ?: return listOf(0,0,0,0,0)
    // According to python: [1][1], [2][1], [3][1], [4][1], [5][1]
    // histBase[0] might be something else
    val result = mutableListOf<Long>()
    for (i in 1..5) {
        val value = histBase.getOrNull(i)?.jsonArray?.getOrNull(1)?.asLongOrNull() ?: 0
        result.add(value)
    }
    return result
}

/**
 * Fetches and returns detailed information about a Google Play application based on the provided application ID.
 *
 * @param appId The unique identifier for the application on the Google Play Store.
 * @param lang The language code to fetch application details in. Defaults to English ("en").
 * @param country The country code to fetch application details from. Defaults to United States ("us").
 * @return An instance of [GooglePlayApplicationInfo] containing the application's information such as title, description, developer details, and more.
 */
suspend fun getGooglePlayApplicationInfo(appId: String, lang: String = "en", country: String = "us"): GooglePlayApplicationInfo {
    logger.info { "Fetching app details for appId: $appId" }
    val response = fetchAppPage(appId, lang, country)
    val html = response.bodyAsText()
    logger.info { "Fetched HTML content of size: ${html.length}" }

    if (!response.status.isSuccess()) {
        throw IllegalArgumentException("Application with appId: $appId does not exist or is not accessible. HTTP status: ${response.status}")
    }

    val scripts = extractJsonBlobsFromHtml(html)
    val datasets = parseDataSetsFromScripts(scripts)

    val comments = extractComments(datasets)

    val detailJson = datasets["ds:5"]
        ?: return GooglePlayApplicationInfo(
            appId = appId,
            url = "$BASE_PLAY_STORE_URL$DETAIL_PATH?id=$appId&hl=$lang&gl=$country",
            comments = comments // even if empty, we set it
        )

    // descriptionHTML = nested_lookup(... [12,0,0,1]) or [72,0,1]
    val rawDescriptionHTML = nestedLookup(detailJson, listOf(1,2,12,0,0,1)).asStringOrNull()
        ?: nestedLookup(detailJson, listOf(1,2,72,0,1)).asStringOrNull()
        ?: ""
    val description = unescapeHtml(rawDescriptionHTML)

    val summary = nestedLookup(detailJson, listOf(1,2,73,0,1)).asStringOrNull()?.let { unescapeHtml(it) } ?: ""

    val title = nestedLookup(detailJson, listOf(1,2,0,0)).asStringOrNull() ?: ""
    val installs = nestedLookup(detailJson, listOf(1,2,13,0)).asStringOrNull() ?: ""
    val minInstalls = nestedLookup(detailJson, listOf(1,2,13,1)).asLongOrNull() ?: 0
    val realInstalls = nestedLookup(detailJson, listOf(1,2,13,2)).asLongOrNull() ?: 0
    val score = nestedLookup(detailJson, listOf(1,2,51,0,1)).asDoubleOrNull() ?: 0.0
    val ratings = nestedLookup(detailJson, listOf(1,2,51,2,1)).asLongOrNull() ?: 0
    val reviews = nestedLookup(detailJson, listOf(1,2,51,3,1)).asLongOrNull() ?: 0
    val histogram = extractHistogram(detailJson)

    val price = microsToPrice(nestedLookup(detailJson, listOf(1,2,57,0,0,0,0,1,0,0)))
    val free = (nestedLookup(detailJson, listOf(1,2,57,0,0,0,0,1,0,0)).asLongOrNull() == 0L)
    val currency = nestedLookup(detailJson, listOf(1,2,57,0,0,0,0,1,0,1)).asStringOrNull() ?: ""

    val sale = datasets["ds:4"]?.let { jsonElementToBool(nestedLookup(it, listOf(0,2,0,0,0,14,0,0))) } ?: false
    val saleTime = datasets["ds:4"]?.let { nestedLookup(it, listOf(0,2,0,0,0,14,0,0)).asLongOrNull() }
    val originalPriceVal = datasets["ds:3"]?.let { microsToPrice(nestedLookup(it, listOf(0,2,0,0,0,1,1,0))) }
    val originalPrice = if (originalPriceVal == 0.0) null else originalPriceVal
    val saleText = datasets["ds:4"]?.let { nestedLookup(it, listOf(0,2,0,0,0,14,1)).asStringOrNull() }

    val offersIAP = jsonElementToBool(nestedLookup(detailJson, listOf(1,2,19,0)))
    val inAppProductPrice = nestedLookup(detailJson, listOf(1,2,19,0)).asStringOrNull() ?: ""

    val developer = nestedLookup(detailJson, listOf(1,2,68,0)).asStringOrNull() ?: ""
    val developerId = nestedLookup(detailJson, listOf(1,2,68,1,4,2)).asStringOrNull()?.substringAfter("id=") ?: ""
    val developerEmail = nestedLookup(detailJson, listOf(1,2,69,1,0)).asStringOrNull() ?: ""
    val developerWebsite = nestedLookup(detailJson, listOf(1,2,69,0,5,2)).asStringOrNull() ?: ""
    val developerAddress = nestedLookup(detailJson, listOf(1,2,69,2,0)).asStringOrNull() ?: ""
    val privacyPolicy = nestedLookup(detailJson, listOf(1,2,99,0,5,2)).asStringOrNull() ?: ""

    val genre = nestedLookup(detailJson, listOf(1,2,79,0,0,0)).asStringOrNull() ?: ""
    val genreId = nestedLookup(detailJson, listOf(1,2,79,0,0,2)).asStringOrNull() ?: ""
    val categories = extractCategories(datasets)

    val icon = nestedLookup(detailJson, listOf(1,2,95,0,3,2)).asStringOrNull() ?: ""
    val headerImage = nestedLookup(detailJson, listOf(1,2,96,0,3,2)).asStringOrNull() ?: ""

    val screenshotsJsonArray = nestedLookup(detailJson, listOf(1,2,78,0)) as? JsonArray
    val screenshots = screenshotsJsonArray?.mapNotNull { element ->
        element.jsonArray.getOrNull(3)?.jsonArray?.getOrNull(2)?.asStringOrNull()
    } ?: emptyList()

    val video = nestedLookup(detailJson, listOf(1,2,100,0,0,3,2)).asStringOrNull() ?: ""
    val videoImage = nestedLookup(detailJson, listOf(1,2,100,1,0,3,2)).asStringOrNull() ?: ""

    val contentRating = nestedLookup(detailJson, listOf(1,2,9,0)).asStringOrNull() ?: ""
    val contentRatingDescription = nestedLookup(detailJson, listOf(1,2,9,2,1)).asStringOrNull() ?: ""

    val adSupported = jsonElementToBool(nestedLookup(detailJson, listOf(1,2,48)))
    val containsAds = (nestedLookup(detailJson, listOf(1,2,48)).asLongOrNull() == 1L)

    val released = nestedLookup(detailJson, listOf(1,2,10,0)).asStringOrNull() ?: ""
    val updated = nestedLookup(detailJson, listOf(1,2,145,0,1,0)).asLongOrNull() ?: 0
    val version = nestedLookup(detailJson, listOf(1,2,140,0,0,0)).asStringOrNull() ?: "Varies with device"




    val url = "$BASE_PLAY_STORE_URL$DETAIL_PATH?id=$appId&hl=$lang&gl=$country"

    return GooglePlayApplicationInfo(
        title = title,
        description = description,
        descriptionHTML = rawDescriptionHTML,
        summary = summary,
        installs = installs,
        minInstalls = minInstalls,
        realInstalls = realInstalls,
        score = score,
        ratings = ratings,
        reviews = reviews,
        histogram = histogram,
        price = price,
        free = free,
        currency = currency,
        sale = sale,
        saleTime = saleTime,
        originalPrice = originalPrice,
        saleText = saleText,
        offersIAP = offersIAP,
        inAppProductPrice = inAppProductPrice,
        developer = developer,
        developerId = developerId,
        developerEmail = developerEmail,
        developerWebsite = developerWebsite,
        developerAddress = developerAddress,
        privacyPolicy = privacyPolicy,
        genre = genre,
        genreId = genreId,
        categories = categories,
        icon = icon,
        headerImage = headerImage,
        screenshots = screenshots,
        video = video,
        videoImage = videoImage,
        contentRating = contentRating,
        contentRatingDescription = contentRatingDescription,
        adSupported = adSupported,
        containsAds = containsAds,
        released = released,
        updated = updated,
        version = version,
        comments = comments,
        appId = appId,
        url = url
    )
}
