package io.github.kdroidfilter.storekit.apkcombo.scraper.utils

import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.APP_PATH
import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.BASE_APKCOMBO_URL
import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.DOWNLOAD_PATH
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

/**
 * Logger for network operations
 */
internal val logger = KotlinLogging.logger {}

/**
 * Fetches the HTML content of an app's download page from APKCombo.
 *
 * @param packageName The package name of the application
 * @return HttpResponse containing the HTML content
 */
suspend fun fetchAppDownloadPage(packageName: String): HttpResponse {
    logger.info { "Fetching app download page for packageName: $packageName" }
    val url = "$BASE_APKCOMBO_URL$APP_PATH/$packageName$DOWNLOAD_PATH"
    
    val client = HttpClient {
        install(UserAgent) {
            agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 10000
        }
    }
    
    return try {
        val response = client.get(url)
        client.close()
        response
    } catch (e: Exception) {
        client.close()
        throw Exception("Error fetching app download page: ${e.message}", e)
    }
}

/**
 * Cleans a download link from APKCombo.
 *
 * @param link The raw download link
 * @return The cleaned download link
 */
fun cleanDownloadLink(link: String): String {
    return if (link.startsWith("/r2?u=")) {
        // Decode the URL encoded after "u="
        val encodedUrl = link.substringAfter("u=")
        try {
            java.net.URLDecoder.decode(encodedUrl, "UTF-8")
        } catch (e: Exception) {
            link
        }
    } else if (link.startsWith("/")) {
        "$BASE_APKCOMBO_URL$link"
    } else {
        link
    }
}