package io.github.kdroidfilter.androidappstorekit.gplay.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a category on the Google Play Store.
 *
 * @property name Name of the category
 * @property id Unique identifier of the category
 */
@Serializable
data class GooglePlayCategory(val name: String, val id: String)

/**
 * Comprehensive data model that represents detailed information about an application on the Google Play Store.
 *
 * @property title The title/name of the application
 * @property description Plain text description of the application
 * @property descriptionHTML HTML-formatted description of the application
 * @property summary A short summary of the application
 * @property installs Number of installs as a formatted string (e.g., "10,000,000+")
 * @property minInstalls Minimum number of installs as a numeric value
 * @property realInstalls Estimated real number of installs
 * @property score Overall rating score (0.0 to 5.0)
 * @property ratings Total number of ratings
 * @property reviews Total number of reviews
 * @property histogram Distribution of ratings (1-5 stars)
 * @property price Price of the application in the specified currency
 * @property free Whether the application is free
 * @property currency Currency code for the price (e.g., "USD")
 * @property sale Whether the application is on sale
 * @property saleTime Duration of the sale in milliseconds
 * @property originalPrice Original price before the sale
 * @property saleText Text describing the sale
 * @property offersIAP Whether the application offers in-app purchases
 * @property inAppProductPrice Price range for in-app purchases
 * @property developer Name of the developer
 * @property developerId Unique identifier of the developer
 * @property developerEmail Contact email of the developer
 * @property developerWebsite Website of the developer
 * @property developerAddress Physical address of the developer
 * @property privacyPolicy URL to the privacy policy
 * @property genre Primary genre of the application
 * @property genreId Identifier of the primary genre
 * @property categories List of categories the application belongs to
 * @property icon URL to the application icon
 * @property headerImage URL to the header image
 * @property screenshots URLs to the application screenshots
 * @property video URL to the promotional video
 * @property videoImage URL to the video thumbnail
 * @property contentRating Content rating (e.g., "Everyone", "Teen")
 * @property contentRatingDescription Description of the content rating
 * @property adSupported Whether the application is supported by ads
 * @property containsAds Whether the application contains ads
 * @property released Release date of the application
 * @property updated Last update timestamp
 * @property version Current version of the application
 * @property comments List of user comments
 * @property appId Unique identifier of the application
 * @property url URL to the application's page on Google Play
 */
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
