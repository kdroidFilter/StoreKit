package io.github.kdroidfilter.storekit.apkcombo.core.model

import kotlinx.serialization.Serializable

/**
 * Represents detailed information about an application on the APKCombo website.
 *
 * @property title The title/name of the application
 * @property version Current version of the application
 * @property versionCode Version code of the application
 * @property downloadLink Direct download link for the APK
 * @property appId Unique identifier of the application
 * @property url URL to the application's page on APKCombo
 */
@Serializable
data class ApkComboApplicationInfo(
    val title: String = "",
    val version: String = "",
    val versionCode: String = "",
    val downloadLink: String = "",
    val appId: String = "",
    val url: String = ""
)