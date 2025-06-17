package io.github.kdroidfilter.androidappstorekit.gplay.scrapper.utils

/**
 * Provides utility functions for decoding HTML entities commonly encountered in HTML content.
 * This object focuses on converting encoded HTML entities back to their literal character equivalents
 * in a given string, allowing for easier text processing and display.
 *
 * The primary focus is on handling a fixed set of HTML entities, including:
 * - &amp; for &
 * - &gt; for >
 * - &lt; for <
 * - &quot; for "
 * - &#39; for '
 */
internal object HtmlDecoder {

    fun decodeHtml(s: String): String {
        return s
            .replace("&amp;", "&")
            .replace("&gt;", ">")
            .replace("&lt;", "<")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
    }

    // Simple unescape HTML function
    fun unescapeHtml(s: String?): String {
        return s?.replace("<br>", "\n")?.let { decodeHtml(it) } ?: ""
    }

}