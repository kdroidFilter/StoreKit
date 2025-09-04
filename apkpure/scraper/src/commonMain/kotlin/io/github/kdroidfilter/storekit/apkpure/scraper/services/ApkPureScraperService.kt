package io.github.kdroidfilter.storekit.apkpure.scraper.services

import io.github.kdroidfilter.storekit.apkpure.core.model.ApkPureApplicationInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

private const val BASE_APKPURE_URL = "https://apkpure.com"
private const val APP_PATH = "/app"
private const val DOWNLOAD_SUFFIX = "/download"
private const val DIRECT_DL_BASE = "https://d.apkpure.com/b/XAPK"

// Build direct download URL: https://d.apkpure.com/b/XAPK/<packagename>?version=<version|latest>
fun buildApkPureDownloadUrl(packageName: String, version: String = "latest"): String {
    val versionParam = if (version.isBlank()) "latest" else version
    return "$DIRECT_DL_BASE/$packageName?version=$versionParam"
}

// Build info page URL: https://apkpure.com/app/<packagename>/download
fun buildApkPureInfoUrl(packageName: String): String = "$BASE_APKPURE_URL$APP_PATH/$packageName$DOWNLOAD_SUFFIX"

suspend fun getApkPureApplicationInfo(
    packageName: String,
    client: HttpClient = defaultClient()
): ApkPureApplicationInfo {
    val url = buildApkPureInfoUrl(packageName)
    val response = client.get(url)
    if (!response.status.isSuccess()) {
        throw IllegalArgumentException("Application with packageName: $packageName does not exist or is not accessible. HTTP status: ${response.status}")
    }
    val html = response.bodyAsText()

    val title = extractTitle(html)
    val version = extractVersion(html)
    val versionCode = extractVersionCode(html).ifBlank { extractVersionCodeFallback(html) }
    val signature = extractSignature(html)

    val downloadLink = buildApkPureDownloadUrl(packageName, "latest")

    return ApkPureApplicationInfo(
        title = if (title.isNotBlank()) title else "Unknown App",
        version = version,
        versionCode = versionCode,
        signature = signature,
        downloadLink = downloadLink,
        appId = packageName,
        url = url
    )
}

internal fun extractTitle(html: String): String {
    val patterns = listOf(
        Regex("""<h1[^>]*class=\"[^\"]*name[^\"]*\"[^>]*>(.*?)</h1>""", RegexOption.IGNORE_CASE),
        Regex("""<title>([^<]+)</title>""", RegexOption.IGNORE_CASE)
    )
    for (p in patterns) {
        val m = p.find(html)
        if (m != null) {
            return m.groupValues[1]
                .replace("&amp;", "&")
                .replace(" - APK Download", "")
                .trim()
        }
    }
    return ""
}

internal fun extractVersion(html: String): String {
    val patterns = listOf(
        Regex("""<div[^>]*class=\"[^\"]*version( name)?[^\"]*\"[^>]*>\s*<span[^>]*>([^<]+)</span>""", RegexOption.IGNORE_CASE),
        Regex("""<span[^>]*class=\"[^\"]*vername[^\"]*\"[^>]*>([^<]+)</span>""", RegexOption.IGNORE_CASE)
    )
    for (p in patterns) {
        val m = p.find(html)
        if (m != null) {
            val idx = if (m.groupValues.size >= 3) 2 else 1
            return m.groupValues[idx].trim()
        }
    }
    return ""
}

internal fun extractVersionCode(html: String): String {
    val p = Regex("""<span[^>]*class=\"[^\"]*vercode[^\"]*\"[^>]*>\(([^)]+)\)</span>""", RegexOption.IGNORE_CASE)
    return p.find(html)?.groupValues?.get(1)?.trim().orEmpty()
}

// Fallbacks: extract versionCode from variant blocks or URLs
internal fun extractVersionCodeFallback(html: String): String {
    val patterns = listOf(
        // Variant info-top line like: <span class="code one-line">(1030640)</span>
        Regex("""<span[^>]*class=\"[^\"]*code[^\"]*\"[^>]*>\\(([^)]+)\\)</span>""", RegexOption.IGNORE_CASE),
        // Download button href: https://d.apkpure.com/b/XAPK/com.package?versionCode=1030640
        Regex("href=\"[^\"]*versionCode=([0-9]+)[^\"]*\""),
        // Data attribute variant code, sometimes plain number inside span
        Regex("""\(\s*([0-9]{3,})\s*\)""")
    )
    for (p in patterns) {
        val m = p.find(html)
        if (m != null) return m.groupValues[1].trim()
    }
    return ""
}

// Extract signature from More App Info section or variant dialog
internal fun extractSignature(html: String): String {
    val patterns = listOf(
        // More App Info list item: <div class="label one-line">Signature</div><div class="value double-lines">35b4...</div>
        Regex("""Signature</div>\s*<div[^>]*class=\"value[^\"]*\"[^>]*>([a-fA-F0-9]{8,})</div>""", RegexOption.IGNORE_CASE),
        // Variant dialog line: <span class="label">Signature</span><span class="value">35b4...</span>
        Regex("""<span[^>]*class=\"label\"[^>]*>\s*Signature\s*</span>\s*<span[^>]*class=\"value\"[^>]*>\s*([a-fA-F0-9]{8,})\s*</span>""", RegexOption.IGNORE_CASE)
    )
    for (p in patterns) {
        val m = p.find(html)
        if (m != null) return m.groupValues[1].trim()
    }
    return ""
}

private fun defaultClient(): HttpClient {
    // Keep minimal: rely on platform default if the consumer injects a configured client elsewhere.
    return HttpClient()
}
