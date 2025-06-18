# 💡 StoreKit Library

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kdroidfilter/storekit-gplay-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.kdroidfilter%22%20AND%20a:%22storekit-gplay-core%22)

## 📑 Table of Contents
- [Introduction](#-introduction)
- [Features](#-features)
- [Google Play Store](#-google-play-store)
  - [Installation](#google-play-installation)
  - [Usage](#google-play-usage)
  - [Data Models](#google-play-data-models)
- [F-Droid Store](#-f-droid-store)
  - [Installation](#f-droid-installation)
  - [Usage](#f-droid-usage)
  - [Data Models](#f-droid-data-models)
- [Aptoide Store](#-aptoide-store)
  - [Installation](#aptoide-installation)
  - [Usage](#aptoide-usage)
  - [Data Models](#aptoide-data-models)
- [App Signature](#-app-signature)
  - [Installation](#app-signature-installation)
  - [Usage](#app-signature-usage)
- [Configuration](#-configuration)
- [Contributions](#-contributions)
- [License](#-license)
- [Disclaimer](#-disclaimer)

## 🔄 Introduction

StoreKit is a comprehensive Kotlin multiplatform library for extracting detailed information about applications from various Android app stores. Currently, it supports:

1. **Google Play Store** - A full Kotlin adaptation of the [google-play-scraper](https://github.com/JoMingyu/google-play-scraper) Python library
2. **F-Droid Store** - A client for the F-Droid API
3. **Aptoide Store** - A client for the Aptoide API
4. **App Signature** - A utility for extracting SHA1 signatures from installed Android applications

With this library, you can fetch details such as application descriptions, developer information, categories, reviews, ratings, and more from multiple app stores using a consistent API.

This library is built using Kotlin Multiplatform and leverages kotlinx.serialization for parsing JSON data and Ktor for HTTP requests.

## 📊 Features

- **Multi-store support**: Extract app data from Google Play Store, F-Droid, and Aptoide
- **Comprehensive app details**: Fetch application information including title, description, developer info, price, etc.
- **Rich metadata**: Retrieve app ratings, reviews, download counts, screenshots, icons, and videos
- **App signatures**: Extract SHA1 signatures from installed Android applications
- **Cross-platform**: Works on Android, JVM, and other platforms supported by Kotlin Multiplatform
- **Modular architecture**: Use only the modules you need for your specific use case

## 🔍 Google Play Store

### Google Play Installation

The Google Play modules are available on Maven Central:

#### Google Play Scraper Module

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kdroidfilter/storekit-gplay-scrapper.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.kdroidfilter%22%20AND%20a:%22storekit-gplay-scrapper%22)

For scraping data from the Google Play Store:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:storekit-gplay-scrapper:<version>")
}
```

#### Google Play Core Module

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kdroidfilter/storekit-gplay-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.kdroidfilter%22%20AND%20a:%22storekit-gplay-core%22)

If you only need the model classes (like `GooglePlayApplicationInfo` and `GooglePlayCategory`) without the scraping functionality:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:storekit-gplay-core:<version>")
}
```

### Google Play Usage

#### Fetching App Information from Google Play

To fetch detailed information about a specific app from Google Play, you can use the `getGooglePlayApplicationInfo` function. This function requires the app ID as a parameter and optionally takes the language and country codes.

##### Parameters:

- `appId: String` (mandatory): The unique identifier of the app.
- `lang: String` (optional, default = "en"): Language code for the app details (ISO 639-1).
- `country: String` (optional, default = "us"): Country code for the app details (ISO 3166).

##### Example:

```kotlin
fun main() {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    runBlocking {
        val appId = "com.android.chrome"
        val appInfo = getGooglePlayApplicationInfo(appId)
        println(json.encodeToString(appInfo))
    }
}
```

### Google Play Data Models

#### `GooglePlayApplicationInfo`

The `GooglePlayApplicationInfo` class is a comprehensive data model that represents detailed information about an application on the Google Play Store. It contains the following fields:

- `title: String` - The title/name of the application
- `description: String` - Plain text description of the application
- `descriptionHTML: String` - HTML-formatted description of the application
- `summary: String` - A short summary of the application
- `installs: String` - Number of installs as a formatted string (e.g., "10,000,000+")
- `minInstalls: Long` - Minimum number of installs as a numeric value
- `realInstalls: Long` - Estimated real number of installs
- `score: Double` - Overall rating score (0.0 to 5.0)
- `ratings: Long` - Total number of ratings
- `reviews: Long` - Total number of reviews
- `histogram: List<Long>` - Distribution of ratings (1-5 stars)
- `price: Double` - Price of the application in the specified currency
- `free: Boolean` - Whether the application is free
- `currency: String` - Currency code for the price (e.g., "USD")
- `sale: Boolean` - Whether the application is on sale
- `saleTime: Long?` - Duration of the sale in milliseconds
- `originalPrice: Double?` - Original price before the sale
- `saleText: String?` - Text describing the sale
- `offersIAP: Boolean` - Whether the application offers in-app purchases
- `inAppProductPrice: String` - Price range for in-app purchases
- `developer: String` - Name of the developer
- `developerId: String` - Unique identifier of the developer
- `developerEmail: String` - Contact email of the developer
- `developerWebsite: String` - Website of the developer
- `developerAddress: String` - Physical address of the developer
- `privacyPolicy: String` - URL to the privacy policy
- `genre: String` - Primary genre of the application
- `genreId: String` - Identifier of the primary genre
- `categories: List<GooglePlayCategory>` - List of categories the application belongs to
- `icon: String` - URL to the application icon
- `headerImage: String` - URL to the header image
- `screenshots: List<String>` - URLs to the application screenshots
- `video: String` - URL to the promotional video
- `videoImage: String` - URL to the video thumbnail
- `contentRating: String` - Content rating (e.g., "Everyone", "Teen")
- `contentRatingDescription: String` - Description of the content rating
- `adSupported: Boolean` - Whether the application is supported by ads
- `containsAds: Boolean` - Whether the application contains ads
- `released: String` - Release date of the application
- `updated: Long` - Last update timestamp
- `version: String` - Current version of the application
- `comments: List<String>` - List of user comments
- `appId: String` - Unique identifier of the application
- `url: String` - URL to the application's page on Google Play

#### `GooglePlayCategory`

The `GooglePlayCategory` class represents a category on the Google Play Store with the following fields:

- `name: String` - Name of the category
- `id: String` - Unique identifier of the category

## 📦 F-Droid Store

### F-Droid Installation

The F-Droid modules are available on Maven Central:

#### F-Droid API Module

For accessing the F-Droid API:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:storekit-fdroid-api:<version>")
}
```

#### F-Droid Core Module

If you only need the model classes (like `FDroidPackageInfo`) without the API client:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:storekit-fdroid-core:<version>")
}
```

### F-Droid Usage

#### Fetching Package Information from F-Droid

To fetch detailed information about a specific package from F-Droid, you can use the `FDroidService` class. The class provides a method to retrieve package information using the package name.

##### Methods:

###### `getPackageInfo`

Fetches package information using the package name.

**Parameters:**
- `packageName: String` (mandatory): The package name of the application.

**Returns:** An instance of `FDroidPackageInfo` containing the package information.

**Throws:** `IllegalArgumentException` if the package with the given package name does not exist or is not accessible.

##### Example:

```kotlin
fun main() {
    val json = Json { 
        prettyPrint = true 
        encodeDefaults = true
    }

    runBlocking {
        try {
            val fdroidService = FDroidService()

            // Fetch package information by package name
            val packageInfo = fdroidService.getPackageInfo("net.thunderbird.android")
            println(json.encodeToString(packageInfo))

            // Get download link for the suggested version
            val downloadLink = packageInfo.getSuggestedVersionDownloadLink()
            println("Download link: $downloadLink")

        } catch (e: Exception) {
            println("Error fetching package information: ${e.message}")
            e.printStackTrace()
        }
    }
}
```

### F-Droid Data Models

#### `FDroidPackageInfo`

The `FDroidPackageInfo` class represents detailed information about a package on the F-Droid store. It contains the following fields:

- `packageName: String` - The package name of the application
- `suggestedVersionCode: Long` - The suggested version code of the application
- `packages: List<FDroidPackageVersion>` - List of available package versions

##### Methods:

- `getDownloadLink(versionCode: Long): String?` - Gets the download link for the package with the specified version code. Returns null if the version code doesn't exist in the packages list.
- `getSuggestedVersionDownloadLink(): String?` - Gets the download link for the suggested version of the package. Returns null if the suggested version doesn't exist in the packages list.

#### `FDroidPackageVersion`

The `FDroidPackageVersion` class represents a version of a package in F-Droid with the following fields:

- `versionName: String` - The version name of the package (e.g., "1.9")
- `versionCode: Long` - The version code of the package (e.g., 1009000)

## 📱 Aptoide Store

### Aptoide Installation

The Aptoide modules are available on Maven Central:

#### Aptoide API Module

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kdroidfilter/storekit-aptoide-api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.kdroidfilter%22%20AND%20a:%22storekit-aptoide-api%22)

For accessing the Aptoide API:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:storekit-aptoide-api:<version>")
}
```

#### Aptoide Core Module

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kdroidfilter/storekit-aptoide-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.kdroidfilter%22%20AND%20a:%22storekit-aptoide-core%22)

If you only need the model classes (like `AptoideApplicationInfo`) without the API client:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:storekit-aptoide-core:<version>")
}
```

### Aptoide Usage

#### Fetching App Information from Aptoide

To fetch detailed information about a specific app from Aptoide, you can use the `AptoideService` class. The class provides several methods to retrieve app information using different identifiers.

##### Methods:

###### `getAppMetaByPackageName`

Fetches application metadata using the package name.

**Parameters:**
- `packageName: String` (mandatory): The package name of the application.
- `language: String` (optional, default = "en"): Language code for the content localization.

**Returns:** An instance of `AptoideApplicationInfo` containing the application's metadata.

###### `getAppMetaById`

Fetches application metadata using the app ID.

**Parameters:**
- `appId: Long` (mandatory): The ID of the application.
- `language: String` (optional, default = "en"): Language code for the content localization.

**Returns:** An instance of `AptoideApplicationInfo` containing the application's metadata.

###### `getAppMetaByMd5sum`

Fetches application metadata using the APK MD5 sum.

**Parameters:**
- `md5sum: String` (mandatory): The MD5 sum of the APK file.
- `language: String` (optional, default = "en"): Language code for the content localization.

**Returns:** An instance of `AptoideApplicationInfo` containing the application's metadata.

##### Example:

```kotlin
fun main() {
    val json = Json { prettyPrint = true }
    val aptoideService = AptoideService()

    runBlocking {
        try {
            // Fetch app metadata by package name
            val appInfo = aptoideService.getAppMetaByPackageName("com.waze", "en")
            println(json.encodeToString(appInfo))

            // Fetch app metadata by app ID
            val appInfoById = aptoideService.getAppMetaById(12345, "en")
            println(json.encodeToString(appInfoById))

            // Fetch app metadata by APK MD5 sum
            val appInfoByMd5 = aptoideService.getAppMetaByMd5sum("abcdef1234567890abcdef1234567890", "en")
            println(json.encodeToString(appInfoByMd5))

        } catch (e: Exception) {
            println("Error fetching app metadata: ${e.message}")
            e.printStackTrace()
        }
    }
}
```

### Aptoide Data Models

#### `AptoideApplicationInfo`

The `AptoideApplicationInfo` class is a comprehensive data model that represents detailed information about an application on the Aptoide store. It contains the following fields:

- `id: Long` - Unique identifier of the application
- `name: String` - Name of the application
- `packageName: String` - Package name of the application
- `uname: String` - Unique name of the application
- `size: Long` - Size of the application in bytes
- `icon: String` - URL to the application icon
- `graphic: String` - URL to the application graphic
- `added: String` - Date when the application was added
- `modified: String` - Date when the application was last modified
- `updated: String` - Date when the application was last updated
- `main_package: String?` - Main package name if different
- `age: AptoideAge` - Age rating information
- `developer: AptoideDeveloper` - Developer information
- `store: AptoideStore` - Store information
- `file: AptoideFile` - File information
- `media: AptoideMedia` - Media assets
- `urls: AptoideUrls` - URLs related to the application
- `stats: AptoideStats` - Statistics about the application
- `aab: String?` - Android App Bundle information
- `obb: String?` - OBB file information
- `pay: String?` - Payment information
- `appcoins: AptoideAppcoins` - AppCoins information
- `soft_locks: List<String>` - Soft locks information

#### `AptoideAge`

The `AptoideAge` class contains age rating information with the following fields:

- `name: String` - Name of the age rating
- `title: String` - Title of the age rating
- `pegi: String` - PEGI rating
- `rating: Int` - Numeric rating

#### `AptoideDeveloper`

The `AptoideDeveloper` class contains information about the developer with the following fields:

- `id: Long` - Unique identifier of the developer
- `name: String` - Name of the developer
- `website: String` - Website of the developer
- `email: String` - Email of the developer
- `privacy: String?` - Privacy policy URL

#### `AptoideStore`

The `AptoideStore` class contains information about the store with the following fields:

- `id: Long` - Unique identifier of the store
- `name: String` - Name of the store
- `avatar: String` - URL to the store avatar
- `appearance: AptoideAppearance` - Appearance information
- `stats: AptoideStoreStats` - Statistics about the store

#### `AptoideAppearance`

The `AptoideAppearance` class contains information about the store appearance with the following fields:

- `theme: String` - Theme of the store
- `description: String` - Description of the store

#### `AptoideStoreStats`

The `AptoideStoreStats` class contains statistics about the store with the following fields:

- `apps: Int` - Number of applications in the store
- `subscribers: Int` - Number of subscribers to the store
- `downloads: Int` - Number of downloads from the store

#### `AptoideFile`

The `AptoideFile` class contains information about the application file with the following fields:

- `vername: String` - Version name
- `vercode: Int` - Version code
- `md5sum: String` - MD5 checksum
- `filesize: Long` - File size in bytes
- `signature: AptoideSignature` - Signature information
- `added: String` - Date when the file was added
- `path: String` - Path to the file
- `path_alt: String` - Alternative path to the file
- `hardware: AptoideHardware` - Hardware requirements
- `malware: AptoideMalware` - Malware scanning information
- `flags: AptoideFlags` - Flags information
- `used_features: List<String>` - List of used features
- `used_permissions: List<String>` - List of used permissions
- `tags: List<String>` - List of tags

#### `AptoideSignature`

The `AptoideSignature` class contains signature information with the following fields:

- `sha1: String` - SHA1 hash of the signature
- `owner: String` - Owner of the signature

##### Extension Functions

- `toFormattedSha1()` - Converts the SHA1 signature format from "XX:XX:XX..." to a continuous lowercase hex string.
  Example: "35:B4:38:FE:1B:C6:9D:97:5D:C8:70:2D:C1:6A:B6:9E:BF:65:F2:6F" becomes "35b438fe1bc69d975dc8702dc16ab69ebf65f26f"

#### `AptoideHardware`

The `AptoideHardware` class contains hardware requirements with the following fields:

- `sdk: Int` - Minimum SDK version
- `screen: String` - Screen requirements
- `gles: Int` - OpenGL ES version
- `cpus: List<String>` - Supported CPUs
- `densities: List<String>` - Supported screen densities
- `dependencies: List<AptoideDependency>` - List of dependencies

#### `AptoideDependency`

The `AptoideDependency` class contains dependency information with the following fields:

- `type: String` - Type of dependency
- `level: String` - Level of dependency

#### `AptoideMalware`

The `AptoideMalware` class contains malware scanning information with the following fields:

- `rank: String` - Malware rank
- `reason: AptoideMalwareReason` - Reason for the malware rank
- `added: String` - Date when the malware scan was added
- `modified: String` - Date when the malware scan was modified

#### `AptoideMalwareReason`

The `AptoideMalwareReason` class contains the reason for the malware rank with the following fields:

- `signature_validated: AptoideSignatureValidated` - Signature validation information

#### `AptoideSignatureValidated`

The `AptoideSignatureValidated` class contains signature validation information with the following fields:

- `date: String` - Date of validation
- `status: String` - Status of validation
- `signature_from: String` - Source of the signature

#### `AptoideFlags`

The `AptoideFlags` class contains flags information with the following fields:

- `votes: List<AptoideVote>` - List of votes

#### `AptoideVote`

The `AptoideVote` class contains vote information with the following fields:

- `type: String` - Type of vote
- `count: Int` - Number of votes

#### `AptoideMedia`

The `AptoideMedia` class contains media assets with the following fields:

- `keywords: List<String>` - List of keywords
- `description: String` - Description of the application
- `summary: String` - Summary of the application
- `news: String` - News about the application
- `videos: List<AptoideVideo>` - List of videos
- `screenshots: List<AptoideScreenshot>` - List of screenshots

#### `AptoideVideo`

The `AptoideVideo` class contains video information with the following fields:

- `type: String` - Type of video
- `url: String` - URL to the video

#### `AptoideScreenshot`

The `AptoideScreenshot` class contains screenshot information with the following fields:

- `url: String` - URL to the screenshot
- `height: Int` - Height of the screenshot
- `width: Int` - Width of the screenshot

#### `AptoideUrls`

The `AptoideUrls` class contains URLs related to the application with the following fields:

- `w: String` - Web URL
- `m: String` - Mobile URL

#### `AptoideStats`

The `AptoideStats` class contains statistics about the application with the following fields:

- `rating: AptoideRating` - Rating information
- `prating: AptoideRating` - Previous rating information
- `downloads: Int` - Number of downloads
- `pdownloads: Int` - Previous number of downloads

#### `AptoideRating`

The `AptoideRating` class contains rating information with the following fields:

- `avg: Double` - Average rating
- `total: Int` - Total number of ratings
- `votes: List<AptoideRatingVote>` - List of votes

#### `AptoideRatingVote`

The `AptoideRatingVote` class contains rating vote information with the following fields:

- `value: Int` - Rating value
- `count: Int` - Number of votes

#### `AptoideAppcoins`

The `AptoideAppcoins` class contains AppCoins information with the following fields:

- `advertising: Boolean` - Whether the application supports AppCoins advertising
- `billing: Boolean` - Whether the application supports AppCoins billing
- `flags: List<String>` - List of AppCoins flags

#### `AptoideResponse`

The `AptoideResponse` class is the top-level response from the Aptoide API with the following fields:

- `info: AptoideInfo` - Information about the response
- `data: AptoideApplicationInfo` - Application information

#### `AptoideInfo`

The `AptoideInfo` class contains information about the response with the following fields:

- `status: String` - Status of the response
- `time: AptoideTime` - Time information

#### `AptoideTime`

The `AptoideTime` class contains time information with the following fields:

- `seconds: Double` - Time in seconds
- `human: String` - Human-readable time

## 📱 App Signature

### App Signature Installation

The App Signature module is available on Maven Central:

#### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:storekit-signature:<version>")
}
```

### App Signature Usage

#### Extracting SHA1 Signature from an Installed App

To extract the SHA1 signature of an installed Android application, you can use the `SignatureExtractor` class:

```kotlin
// In your Android activity or fragment
fun extractSignature(context: Context) {
    val packageName = "com.example.app" // Package name of the app

    // Extract the SHA1 signature
    val signature = SignatureExtractor.extractSha1Signature(context, packageName)

    // Use the signature
    if (signature != null) {
        println("SHA1 Signature: $signature")
    } else {
        println("Failed to extract signature. App might not be installed.")
    }
}
```

**Parameters:**
- `context`: The Android context.
- `packageName`: The package name of the application.

**Returns:** The SHA1 signature of the application as a string, or null if the application is not installed or the signature cannot be extracted.

#### Validating a SHA1 Signature

To validate if a string is a valid SHA1 signature, you can use the `isValidSha1Signature` function:

```kotlin
fun validateSignature() {
    val sha1String = "38918a453d07199354f8b19af05ec6562ced5788" // Example SHA1 signature

    // Validate the SHA1 signature
    val isValid = SignatureExtractor.isValidSha1Signature(sha1String)

    if (isValid) {
        println("The signature is a valid SHA1 signature")
    } else {
        println("The signature is not a valid SHA1 signature")
    }
}
```

**Parameters:**
- `sha1String`: The SHA1 string to verify.

**Returns:** True if the string is a valid SHA1 signature (40 characters long and contains only hexadecimal digits), false otherwise.

#### Verifying an App's Signature

To verify if an installed app's signature matches an expected SHA1 signature, you can use the `verifyPackageSignature` function. This function already validates the SHA1 signature internally, so you don't need to call `isValidSha1Signature` separately:

```kotlin
fun verifyAppSignature(context: Context) {
    val packageName = "com.android.chrome" // Package name of the app
    val expectedSignature = "38918a453d07199354f8b19af05ec6562ced5788" // Expected SHA1 signature

    // Verify the app's signature
    val isSignatureValid = SignatureExtractor.verifyPackageSignature(context, packageName, expectedSignature)

    if (isSignatureValid) {
        println("The app's signature matches the expected signature")
    } else {
        println("The app's signature does not match the expected signature, or the app is not installed")
    }
}
```

**Parameters:**
- `context`: The Android context.
- `packageName`: The package name of the application to verify.
- `expectedSha1`: The expected SHA1 signature to compare against.

**Returns:** True if the signatures match, false otherwise (including if the package is not installed or the signature cannot be extracted, or if the provided SHA1 is invalid).

## 💧 Configuration

### Logging

The library includes a logger for debugging. You can customize or disable logging by modifying the logger configuration.

### Network Configuration

The library uses Ktor for network operations. Since Ktor dependencies are configured as `compileOnly`, you need to include the following Ktor dependencies in your project:

```kotlin
// Ktor dependencies - version 3.2.0 or later
implementation("io.ktor:ktor-client-core:$ktor_version")
implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
implementation("io.ktor:ktor-client-serialization:$ktor_version")
implementation("io.ktor:ktor-client-logging:$ktor_version")
implementation("io.ktor:ktor-client-cio:$ktor_version")
```

Also ensure that network permissions are properly configured for Android.

## 📚 Contributions

Contributions to the library are welcome. If you find a bug or want to request a feature, feel free to open an issue or submit a pull request on [GitHub](https://github.com/kdroidFilter/StoreKit).

## 📄 License

This library is licensed under the MIT License. See the LICENSE file for details.

## 🛑 Disclaimer

The user assumes full responsibility for the use of this library and must verify the legality of such use in their country.

This library is intended for educational and personal use. Scraping data from app stores may violate their terms of service. Use responsibly and at your own risk.
