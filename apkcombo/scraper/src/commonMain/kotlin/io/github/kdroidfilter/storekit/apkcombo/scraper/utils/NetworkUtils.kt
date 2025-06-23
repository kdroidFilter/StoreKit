package io.github.kdroidfilter.storekit.apkcombo.scraper.utils

import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.APP_PATH
import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.BASE_APKCOMBO_URL
import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.DOWNLOAD_PATH
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import io.ktor.http.isSuccess

/**
 * Logger for network operations
 */
internal val logger = KotlinLogging.logger {}

/**
 * Fetches the HTML content of an app's download page from APKCombo.
 * Uses /app path which redirects automatically to the correct URL.
 *
 * @param packageName The package name of the application
 * @return HttpResponse containing the HTML content with download information
 */
suspend fun fetchAppDownloadPage(packageName: String): HttpResponse {
    logger.info { "Fetching app download page for packageName: $packageName" }

    val client = HttpClient {
        install(UserAgent) {
            agent = "Mozilla/5.0 (Linux; Android 16) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.7151.116 Mobile"
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 10000
        }
    }

    return try {
        // Use /app path which redirects automatically to correct URL
        val downloadUrl = "$BASE_APKCOMBO_URL$APP_PATH/$packageName$DOWNLOAD_PATH"
        logger.info { "Fetching download page: $downloadUrl" }

        // Get the initial download page
        val initialResponse = client.get(downloadUrl)
        val initialHtml = initialResponse.bodyAsText()

        // Check if we need to make an AJAX call to get download details
        // Only do this if the page doesn't already contain download links
        val hasDirectDownloadLinks = initialHtml.contains("class=\"variant\"") &&
                (initialHtml.contains("/r2?u=") || initialHtml.contains(".apk") || initialHtml.contains(".xapk"))

        if (!hasDirectDownloadLinks && (initialHtml.contains("fetchData") || initialHtml.contains("app-details"))) {
            logger.info { "Detected dynamic content without direct download links, attempting to extract download endpoint..." }

            // Extract the download endpoint from the JavaScript
            val downloadEndpoint = extractDownloadEndpoint(initialHtml, packageName)

            if (downloadEndpoint.isNotEmpty()) {
                logger.info { "Making POST request to: $downloadEndpoint" }

                // Make POST request to get actual download data
                val postResponse = client.post(downloadEndpoint) {
                    setBody(FormDataContent(Parameters.build {
                        append("package_name", packageName)
                        append("version", "")
                    }))
                }

                // Check if POST request was successful
                if (postResponse.status.isSuccess()) {
                    logger.info { "POST request successful, status: ${postResponse.status}" }
                    client.close()
                    return postResponse
                } else {
                    logger.warn { "POST request failed with status: ${postResponse.status}" }
                    // Fall back to initial response
                }
            } else {
                logger.warn { "Could not extract download endpoint from dynamic content" }
            }
        } else if (hasDirectDownloadLinks) {
            logger.info { "Found direct download links in initial response, using static content" }
        } else {
            logger.info { "No dynamic content detected, using initial response" }
        }

        client.close()
        initialResponse
    } catch (e: Exception) {
        client.close()
        throw Exception("Error fetching app download page: ${e.message}", e)
    }
}

/**
 * Extracts the download endpoint from JavaScript code
 */
private fun extractDownloadEndpoint(html: String, packageName: String): String {
    logger.info { "Extracting download endpoint for package: $packageName" }

    // First, look for the xid variable
    val xidPattern = Regex("""var xid = "([^"]+)"""")
    val xidMatch = xidPattern.find(html)

    if (xidMatch != null) {
        val xid = xidMatch.groupValues[1]
        logger.info { "Found xid: $xid" }

        // Now look for the app path construction
        // Pattern: fetchData("/app-name/package.name/" + xid + "/dl")
        val appPathPattern = Regex("""fetchData\("([^"]+/$packageName/)" \+ xid \+ "/dl"\)""")
        val appPathMatch = appPathPattern.find(html)

        if (appPathMatch != null) {
            val appPath = appPathMatch.groupValues[1]
            val fullEndpoint = "$appPath$xid/dl"
            logger.info { "Constructed endpoint from xid: $fullEndpoint" }
            return "$BASE_APKCOMBO_URL$fullEndpoint"
        }

        // Fallback: try to find any path that contains the package name
        val pathPattern = Regex(""""/([^"/]+/$packageName/)"""")
        val pathMatch = pathPattern.find(html)

        if (pathMatch != null) {
            val basePath = pathMatch.groupValues[1]
            val fullEndpoint = "/$basePath$xid/dl"
            logger.info { "Constructed endpoint from base path: $fullEndpoint" }
            return "$BASE_APKCOMBO_URL$fullEndpoint"
        }
    }

    // Look for direct fetchData patterns
    val fetchDataPatterns = listOf(
        // Pattern: fetchData("/full/path/to/endpoint")
        Regex("""fetchData\("([^"]+/dl)""""),
        // Pattern: fetchData with variable construction
        Regex("""fetchData\("([^"]+)" \+ [^"]+ \+ "([^"]+)""""),
    )

    for (pattern in fetchDataPatterns) {
        val match = pattern.find(html)
        if (match != null) {
            val endpoint = match.groupValues[1]
            logger.info { "Found direct fetchData endpoint: $endpoint" }

            if (endpoint.contains(packageName) || endpoint.endsWith("/dl")) {
                return if (endpoint.startsWith("/")) {
                    "$BASE_APKCOMBO_URL$endpoint"
                } else {
                    endpoint
                }
            }
        }
    }

    // Look in specific script sections for the pattern
    val scriptPattern = Regex("""<script[^>]*>(.*?)</script>""", RegexOption.DOT_MATCHES_ALL)
    val scriptMatches = scriptPattern.findAll(html)

    for (scriptMatch in scriptMatches) {
        val scriptContent = scriptMatch.groupValues[1]

        // Look for the specific pattern in this app
        if (scriptContent.contains("fetchData") && scriptContent.contains(packageName)) {
            logger.info { "Found relevant script section" }

            // Try to extract the complete pattern
            val completePattern = Regex("""var xid = "([^"]+)".*?fetchData\("([^"]+/$packageName/)" \+ xid \+ "/dl"\)""", RegexOption.DOT_MATCHES_ALL)
            val completeMatch = completePattern.find(scriptContent)

            if (completeMatch != null) {
                val xid = completeMatch.groupValues[1]
                val basePath = completeMatch.groupValues[2]
                val fullEndpoint = "$basePath$xid/dl"
                logger.info { "Extracted complete endpoint: $fullEndpoint" }
                return "$BASE_APKCOMBO_URL$fullEndpoint"
            }

            // Alternative: look for any dl endpoint
            val dlPattern = Regex("""["']([^"']*$packageName[^"']*dl)["']""")
            val dlMatch = dlPattern.find(scriptContent)
            if (dlMatch != null) {
                val endpoint = dlMatch.groupValues[1]
                logger.info { "Found dl endpoint: $endpoint" }
                return if (endpoint.startsWith("/")) {
                    "$BASE_APKCOMBO_URL$endpoint"
                } else {
                    "$BASE_APKCOMBO_URL/$endpoint"
                }
            }
        }
    }

    logger.warn { "Could not extract download endpoint" }
    return ""
}

/**
 * Cleans a download link from APKCombo.
 */
fun cleanDownloadLink(link: String): String {
    return when {
        link.startsWith("/r2?u=") -> {
            // Decode the URL encoded after "u="
            val encodedUrl = link.substringAfter("u=")
            try {
                java.net.URLDecoder.decode(encodedUrl, "UTF-8")
            } catch (e: Exception) {
                link
            }
        }
        link.startsWith("/") -> "$BASE_APKCOMBO_URL$link"
        else -> link
    }
}