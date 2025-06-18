package io.github.kdroidfilter.storekit.fdroid.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing the F-Droid package information.
 * This class corresponds to the response from the F-Droid API.
 *
 * Example response:
 * ```json
 * {
 *   "packageName": "org.fdroid.fdroid",
 *   "suggestedVersionCode": 1009000,
 *   "packages": [
 *     {
 *       "versionName": "1.10-alpha0",
 *       "versionCode": 1010000
 *     },
 *     {
 *       "versionName": "1.9",
 *       "versionCode": 1009000
 *     }
 *   ]
 * }
 * ```
 */
@Serializable
data class FDroidPackageInfo(
    /**
     * The package name of the application.
     */
    val packageName: String = "",

    /**
     * The suggested version code of the application.
     */
    val suggestedVersionCode: Long = 0,

    /**
     * List of available package versions.
     */
    val packages: List<FDroidPackageVersion> = emptyList()
) {
    /**
     * Base URL for F-Droid repository downloads.
     */
    private val baseRepoUrl = "https://f-droid.org/repo/"

    /**
     * Gets the download link for the package with the specified version code.
     *
     * @param versionCode The version code of the package.
     * @return The download link in the format "https://f-droid.org/repo/packagename_versioncode.apk",
     *         or null if the version code doesn't exist in the packages list.
     */
    fun getDownloadLink(versionCode: Long): String? {
        // Check if the version exists in the packages list
        val versionExists = packages.any { it.versionCode == versionCode }
        if (!versionExists) {
            return null
        }

        return "$baseRepoUrl${packageName}_$versionCode.apk"
    }

    /**
     * Gets the download link for the suggested version of the package.
     *
     * @return The download link for the suggested version,
     *         or null if the suggested version doesn't exist in the packages list.
     */
    fun getSuggestedVersionDownloadLink(): String? {
        return getDownloadLink(suggestedVersionCode)
    }
}

/**
 * Data class representing a version of a package in F-Droid.
 */
@Serializable
data class FDroidPackageVersion(
    /**
     * The version name of the package (e.g., "1.9").
     */
    val versionName: String = "",

    /**
     * The version code of the package (e.g., 1009000).
     */
    val versionCode: Long = 0
)
