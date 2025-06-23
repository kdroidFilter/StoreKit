package io.github.kdroidfilter.storekit.apkcombo.scraper.services

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import io.github.kdroidfilter.storekit.apkcombo.core.model.ApkComboApplicationInfo
import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.APP_PATH
import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.BASE_APKCOMBO_URL
import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.DOWNLOAD_PATH
import io.github.kdroidfilter.storekit.apkcombo.scraper.utils.cleanDownloadLink
import io.github.kdroidfilter.storekit.apkcombo.scraper.utils.fetchAppDownloadPage
import io.github.kdroidfilter.storekit.apkcombo.scraper.utils.logger
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

/**
 * Class responsible for extracting app information from APKCombo HTML content.
 */
class AppInfoExtractor {
    private var appInfo = AppInfo()
    private var isInVariantLink = false
    private var currentLinkHref = ""
    private var isInVersionSpan = false
    private var isInVersionCodeSpan = false
    private var title = ""

    // Internal data class for storing extracted information
    data class AppInfo(
        val version: String = "",
        val versionCode: String = "",
        val downloadLink: String = ""
    )

    private val handler = KsoupHtmlHandler
        .Builder()
        .onOpenTag { name, attributes, _ ->
            when (name.lowercase()) {
                "a" -> {
                    val href = attributes["href"] ?: ""
                    val className = attributes["class"] ?: ""

                    // Check if it's a download link (variant class)
                    if (className.contains("variant") && href.isNotEmpty()) {
                        isInVariantLink = true
                        currentLinkHref = href
                    }
                }
                "span" -> {
                    val className = attributes["class"] ?: ""
                    // Check if it's a span containing the version
                    if (className.contains("vername")) {
                        isInVersionSpan = true
                    }
                    // Check if it's a span containing the version code
                    else if (className.contains("vercode")) {
                        isInVersionCodeSpan = true
                    }
                }
                "h1" -> {
                    // Look for the app title in h1 tags
                    val className = attributes["class"] ?: ""
                    if (className.contains("app-name") || className.contains("title")) {
                        // We'll capture the text in the onText handler
                    }
                }
            }
        }
        .onCloseTag { name, _ ->
            when (name.lowercase()) {
                "a" -> {
                    if (isInVariantLink) {
                        isInVariantLink = false
                        if (appInfo.downloadLink.isEmpty()) {
                            appInfo = appInfo.copy(downloadLink = currentLinkHref)
                        }
                    }
                }
                "span" -> {
                    if (isInVersionSpan) {
                        isInVersionSpan = false
                    } else if (isInVersionCodeSpan) {
                        isInVersionCodeSpan = false
                    }
                }
            }
        }
        .onText { text ->
            if (isInVersionSpan && appInfo.version.isEmpty()) {
                // Extract the version (e.g., "Waze 5.6.0.1")
                val trimmedText = text.trim()
                if (trimmedText.isNotEmpty()) {
                    appInfo = appInfo.copy(version = trimmedText)
                }
            } else if (isInVersionCodeSpan && appInfo.versionCode.isEmpty()) {
                // Extract the version code (e.g., "(1030572)")
                val trimmedText = text.trim()
                if (trimmedText.isNotEmpty()) {
                    // Remove parentheses if present
                    val cleanVersionCode = trimmedText.removePrefix("(").removeSuffix(")")
                    appInfo = appInfo.copy(versionCode = cleanVersionCode)
                }
            }
        }
        .build()

    fun extractAppInfo(html: String): AppInfo {
        // Reset data
        appInfo = AppInfo()
        isInVariantLink = false
        currentLinkHref = ""
        isInVersionSpan = false
        isInVersionCodeSpan = false

        // Create parser
        val parser = KsoupHtmlParser(handler = handler)

        // Parse HTML
        parser.write(html)
        parser.end()

        return appInfo
    }
}

/**
 * Fetches and returns detailed information about an application on APKCombo based on the provided package name.
 *
 * @param packageName The package name of the application
 * @return An instance of [ApkComboApplicationInfo] containing the application's information
 */
suspend fun getApkComboApplicationInfo(packageName: String): ApkComboApplicationInfo {
    logger.info { "Fetching app details for packageName: $packageName" }
    
    val response = fetchAppDownloadPage(packageName)
    val html = response.bodyAsText()
    logger.info { "Fetched HTML content of size: ${html.length}" }

    if (!response.status.isSuccess()) {
        throw IllegalArgumentException("Application with packageName: $packageName does not exist or is not accessible. HTTP status: ${response.status}")
    }

    val extractor = AppInfoExtractor()
    val appInfo = extractor.extractAppInfo(html)
    
    val downloadLink = cleanDownloadLink(appInfo.downloadLink)
    val url = "$BASE_APKCOMBO_URL$APP_PATH/$packageName$DOWNLOAD_PATH"

    return ApkComboApplicationInfo(
        title = "", // Currently not extracted, could be enhanced in the future
        version = appInfo.version,
        versionCode = appInfo.versionCode,
        downloadLink = downloadLink,
        appId = packageName,
        url = url
    )
}