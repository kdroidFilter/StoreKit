package io.github.kdroidfilter.storekit.aptoide.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Comprehensive data model that represents detailed information about an application on the Aptoide store.
 *
 * @property id Unique identifier of the application
 * @property name Name of the application
 * @property package_ Package name (alternative property)
 * @property packageName Package name of the application
 * @property uname Unique name of the application
 * @property size Size of the application in bytes
 * @property icon URL to the application icon
 * @property graphic URL to the application graphic
 * @property added Date when the application was added
 * @property modified Date when the application was last modified
 * @property updated Date when the application was last updated
 * @property main_package Main package name if different
 * @property age Age rating information
 * @property developer Developer information
 * @property store Store information
 * @property file File information
 * @property media Media assets
 * @property urls URLs related to the application
 * @property stats Statistics about the application
 * @property aab Android App Bundle information
 * @property obb OBB file information
 * @property pay Payment information
 * @property appcoins AppCoins information
 * @property soft_locks Soft locks information
 */
@Serializable
data class AptoideApplicationInfo(
    val id: Long = 0,
    val name: String = "",
    val package_: String = "",
    @SerialName("package")
    val packageName: String = "",
    val uname: String = "",
    val size: Long = 0,
    val icon: String = "",
    val graphic: String = "",
    val added: String = "",
    val modified: String = "",
    val updated: String = "",
    val main_package: String? = null,
    val age: AptoideAge = AptoideAge(),
    val developer: AptoideDeveloper = AptoideDeveloper(),
    val store: AptoideStore = AptoideStore(),
    val file: AptoideFile = AptoideFile(),
    val media: AptoideMedia = AptoideMedia(),
    val urls: AptoideUrls = AptoideUrls(),
    val stats: AptoideStats = AptoideStats(),
    val aab: String? = null,
    val obb: String? = null,
    val pay: String? = null,
    val appcoins: AptoideAppcoins = AptoideAppcoins(),
    val soft_locks: List<String> = emptyList()
)

/**
 * Contains age rating information for an application.
 *
 * @property name Name of the age rating
 * @property title Title of the age rating
 * @property pegi PEGI rating
 * @property rating Numeric rating
 */
@Serializable
data class AptoideAge(
    val name: String = "",
    val title: String = "",
    val pegi: String = "",
    val rating: Int = 0
)

/**
 * Contains information about the developer of an application.
 *
 * @property id Unique identifier of the developer
 * @property name Name of the developer
 * @property website Website of the developer
 * @property email Email of the developer
 * @property privacy Privacy policy URL
 */
@Serializable
data class AptoideDeveloper(
    val id: Long = 0,
    val name: String = "",
    val website: String = "",
    val email: String = "",
    val privacy: String? = null
)

/**
 * Contains information about the store where the application is hosted.
 *
 * @property id Unique identifier of the store
 * @property name Name of the store
 * @property avatar URL to the store avatar
 * @property appearance Appearance information
 * @property stats Statistics about the store
 */
@Serializable
data class AptoideStore(
    val id: Long = 0,
    val name: String = "",
    val avatar: String = "",
    val appearance: AptoideAppearance = AptoideAppearance(),
    val stats: AptoideStoreStats = AptoideStoreStats()
)

/**
 * Contains information about the store appearance.
 *
 * @property theme Theme of the store
 * @property description Description of the store
 */
@Serializable
data class AptoideAppearance(
    val theme: String = "",
    val description: String = ""
)

/**
 * Contains statistics about the store.
 *
 * @property apps Number of applications in the store
 * @property subscribers Number of subscribers to the store
 * @property downloads Number of downloads from the store
 */
@Serializable
data class AptoideStoreStats(
    val apps: Int = 0,
    val subscribers: Int = 0,
    val downloads: Int = 0
)

/**
 * Contains information about the application file.
 *
 * @property vername Version name
 * @property vercode Version code
 * @property md5sum MD5 checksum
 * @property filesize File size in bytes
 * @property signature Signature information
 * @property added Date when the file was added
 * @property path Path to the file
 * @property path_alt Alternative path to the file
 * @property hardware Hardware requirements
 * @property malware Malware scanning information
 * @property flags Flags information
 * @property used_features List of used features
 * @property used_permissions List of used permissions
 * @property tags List of tags
 */
@Serializable
data class AptoideFile(
    val vername: String = "",
    val vercode: Int = 0,
    val md5sum: String = "",
    val filesize: Long = 0,
    val signature: AptoideSignature = AptoideSignature(),
    val added: String = "",
    val path: String = "",
    val path_alt: String = "",
    val hardware: AptoideHardware = AptoideHardware(),
    val malware: AptoideMalware = AptoideMalware(),
    val flags: AptoideFlags = AptoideFlags(),
    val used_features: List<String> = emptyList(),
    val used_permissions: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)

/**
 * Contains signature information for the application file.
 *
 * @property sha1 SHA1 hash of the signature
 * @property owner Owner of the signature
 */
@Serializable
data class AptoideSignature(
    val sha1: String = "",
    val owner: String = ""
)

/**
 * Contains dependency information for the application.
 *
 * @property type Type of dependency
 * @property level Level of dependency
 */
@Serializable
data class AptoideDependency(
    val type: String = "",
    val level: String = ""
)

/**
 * Contains hardware requirements for the application.
 *
 * @property sdk Minimum SDK version
 * @property screen Screen requirements
 * @property gles OpenGL ES version
 * @property cpus Supported CPUs
 * @property densities Supported screen densities
 * @property dependencies List of dependencies
 */
@Serializable
data class AptoideHardware(
    val sdk: Int = 0,
    val screen: String = "",
    val gles: Int = 0,
    val cpus: List<String> = emptyList(),
    val densities: List<List<Int>> = emptyList(),
    val dependencies: List<AptoideDependency> = emptyList()
)

/**
 * Contains malware scanning information for the application.
 *
 * @property rank Malware rank
 * @property reason Reason for the malware rank
 * @property added Date when the malware scan was added
 * @property modified Date when the malware scan was modified
 */
@Serializable
data class AptoideMalware(
    val rank: String = "",
    val reason: AptoideMalwareReason = AptoideMalwareReason(),
    val added: String = "",
    val modified: String = ""
)

/**
 * Contains the reason for the malware rank.
 *
 * @property signature_validated Signature validation information
 */
@Serializable
data class AptoideMalwareReason(
    val signature_validated: AptoideSignatureValidated = AptoideSignatureValidated()
)

/**
 * Contains signature validation information.
 *
 * @property date Date of validation
 * @property status Status of validation
 * @property signature_from Source of the signature
 */
@Serializable
data class AptoideSignatureValidated(
    val date: String = "",
    val status: String = "",
    val signature_from: String = ""
)

/**
 * Contains flags information for the application.
 *
 * @property votes List of votes
 */
@Serializable
data class AptoideFlags(
    val votes: List<AptoideVote> = emptyList()
)

/**
 * Contains vote information.
 *
 * @property type Type of vote
 * @property count Number of votes
 */
@Serializable
data class AptoideVote(
    val type: String = "",
    val count: Int = 0
)

/**
 * Contains video information for the application.
 *
 * @property type Type of video
 * @property url URL to the video
 */
@Serializable
data class AptoideVideo(
    val type: String = "",
    val url: String = ""
)

/**
 * Contains media assets for the application.
 *
 * @property keywords List of keywords
 * @property description Description of the application
 * @property summary Summary of the application
 * @property news News about the application
 * @property videos List of videos
 * @property screenshots List of screenshots
 */
@Serializable
data class AptoideMedia(
    val keywords: List<String> = emptyList(),
    val description: String = "",
    val summary: String = "",
    val news: String = "",
    val videos: List<AptoideVideo> = emptyList(),
    val screenshots: List<AptoideScreenshot> = emptyList()
)

/**
 * Contains screenshot information for the application.
 *
 * @property url URL to the screenshot
 * @property height Height of the screenshot
 * @property width Width of the screenshot
 */
@Serializable
data class AptoideScreenshot(
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0
)

/**
 * Contains URLs related to the application.
 *
 * @property w Web URL
 * @property m Mobile URL
 */
@Serializable
data class AptoideUrls(
    val w: String = "",
    val m: String = ""
)

/**
 * Contains statistics about the application.
 *
 * @property rating Rating information
 * @property prating Previous rating information
 * @property downloads Number of downloads
 * @property pdownloads Previous number of downloads
 */
@Serializable
data class AptoideStats(
    val rating: AptoideRating = AptoideRating(),
    val prating: AptoideRating = AptoideRating(),
    val downloads: Int = 0,
    val pdownloads: Int = 0
)

/**
 * Contains rating information for the application.
 *
 * @property avg Average rating
 * @property total Total number of ratings
 * @property votes List of votes
 */
@Serializable
data class AptoideRating(
    val avg: Double = 0.0,
    val total: Int = 0,
    val votes: List<AptoideRatingVote> = emptyList()
)

/**
 * Contains rating vote information.
 *
 * @property value Rating value
 * @property count Number of votes
 */
@Serializable
data class AptoideRatingVote(
    val value: Int = 0,
    val count: Int = 0
)

/**
 * Contains AppCoins information for the application.
 *
 * @property advertising Whether the application supports AppCoins advertising
 * @property billing Whether the application supports AppCoins billing
 * @property flags List of AppCoins flags
 */
@Serializable
data class AptoideAppcoins(
    val advertising: Boolean = false,
    val billing: Boolean = false,
    val flags: List<String> = emptyList()
)

/**
 * Top-level response from the Aptoide API.
 *
 * @property info Information about the response
 * @property data Application information
 */
@Serializable
data class AptoideResponse(
    val info: AptoideInfo = AptoideInfo(),
    val data: AptoideApplicationInfo = AptoideApplicationInfo()
)

/**
 * Contains information about the response from the Aptoide API.
 *
 * @property status Status of the response
 * @property time Time information
 */
@Serializable
data class AptoideInfo(
    val status: String = "",
    val time: AptoideTime = AptoideTime()
)

/**
 * Contains time information for the Aptoide API response.
 *
 * @property seconds Time in seconds
 * @property human Human-readable time
 */
@Serializable
data class AptoideTime(
    val seconds: Double = 0.0,
    val human: String = ""
)
