package io.github.kdroidfilter.storekit.apkdownloader.core.model

import kotlinx.serialization.Serializable

/**
 * Represents information about an APK download.
 *
 * @property packageName The package name of the application
 * @property downloadLink The direct download link for the APK
 * @property source The source of the download link (e.g., "apkcombo", "aptoide")
 * @property version The version of the application (if available)
 * @property versionCode The version code of the application (if available)
 * @property title The title/name of the application (if available)
 * @property fileSize The size of the APK file in bytes (if available)
 */
@Serializable
data class ApkDownloadInfo(
    val packageName: String,
    val downloadLink: String,
    val source: String,
    val version: String = "",
    val versionCode: String = "",
    val title: String = "",
    val fileSize: Long = -1
)
