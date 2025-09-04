package io.github.kdroidfilter.storekit.apkpure.core.model

import kotlinx.serialization.Serializable

/**
 * Represents basic information about an application on the APKPure website.
 */
@Serializable
data class ApkPureApplicationInfo(
    val title: String = "",
    val version: String = "",
    val versionCode: String = "",
    val signature: String = "",
    val downloadLink: String = "",
    val appId: String = "",
    val url: String = ""
)
