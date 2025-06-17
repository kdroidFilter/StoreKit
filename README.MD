# 💡 AndroidAppStoreKit Library

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kdroidfilter.androidappstorekit.gplay/core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.kdroidfilter.androidappstorekit.gplay%22%20AND%20a:%22core%22)

## 📑 Table of Contents
- [Introduction](#-introduction)
- [Features](#-features)
- [Installation](#-installation)
  - [Google Play Modules](#google-play-modules)
  - [Aptoide Modules](#aptoide-modules)
- [Usage](#-usage)
  - [Google Play Store](#google-play-store)
  - [Aptoide Store](#aptoide-store)
- [Data Models](#-data-models)
  - [Google Play Models](#google-play-models)
  - [Aptoide Models](#aptoide-models)
- [Configuration](#-configuration)
- [Contributions](#-contributions)
- [License](#-license)
- [Disclaimer](#-disclaimer)

## 🔄 Introduction

AndroidAppStoreKit is a comprehensive Kotlin multiplatform library for extracting detailed information about applications from various Android app stores. Currently, it supports:

1. **Google Play Store** - A full Kotlin adaptation of the [google-play-scraper](https://github.com/JoMingyu/google-play-scraper) Python library
2. **Aptoide Store** - A client for the Aptoide API

With this library, you can fetch details such as application descriptions, developer information, categories, reviews, ratings, and more from multiple app stores using a consistent API.

This library is built using Kotlin Multiplatform and leverages kotlinx.serialization for parsing JSON data and Ktor for HTTP requests.

## 📊 Features

- **Multi-store support**: Extract app data from Google Play Store and Aptoide
- **Comprehensive app details**: Fetch application information including title, description, developer info, price, etc.
- **Rich metadata**: Retrieve app ratings, reviews, download counts, screenshots, icons, and videos
- **Cross-platform**: Works on Android, iOS, JVM, and other platforms supported by Kotlin Multiplatform
- **Modular architecture**: Use only the modules you need for your specific use case

## 📦 Installation

The library is available on Maven Central and is organized into several modules:

### Google Play Modules

#### Google Play Scraper Module

For scraping data from the Google Play Store:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter.androidappstorekit.gplay:scrapper:<version>")
}
```

##### Gradle Groovy DSL

```groovy
dependencies {
    implementation 'io.github.kdroidfilter.androidappstorekit.gplay:scrapper:<version>'
}
```

#### Google Play Core Module

If you only need the model classes (like `GooglePlayApplicationInfo` and `GooglePlayCategory`) without the scraping functionality:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter.androidappstorekit.gplay:core:<version>")
}
```

##### Gradle Groovy DSL

```groovy
dependencies {
    implementation 'io.github.kdroidfilter.androidappstorekit.gplay:core:<version>'
}
```

### Aptoide Modules

#### Aptoide API Module

For accessing the Aptoide API:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter.androidappstorekit.aptoide:api:<version>")
}
```

##### Gradle Groovy DSL

```groovy
dependencies {
    implementation 'io.github.kdroidfilter.androidappstorekit.aptoide:api:<version>'
}
```

#### Aptoide Core Module

If you only need the model classes (like `AptoideApplicationInfo`) without the API client:

##### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter.androidappstorekit.aptoide:core:<version>")
}
```

##### Gradle Groovy DSL

```groovy
dependencies {
    implementation 'io.github.kdroidfilter.androidappstorekit.aptoide:core:<version>'
}
```

## 🔄 Usage

### Google Play Store

#### Fetching App Information from Google Play

To fetch detailed information about a specific app from Google Play, you can use the `getGooglePlayApplicationInfo` function. This function requires the app ID as a parameter and optionally takes the language and country codes.

##### Parameters:

- `appId: String` (mandatory): The unique identifier of the app.
- `lang: String` (optional, default = "en"): Language code for the app details (ISO 639-1).
- `country: String` (optional, default = "us"): Country code for the app details (ISO 3166).

##### Example:

```kotlin
import io.github.kdroidfilter.androidappstorekit.gplay.scrapper.getGooglePlayApplicationInfo
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

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

#### Using Google Play Scraper in Java

The library also supports Java through the `getGooglePlayApplicationInfoAsync` function, which returns a `CompletableFuture`.

##### Example:

```java
import io.github.kdroidfilter.androidappstorekit.gplay.scrapper.JavaImplKt;
import io.github.kdroidfilter.androidappstorekit.gplay.core.model.GooglePlayApplicationInfo;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        String appId = "com.android.chrome";

        // Call Kotlin function with parameters
        CompletableFuture<GooglePlayApplicationInfo> appInfoFuture = 
            JavaImplKt.getGooglePlayApplicationInfoAsync(appId, "en", "us");

        appInfoFuture.thenAccept(appInfo -> {
            System.out.println("App Info: " + appInfo.getTitle());
        }).exceptionally(throwable -> {
            System.err.println("Error: " + throwable.getMessage());
            return null;
        });
    }
}
```

### Aptoide Store

#### Fetching App Information from Aptoide

To fetch detailed information about a specific app from Aptoide, you can use the `AptoideService` class.

##### Example:

```kotlin
import io.github.kdroidfilter.androidappstorekit.aptoide.api.services.AptoideService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

fun main() {
    val json = Json { prettyPrint = true }
    val aptoideService = AptoideService()
    
    runBlocking {
        try {
            // Fetch app metadata by package name
            // The second parameter is the store name (e.g., "eb" for the main Aptoide store)
            val appInfo = aptoideService.getAppMetaByPackageName("com.waze", "eb")
            println(json.encodeToString(appInfo))
            
        } catch (e: Exception) {
            println("Error fetching app metadata: ${e.message}")
            e.printStackTrace()
        }
    }
}
```

## 🔗 Data Models

The library provides data models for both Google Play Store and Aptoide.

### Google Play Models

#### `GooglePlayApplicationInfo`

Represents detailed app information from Google Play, including:

- `title`: App title
- `description`: Plain text description of the app
- `score`: Overall rating score
- `categories`: List of `GooglePlayCategory` objects
- `developer`: Developer name
- `price`: App price
- and more

##### Example JSON Output (Google Play)

```json
{
    "title": "Google Chrome",
    "summary": "Google's fast and secure browser",
    "installs": "10,000,000,000+",
    "minInstalls": 10000000000,
    "score": 4.065567,
    "ratings": 46344164,
    "reviews": 992885,
    "price": 0.0,
    "free": true,
    "currency": "USD",
    "developer": "Google LLC",
    "developerId": "5700313618786177705",
    "developerEmail": "apps-help@google.com",
    "developerWebsite": "http://www.google.com/chrome/android",
    "genre": "Communication",
    "genreId": "COMMUNICATION",
    "icon": "https://play-lh.googleusercontent.com/QRRGW2tMZ4-FNw0XWk6WWiXHaQCGxuwM-92HrBhlA4WOd_AGmjVmQkiHyAqQjW2yByc",
    "appId": "com.android.chrome",
    "url": "https://play.google.com/store/apps/details?id=com.android.chrome&hl=en&gl=us"
}
```

#### `GooglePlayCategory`

Represents app categories with:

- `name`: Category name
- `id`: Category ID

### Aptoide Models

#### `AptoideApplicationInfo`

Represents detailed app information from Aptoide, including:

- `id`: App ID
- `name`: App name
- `packageName`: Package name
- `size`: App size in bytes
- `icon`: URL to the app icon
- `developer`: Developer information
- `stats`: App statistics (ratings, downloads)
- `media`: App media (screenshots, videos)
- and more

##### Example JSON Output (Aptoide)

```json
{
  "id": 12345,
  "name": "Waze",
  "package": "com.waze",
  "size": 15000000,
  "icon": "https://cdn.aptoide.com/imgs/w/a/z/waze.png",
  "developer": {
    "id": 123,
    "name": "Waze Inc.",
    "website": "https://www.waze.com",
    "email": "support@waze.com"
  },
  "stats": {
    "rating": {
      "avg": 4.5,
      "total": 10000
    },
    "downloads": 5000000
  },
  "media": {
    "description": "Waze is a GPS navigation software app...",
    "screenshots": [
      {
        "url": "https://cdn.aptoide.com/screenshots/waze1.jpg",
        "height": 1920,
        "width": 1080
      }
    ]
  }
}
```

## 💧 Configuration

### Logging

The library includes a logger for debugging. You can customize or disable logging by modifying the logger configuration.

### Network Configuration

The library uses Ktor for network operations. Since Ktor dependencies are configured as `compileOnly`, you need to include the following Ktor dependencies in your project:

```kotlin
// Ktor dependencies
implementation("io.ktor:ktor-client-core:$ktor_version")
implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
implementation("io.ktor:ktor-client-serialization:$ktor_version")
implementation("io.ktor:ktor-client-logging:$ktor_version")
implementation("io.ktor:ktor-client-cio:$ktor_version")
```

Also ensure that network permissions are properly configured for Android.

## 📚 Contributions

Contributions to the library are welcome. If you find a bug or want to request a feature, feel free to open an issue or submit a pull request on GitHub.

## 📄 License

This library is licensed under the MIT License. See the LICENSE file for details.

## 🛑 Disclaimer

The user assumes full responsibility for the use of this library and must verify the legality of such use in their country.

This library is intended for educational and personal use. Scraping data from app stores may violate their terms of service. Use responsibly and at your own risk.