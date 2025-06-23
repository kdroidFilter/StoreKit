package io.github.kdroidfilter.storekit.apkdownloader.core.utils

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.UserAgent
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

/**
 * Utility functions for file operations.
 */
object FileUtils {
    private val logger = KotlinLogging.logger {}

    /**
     * Retrieves the file size from a URL.
     * First tries a HEAD request, then falls back to a GET request with Range header,
     * and finally tries a regular GET request if both previous methods fail.
     *
     * @param url The URL to check
     * @return The file size in bytes, or -1 if the size could not be determined
     */
    suspend fun getFileSizeFromUrl(url: String): Long {
        logger.info { "Retrieving file size for URL: $url" }

        val client = HttpClient(CIO) {
            install(UserAgent) {
                agent = "Mozilla/5.0 (Linux; Android 16) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.7151.116 Mobile"
            }
        }

        return try {
            // First try: HEAD request (most efficient)
            try {
                val headResponse = client.head(url)

                if (headResponse.status == HttpStatusCode.OK) {
                    // Extract Content-Length header
                    val contentLength = headResponse.headers[HttpHeaders.ContentLength]?.toLongOrNull()

                    if (contentLength != null && contentLength > 0) {
                        logger.info { "File size from HEAD request: $contentLength bytes" }
                        return contentLength
                    }
                }
                logger.info { "HEAD request didn't provide file size, trying GET with Range header" }
            } catch (e: Exception) {
                logger.info { "HEAD request failed: ${e.message}, trying GET with Range header" }
            }

            // Second try: GET request with Range header (requests only first byte)
            try {
                val rangeResponse = client.get(url) {
                    header(HttpHeaders.Range, "bytes=0-0")
                }

                // Check for Content-Range header which contains total size
                val contentRange = rangeResponse.headers[HttpHeaders.ContentRange]
                if (contentRange != null) {
                    // Content-Range format: "bytes 0-0/12345" where 12345 is the total size
                    val totalSize = contentRange.substringAfter("/").toLongOrNull()
                    if (totalSize != null && totalSize > 0) {
                        logger.info { "File size from Content-Range: $totalSize bytes" }
                        return totalSize
                    }
                }

                // Check if we got Content-Length header as fallback
                val contentLength = rangeResponse.headers[HttpHeaders.ContentLength]?.toLongOrNull()
                if (contentLength != null && contentLength > 0) {
                    logger.info { "File size from GET with Range: $contentLength bytes" }
                    return contentLength
                }

                logger.info { "GET with Range didn't provide file size, trying regular GET" }
            } catch (e: Exception) {
                logger.info { "GET with Range failed: ${e.message}, trying regular GET" }
            }

            // Third try: Regular GET request (least efficient, but most compatible)
            val getResponse = client.get(url)

            if (getResponse.status == HttpStatusCode.OK) {
                val contentLength = getResponse.headers[HttpHeaders.ContentLength]?.toLongOrNull()

                if (contentLength != null && contentLength > 0) {
                    logger.info { "File size from GET request: $contentLength bytes" }
                    return contentLength
                }
            }

            logger.warn { "Could not determine file size after all attempts" }
            -1
        } catch (e: Exception) {
            logger.error(e) { "Error retrieving file size: ${e.message}" }
            -1
        } finally {
            client.close()
        }
    }
}
