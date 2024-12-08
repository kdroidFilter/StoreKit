# 💡 Google Play Scrapper Library

## 🔄 Introduction

This library is a full Kotlin adaptation of the [google-play-scraper](https://github.com/JoMingyu/google-play-scraper) python library.

The Google Play Scrapper Library provides a comprehensive way to extract detailed information about applications listed on the Google Play Store. With this library, you can fetch details such as application descriptions, developer information, categories, reviews, and more.

This library is built using Kotlin and leverages kotlinx.serialization for parsing JSON data and Ktor for HTTP requests.

## 📊 Features

- Fetch application details including title, description, developer info, price, etc.
- Extract application categories and genres.
- Retrieve app ratings, reviews, and download counts.
- Identify in-app purchase details and sale statuses.
- Collect metadata like screenshots, icons, and videos.

## 📦 Installation

This library is available on maven central, add the following dependency to your project:

### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("com.kdroid.gplayscrapper:library:0.1.0")
}
```

## 🔄 Usage

### Fetching App Information

To fetch detailed information about a specific app, you can use the `getGooglePlayApplicationInfo` function. This function requires the app ID as a parameter and optionally takes the language and country codes.

#### Example:

```kotlin
import com.kdroid.gplayscrapper.services.getGooglePlayApplicationInfo
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    runBlocking {
        val appId = "com.android.chrome" // Replace with the actual app ID
        val appInfo = getGooglePlayApplicationInfo(appId)
        println(json.encodeToString(appInfo))
    }
}
```

## 🔗 Data Models

The library uses the following primary data models:

### `GooglePlayApplicationInfo`

Represents detailed app information, including:

- `title`: App title.
- `description`: Plain text description of the app.
- `score`: Overall rating score.
- `categories`: List of `GooglePlayCategory` objects.
- `developer`: Developer name.
- `price`: App price.
- and more.

#### Example JSON Output

Below is an example of the JSON output generated for an app:

```json
{
    "title": "Google Chrome",
    "description": "Google Chrome is a fast, easy to use, and secure web browser. Designed for Android, Chrome brings you personalized news articles, quick links to your favorite sites, downloads, and Google Search and Google Translate built-in. Download now to enjoy the same Chrome web browser experience you love across all your devices.",
    "descriptionHTML": "Google Chrome is a fast, easy to use, and secure web browser. Designed for Android, Chrome brings you personalized news articles, quick links to your favorite sites, downloads, and Google Search and Google Translate built-in. Download now to enjoy the same Chrome web browser experience you love across all your devices.<br> <br> <b>Browse fast and type less.</b> Choose from personalized search results that instantly appear as you type and quickly browse previously visited web pages. Fill in forms quickly with Autofill.<br> <br> <b>Incognito Browsing.</b> Use Incognito mode to browse the internet without saving your history. Browse privately across all your devices.<br>",
    "summary": "Google's fast and secure browser",
    "installs": "10,000,000,000+",
    "minInstalls": 10000000000,
    "realInstalls": 16985273589,
    "score": 4.0655704,
    "ratings": 46344191,
    "reviews": 992887,
    "histogram": [
        5920640,
        2376715,
        3450945,
        5590723,
        29005127
    ],
    "price": 0.0,
    "free": true,
    "currency": "USD",
    "sale": false,
    "developer": "Google LLC",
    "developerId": "5700313618786177705",
    "developerEmail": "apps-help@google.com",
    "developerWebsite": "http://www.google.com/chrome/android",
    "developerAddress": "1600 Amphitheatre Parkway, Mountain View 94043",
    "privacyPolicy": "https://policies.google.com/privacy",
    "genre": "Communication",
    "categories": [
        {
            "name": "Communication",
            "id": "COMMUNICATION"
        }
    ],
    "icon": "https://play-lh.googleusercontent.com/QRRGW2tMZ4-FNw0XWk6WWiXHaQCGxuwM-92HrBhlA4WOd_AGmjVmQkiHyAqQjW2yByc",
    "screenshots": [
        "https://play-lh.googleusercontent.com/OMmJGYNXlDfnWFX8iS1Veq2hqYJAkp0KF4kjPMEBEkZN4KutaoGLooWJTB86kLyF55uy",
        "https://play-lh.googleusercontent.com/xR_gxXoYDfTahDoexvHc0MB6kkydLLMSlUzshH2xu_5IsnFefSIgi5Bn4yOdgCNpRuWL"
    ],
    "contentRating": "Everyone",
    "adSupported": false,
    "containsAds": false,
    "released": "Feb 7, 2012",
    "updated": 1733436114,
    "version": "Varies with device",
    "appId": "com.android.chrome",
    "url": "https://play.google.com/store/apps/details?id=com.android.chrome&hl=en&gl=us"
}
```

### `GooglePlayCategory`

Represents app categories with:

- `name`: Category name.
- `id`: Category ID.

## 🔧 Configuration

### Logging

The library includes a logger for debugging. You can customize or disable logging by modifying the `logger` instance in `NetworkUtils`.

### Network Configuration

The library uses Ktor for network operations. Ensure your project includes the required dependencies for Ktor and that network permissions are properly configured for Android.

## 📚 Contributions

Contributions to the library are welcome. If you find a bug or want to request a feature, feel free to open an issue or submit a pull request on GitHub.

## 📄 License

This library is licensed under the MIT License. See the LICENSE file for details.

## 🚫 Disclaimer

This library is intended for educational and personal use. Scraping data from Google Play Store may violate its terms of service. Use responsibly and at your own risk.

