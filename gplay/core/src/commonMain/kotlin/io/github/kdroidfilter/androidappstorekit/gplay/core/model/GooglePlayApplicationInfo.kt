package io.github.kdroidfilter.androidappstorekit.gplay.core.model

import kotlinx.serialization.Serializable

@Serializable
data class GooglePlayCategory(val name: String, val id: String)

@Serializable
data class GooglePlayApplicationInfo(
    val title: String = "",
    val description: String = "",
    val descriptionHTML: String = "",
    val summary: String = "",
    val installs: String = "",
    val minInstalls: Long = 0,
    val realInstalls: Long = 0,
    val score: Double = 0.0,
    val ratings: Long = 0,
    val reviews: Long = 0,
    val histogram: List<Long> = emptyList(),
    val price: Double = 0.0,
    val free: Boolean = false,
    val currency: String = "",
    val sale: Boolean = false,
    val saleTime: Long? = null,
    val originalPrice: Double? = null,
    val saleText: String? = null,
    val offersIAP: Boolean = false,
    val inAppProductPrice: String = "",
    val developer: String = "",
    val developerId: String = "",
    val developerEmail: String = "",
    val developerWebsite: String = "",
    val developerAddress: String = "",
    val privacyPolicy: String = "",
    val genre: String = "",
    val genreId: String = "",
    val categories: List<GooglePlayCategory> = emptyList(),
    val icon: String = "",
    val headerImage: String = "",
    val screenshots: List<String> = emptyList(),
    val video: String = "",
    val videoImage: String = "",
    val contentRating: String = "",
    val contentRatingDescription: String = "",
    val adSupported: Boolean = false,
    val containsAds: Boolean = false,
    val released: String = "",
    val updated: Long = 0,
    val version: String = "Varies with device",
    val comments: List<String> = emptyList(),
    val appId: String = "",
    val url: String = ""
)