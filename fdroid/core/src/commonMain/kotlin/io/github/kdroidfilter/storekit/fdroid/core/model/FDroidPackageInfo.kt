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
)

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