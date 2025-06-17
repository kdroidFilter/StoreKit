package com.kdroid.aptoide.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
data class AptoideAge(
    val name: String = "",
    val title: String = "",
    val pegi: String = "",
    val rating: Int = 0
)

@Serializable
data class AptoideDeveloper(
    val id: Long = 0,
    val name: String = "",
    val website: String = "",
    val email: String = "",
    val privacy: String? = null
)

@Serializable
data class AptoideStore(
    val id: Long = 0,
    val name: String = "",
    val avatar: String = "",
    val appearance: AptoideAppearance = AptoideAppearance(),
    val stats: AptoideStoreStats = AptoideStoreStats()
)

@Serializable
data class AptoideAppearance(
    val theme: String = "",
    val description: String = ""
)

@Serializable
data class AptoideStoreStats(
    val apps: Int = 0,
    val subscribers: Int = 0,
    val downloads: Int = 0
)

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

@Serializable
data class AptoideSignature(
    val sha1: String = "",
    val owner: String = ""
)

@Serializable
data class AptoideDependency(
    val type: String = "",
    val level: String = ""
)

@Serializable
data class AptoideHardware(
    val sdk: Int = 0,
    val screen: String = "",
    val gles: Int = 0,
    val cpus: List<String> = emptyList(),
    val densities: List<String> = emptyList(),
    val dependencies: List<AptoideDependency> = emptyList()
)

@Serializable
data class AptoideMalware(
    val rank: String = "",
    val reason: AptoideMalwareReason = AptoideMalwareReason(),
    val added: String = "",
    val modified: String = ""
)

@Serializable
data class AptoideMalwareReason(
    val signature_validated: AptoideSignatureValidated = AptoideSignatureValidated()
)

@Serializable
data class AptoideSignatureValidated(
    val date: String = "",
    val status: String = "",
    val signature_from: String = ""
)

@Serializable
data class AptoideFlags(
    val votes: List<AptoideVote> = emptyList()
)

@Serializable
data class AptoideVote(
    val type: String = "",
    val count: Int = 0
)

@Serializable
data class AptoideVideo(
    val type: String = "",
    val url: String = ""
)

@Serializable
data class AptoideMedia(
    val keywords: List<String> = emptyList(),
    val description: String = "",
    val summary: String = "",
    val news: String = "",
    val videos: List<AptoideVideo> = emptyList(),
    val screenshots: List<AptoideScreenshot> = emptyList()
)

@Serializable
data class AptoideScreenshot(
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0
)

@Serializable
data class AptoideUrls(
    val w: String = "",
    val m: String = ""
)

@Serializable
data class AptoideStats(
    val rating: AptoideRating = AptoideRating(),
    val prating: AptoideRating = AptoideRating(),
    val downloads: Int = 0,
    val pdownloads: Int = 0
)

@Serializable
data class AptoideRating(
    val avg: Double = 0.0,
    val total: Int = 0,
    val votes: List<AptoideRatingVote> = emptyList()
)

@Serializable
data class AptoideRatingVote(
    val value: Int = 0,
    val count: Int = 0
)

@Serializable
data class AptoideAppcoins(
    val advertising: Boolean = false,
    val billing: Boolean = false,
    val flags: List<String> = emptyList()
)

@Serializable
data class AptoideResponse(
    val info: AptoideInfo = AptoideInfo(),
    val data: AptoideApplicationInfo = AptoideApplicationInfo()
)

@Serializable
data class AptoideInfo(
    val status: String = "",
    val time: AptoideTime = AptoideTime()
)

@Serializable
data class AptoideTime(
    val seconds: Double = 0.0,
    val human: String = ""
)
