package io.github.kdroidfilter.storekit.gplay.scrapper.utils

import io.github.kdroidfilter.storekit.gplay.scrapper.constants.BASE_PLAY_STORE_URL
import io.github.kdroidfilter.storekit.gplay.scrapper.constants.DETAIL_PATH
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

// Ktor HttpClient for making network requests

/**
 * A utility object providing network-related functionalities specific to app data fetching and
 * HTML content processing. This object contains functions to perform HTTP requests to fetch web
 * pages and extract JSON data from HTML content.
 */
internal object NetworkUtils {
    internal val logger = KotlinLogging.logger {}
    internal val client = HttpClient(CIO)

    /**
     * Fetches the application page from the Play Store using the provided application ID.
     *
     * @param appId The unique identifier of the application whose page is to be fetched.
     * @param lang The language code for the content localization. Defaults to "en".
     * @param country The country code for the content localization. Defaults to "us".
     * @return HttpResponse representing the server's response to the request.
     */
// Networking
   internal suspend fun fetchAppPage(appId: String, lang: String = "en", country: String = "us"): HttpResponse {
        val url = "$BASE_PLAY_STORE_URL$DETAIL_PATH?id=$appId&hl=$lang&gl=$country"
        logger.info { "Fetching URL: $url" }
        return client.get(url)
    }

    /**
     * Extracts JSON blobs contained within script tags from the provided HTML content.
     *
     * The method identifies script tags in the HTML and collects their content if it contains
     * a specific pattern ("AF_initDataCallback"), indicating the presence of JSON blobs.
     *
     * @param html The string representation of the HTML content to be parsed.
     * @return A list of strings, each representing a JSON blob extracted from the script tags in the HTML.
     */
// Extract JSON from HTML
    internal fun extractJsonBlobsFromHtml(html: String): List<String> {
        val jsonScripts = mutableListOf<String>()
        var currentTagIsScript = false
        val currentScriptContent = StringBuilder()

        val handler = KsoupHtmlHandler.Builder()
            .onOpenTag { name, _, _ ->
                if (name.equals("script", ignoreCase = true)) {
                    currentTagIsScript = true
                    currentScriptContent.clear()
                }
            }
            .onCloseTag { name, _ ->
                if (name.equals("script", ignoreCase = true)) {
                    currentTagIsScript = false
                    val scriptText = currentScriptContent.toString()
                    if (scriptText.contains("AF_initDataCallback")) {
                        jsonScripts.add(scriptText)
                    }
                }
            }
            .onText { text ->
                if (currentTagIsScript) {
                    currentScriptContent.append(text)
                }
            }
            .build()

        val parser = KsoupHtmlParser(handler = handler)
        parser.write(html)
        parser.end()

        return jsonScripts
    }

}