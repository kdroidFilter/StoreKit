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
 * Enhanced class responsible for extracting app information from APKCombo HTML content.
 */
class AppInfoExtractor {
    private var appInfo = AppInfo()
    private var isInVariantLink = false
    private var isInDownloadLink = false
    private var currentLinkHref = ""
    private var isInVersionSpan = false
    private var isInVersionCodeSpan = false
    private var isInTitleElement = false
    private var currentTitle = ""

    data class AppInfo(
        val title: String = "",
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

                    // Multiple patterns for download links
                    when {
                        // Primary pattern: variant class with download link
                        className.contains("variant") && href.isNotEmpty() &&
                                (href.contains("/r2?u=") || href.contains(".apk") || href.contains(".xapk")) -> {
                            isInVariantLink = true
                            currentLinkHref = href
                        }
                        // Secondary pattern: any download-related link
                        href.contains("/r2?u=") ||
                                (href.contains("download") && (href.contains(".apk") || href.contains(".xapk"))) -> {
                            isInDownloadLink = true
                            currentLinkHref = href
                        }
                        // Tertiary pattern: variant class (even without obvious download URL)
                        className.contains("variant") && href.isNotEmpty() -> {
                            if (appInfo.downloadLink.isEmpty()) {
                                isInVariantLink = true
                                currentLinkHref = href
                            }
                        }
                    }
                }
                "span" -> {
                    val className = attributes["class"] ?: ""
                    when {
                        className.contains("vername") -> isInVersionSpan = true
                        className.contains("vercode") -> isInVersionCodeSpan = true
                    }
                }
                "h1" -> {
                    isInTitleElement = true
                }
                "title" -> {
                    isInTitleElement = true
                }
            }
        }
        .onCloseTag { name, _ ->
            when (name.lowercase()) {
                "a" -> {
                    when {
                        isInVariantLink -> {
                            isInVariantLink = false
                            if (appInfo.downloadLink.isEmpty()) {
                                appInfo = appInfo.copy(downloadLink = currentLinkHref)
                            }
                        }
                        isInDownloadLink -> {
                            isInDownloadLink = false
                            if (appInfo.downloadLink.isEmpty()) {
                                appInfo = appInfo.copy(downloadLink = currentLinkHref)
                            }
                        }
                    }
                }
                "span" -> {
                    when {
                        isInVersionSpan -> isInVersionSpan = false
                        isInVersionCodeSpan -> isInVersionCodeSpan = false
                    }
                }
                "h1", "title" -> {
                    isInTitleElement = false
                    if (currentTitle.isNotEmpty() && appInfo.title.isEmpty()) {
                        appInfo = appInfo.copy(title = currentTitle.trim())
                    }
                    currentTitle = ""
                }
            }
        }
        .onText { text ->
            when {
                isInVersionSpan && appInfo.version.isEmpty() -> {
                    val trimmedText = text.trim()
                    if (trimmedText.isNotEmpty()) {
                        appInfo = appInfo.copy(version = trimmedText)
                    }
                }
                isInVersionCodeSpan && appInfo.versionCode.isEmpty() -> {
                    val trimmedText = text.trim()
                    if (trimmedText.isNotEmpty()) {
                        val cleanVersionCode = trimmedText.removePrefix("(").removeSuffix(")")
                        appInfo = appInfo.copy(versionCode = cleanVersionCode)
                    }
                }
                isInTitleElement -> {
                    currentTitle += text
                }
            }
        }
        .build()

    fun extractAppInfo(html: String): AppInfo {
        // Reset data
        appInfo = AppInfo()
        isInVariantLink = false
        isInDownloadLink = false
        currentLinkHref = ""
        isInVersionSpan = false
        isInVersionCodeSpan = false
        isInTitleElement = false
        currentTitle = ""

        val parser = KsoupHtmlParser(handler = handler)
        parser.write(html)
        parser.end()

        logger.info { "Extracted app info - Title: '${appInfo.title}', Version: '${appInfo.version}', VersionCode: '${appInfo.versionCode}', DownloadLink: '${appInfo.downloadLink}'" }

        // If we couldn't extract version info, try alternative methods
        if (appInfo.version.isEmpty() || appInfo.downloadLink.isEmpty()) {
            logger.info { "Some info missing, trying regex extraction..." }
            extractWithRegex(html)
        }

        return appInfo
    }

    private fun extractWithRegex(html: String) {
        // Extract version with regex as fallback
        if (appInfo.version.isEmpty()) {
            val versionPattern = Regex("""<span[^>]*class="[^"]*vername[^"]*"[^>]*>([^<]+)</span>""")
            val versionMatch = versionPattern.find(html)
            if (versionMatch != null) {
                appInfo = appInfo.copy(version = versionMatch.groupValues[1].trim())
            }
        }

        // Extract version code with regex as fallback
        if (appInfo.versionCode.isEmpty()) {
            val versionCodePattern = Regex("""<span[^>]*class="[^"]*vercode[^"]*"[^>]*>\(([^)]+)\)</span>""")
            val versionCodeMatch = versionCodePattern.find(html)
            if (versionCodeMatch != null) {
                appInfo = appInfo.copy(versionCode = versionCodeMatch.groupValues[1].trim())
            }
        }

        // Extract download link with regex as fallback
        if (appInfo.downloadLink.isEmpty()) {
            val downloadPatterns = listOf(
                Regex("""href="(/r2\?u=[^"]+)""""),
                Regex("""href="([^"]*download[^"]*\.apk[^"]*)""""),
                Regex("""href="([^"]*\.xapk[^"]*)"""")
            )

            for (pattern in downloadPatterns) {
                val match = pattern.find(html)
                if (match != null) {
                    appInfo = appInfo.copy(downloadLink = match.groupValues[1])
                    break
                }
            }
        }

        // Extract title as fallback
        if (appInfo.title.isEmpty()) {
            val titlePatterns = listOf(
                Regex("""<title>([^<]*APK[^<]*)</title>""", RegexOption.IGNORE_CASE),
                Regex("""<h1[^>]*class="[^"]*title[^"]*"[^>]*>([^<]+)</h1>""", RegexOption.IGNORE_CASE),
                Regex("""<h1[^>]*>([^<]+)</h1>"""),
                // Extract from Download ... APK pattern
                Regex("""Download ([^-]+) APK""", RegexOption.IGNORE_CASE),
                // Extract from meta description
                Regex("""<meta name="description" content="[^"]*?([^&]+) APK""", RegexOption.IGNORE_CASE)
            )

            for (pattern in titlePatterns) {
                val match = pattern.find(html)
                if (match != null) {
                    val title = match.groupValues[1].trim()
                        .replace(" APK", "")
                        .replace(" - Latest Version", "")
                        .replace(" - Download", "")
                        .replace("Download ", "")
                        .replace("&amp;", "&")
                    if (title.isNotBlank() && title.length > 1) {
                        appInfo = appInfo.copy(title = title)
                        break
                    }
                }
            }
        }
    }
}

/**
 * Enhanced function to fetch and return detailed information about an application on APKCombo.
 */
suspend fun getApkComboApplicationInfo(packageName: String): ApkComboApplicationInfo {
    logger.info { "Fetching app details for packageName: $packageName" }

    val response = fetchAppDownloadPage(packageName)

    if (!response.status.isSuccess()) {
        throw IllegalArgumentException("Application with packageName: $packageName does not exist or is not accessible. HTTP status: ${response.status}")
    }

    val html = response.bodyAsText()
    logger.info { "Fetched HTML content of size: ${html.length}" }

    val extractor = AppInfoExtractor()
    val appInfo = extractor.extractAppInfo(html)

    val downloadLink = cleanDownloadLink(appInfo.downloadLink)

    // Use the /app path as it redirects automatically
    val url = "$BASE_APKCOMBO_URL$APP_PATH/$packageName$DOWNLOAD_PATH"

    return ApkComboApplicationInfo(
        title = appInfo.title.ifEmpty { "Unknown App" },
        version = appInfo.version,
        versionCode = appInfo.versionCode,
        downloadLink = downloadLink,
        appId = packageName,
        url = url
    )
}