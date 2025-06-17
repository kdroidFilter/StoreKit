# 💡 GPlay Scrapper Library

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kdroidfilter/gplayscrapper.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.kdroidfilter%22%20AND%20a:%22gplayscrapper%22)

## 📑 Table of Contents
- [Introduction](#-introduction)
- [Features](#-features)
- [Installation](#-installation)
- [Usage](#-usage)
- [Data Models](#-data-models)
- [Configuration](#-configuration)
- [Contributions](#-contributions)
- [License](#-license)
- [Disclaimer](#-disclaimer)

## 🔄 Introduction

This library is a full Kotlin adaptation of the [google-play-scraper](https://github.com/JoMingyu/google-play-scraper) python library.

The GPlay Scrapper Library provides a comprehensive way to extract detailed information about applications listed on the Google Play Store. With this library, you can fetch details such as application descriptions, developer information, categories, reviews, and more.

This library is built using Kotlin and leverages kotlinx.serialization for parsing JSON data and Ktor for HTTP requests.

## 📊 Features

- Fetch application details including title, description, developer info, price, etc.
- Extract application categories and genres.
- Retrieve app ratings, reviews, and download counts.
- Identify in-app purchase details and sale statuses.
- Collect metadata like screenshots, icons, and videos.

## 📦 Installation

This library is available on Maven Central. Add the following dependency to your project:

### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:gplayscrapper:<version>")
}
```

### Gradle Groovy DSL

```groovy
dependencies {
    implementation 'io.github.kdroidfilter:gplayscrapper:<version>'
}
```

### Using Only the Core Module

If you only need the model classes (like `GooglePlayApplicationInfo` and `GooglePlayCategory`) without the scraping functionality, you can use just the core module:

#### Gradle Kotlin DSL

```kotlin
dependencies {
    implementation("io.github.kdroidfilter:gplayscrapper-core:<version>")
}
```

#### Gradle Groovy DSL

```groovy
dependencies {
    implementation 'io.github.kdroidfilter:gplayscrapper-core:<version>'
}
```

This is useful when you only need the data models in your application, for example when you're building a client that consumes data from a server that already uses the full library.


## 🔄 Usage

### Fetching App Information

To fetch detailed information about a specific app, you can use the `getGooglePlayApplicationInfo` function. This function requires the app ID as a parameter and optionally takes the language and country codes. If the app is not found or accessible, it throws an `IllegalArgumentException`.

#### Parameters:

- `appId: String` (mandatory): The unique identifier of the app.
- The country and language codes that can be included in the `lang` and `country` parameters described below depend on the ISO 3166 and ISO 639-1 standards, respectively.
- `lang: String` (optional, default = "en"): Language code for the app details.
- `country: String` (optional, default = "us"): Country code for the app details.

#### Example:

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
### Using the Library in Java

The library also supports Java through the `getGooglePlayApplicationInfoAsync` function, which returns a `CompletableFuture`.

#### Example:

```java

public class Main {
    public static void main(String[] args) {
        String appId = "com.android.chrome";

        // Call Kotlin function with parameters
        CompletableFuture<GooglePlayApplicationInfo> appInfoFuture = getGooglePlayApplicationInfoAsync("com.android.chrome", "en", "us");

        appInfoFuture.thenAccept(appInfo -> {
            System.out.println("App Info: " + appInfo.getTitle());
        }).exceptionally(throwable -> {
            System.err.println("Error : " + throwable.getMessage());
            return null;
        });
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
    "description": "Google Chrome is a fast, easy to use, and secure web browser. Designed for Android, Chrome brings you personalized news articles, quick links to your favorite sites, downloads, and Google Search and Google Translate built-in. Download now to enjoy the same Chrome web browser experience you love across all your devices.\n        \n        <b>Browse fast and type less.</b> Choose from personalized search results that instantly appear as you type and quickly browse previously visited web pages. Fill in forms quickly with Autofill.\n        \n        <b>Incognito Browsing.</b> Use Incognito mode to browse the internet without saving your history. Browse privately across all your devices.\n        \n        <b>Access your Chrome across devices.</b> When you sign in to Chrome, you can save bookmarks, passwords and more in your Google Account, so you can access them on your other devices.\n        \n        <b>All your favorite content, one tap away.</b> Chrome is not just fast for Google Search, but designed so you are one tap away from all your favorite content. You can tap on your favorite news sites or social media directly from the new tab page. Chrome also has the “Tap to Search”- feature on most webpages. You can tap on any word or phrase to start a Google search while still in the page you are enjoying.\n        \n        <b>Protect your phone with Google Safe Browsing.</b> Chrome has Google Safe Browsing built-in. It keeps your phone safe by showing warnings to you when you attempt to navigate to dangerous sites or download dangerous files.\n        \n        <b>Fast downloads and view web pages and videos offline</b> Chrome has a dedicated download button, so you can easily download videos, pictures, and entire webpages with just one tap. Chrome also has downloads home right inside Chrome, where you can access all the content you downloaded, even when you are offline.\n        \n        <b>Google Voice Search.</b> Chrome gives you an actual web browser you can talk to. Use your voice to find answers on-the-go without typing and go hands free. You can browse and navigate quicker using your voice anywhere, anytime.\n        \n        <b>Google Translate built-in: Quickly translate entire web pages.</b> Chrome has Google Translate built in to help you to translate entire web to your own language with one tap.\n                \n        <b>Smart personalized recommendations.</b> Chrome creates an experience that is tailored to your interests.  On the new tab page, you will find articles that Chrome selected based on your previous browsing history.",
    "descriptionHTML": "Google Chrome is a fast, easy to use, and secure web browser. Designed for Android, Chrome brings you personalized news articles, quick links to your favorite sites, downloads, and Google Search and Google Translate built-in. Download now to enjoy the same Chrome web browser experience you love across all your devices.<br>        <br>        <b>Browse fast and type less.</b> Choose from personalized search results that instantly appear as you type and quickly browse previously visited web pages. Fill in forms quickly with Autofill.<br>        <br>        <b>Incognito Browsing.</b> Use Incognito mode to browse the internet without saving your history. Browse privately across all your devices.<br>        <br>        <b>Access your Chrome across devices.</b> When you sign in to Chrome, you can save bookmarks, passwords and more in your Google Account, so you can access them on your other devices.<br>        <br>        <b>All your favorite content, one tap away.</b> Chrome is not just fast for Google Search, but designed so you are one tap away from all your favorite content. You can tap on your favorite news sites or social media directly from the new tab page. Chrome also has the “Tap to Search”- feature on most webpages. You can tap on any word or phrase to start a Google search while still in the page you are enjoying.<br>        <br>        <b>Protect your phone with Google Safe Browsing.</b> Chrome has Google Safe Browsing built-in. It keeps your phone safe by showing warnings to you when you attempt to navigate to dangerous sites or download dangerous files.<br>        <br>        <b>Fast downloads and view web pages and videos offline</b> Chrome has a dedicated download button, so you can easily download videos, pictures, and entire webpages with just one tap. Chrome also has downloads home right inside Chrome, where you can access all the content you downloaded, even when you are offline.<br>        <br>        <b>Google Voice Search.</b> Chrome gives you an actual web browser you can talk to. Use your voice to find answers on-the-go without typing and go hands free. You can browse and navigate quicker using your voice anywhere, anytime.<br>        <br>        <b>Google Translate built-in: Quickly translate entire web pages.</b> Chrome has Google Translate built in to help you to translate entire web to your own language with one tap.<br>                <br>        <b>Smart personalized recommendations.</b> Chrome creates an experience that is tailored to your interests.  On the new tab page, you will find articles that Chrome selected based on your previous browsing history.",
    "summary": "Google's fast and secure browser",
    "installs": "10,000,000,000+",
    "minInstalls": 10000000000,
    "realInstalls": 16985273589,
    "score": 4.065567,
    "ratings": 46344164,
    "reviews": 992885,
    "histogram": [
        5920664,
        2376727,
        3450944,
        5590747,
        29005054
    ],
    "price": 0.0,
    "free": true,
    "currency": "USD",
    "sale": false,
    "saleTime": null,
    "originalPrice": null,
    "saleText": null,
    "offersIAP": false,
    "inAppProductPrice": "",
    "developer": "Google LLC",
    "developerId": "5700313618786177705",
    "developerEmail": "apps-help@google.com",
    "developerWebsite": "http://www.google.com/chrome/android",
    "developerAddress": "1600 Amphitheatre Parkway, Mountain View 94043",
    "privacyPolicy": "https://policies.google.com/privacy",
    "genre": "Communication",
    "genreId": "COMMUNICATION",
    "categories": [
        {
            "name": "Communication",
            "id": "COMMUNICATION"
        }
    ],
    "icon": "https://play-lh.googleusercontent.com/QRRGW2tMZ4-FNw0XWk6WWiXHaQCGxuwM-92HrBhlA4WOd_AGmjVmQkiHyAqQjW2yByc",
    "headerImage": "https://play-lh.googleusercontent.com/yZPtmF--wo_V78QRe00YWjnHBnTISZZJeSo4WbSsJpFBRI1MNsv6sN1UxRBIK6mbtQ",
    "screenshots": [
        "https://play-lh.googleusercontent.com/OMmJGYNXlDfnWFX8iS1Veq2hqYJAkp0KF4kjPMEBEkZN4KutaoGLooWJTB86kLyF55uy",
        "https://play-lh.googleusercontent.com/xR_gxXoYDfTahDoexvHc0MB6kkydLLMSlUzshH2xu_5IsnFefSIgi5Bn4yOdgCNpRuWL",
        "https://play-lh.googleusercontent.com/7_ksUyBu73YwRTelEGXzZeqWMNQOyXYWVzIwvWW0-Xyu4g5sOnhMdc4g-Y_u37z2ewI",
        "https://play-lh.googleusercontent.com/_UizlCmKyFhS138Qeh3XXWvwatHa2j9cdAiFSN4FVU7wYP0qaF-Ci3_K-ZkAvhXx3Q",
        "https://play-lh.googleusercontent.com/X_pt3f62938fqokgs44Ga1t8zX0kC0MPldi0qWKU95ivpTUPfawy9hxE5F9RGnf1Uko",
        "https://play-lh.googleusercontent.com/BZwVMcpIt1gZZGe1X5gjKBdYmLrzgSD_cwKlKmJib8BV0lHPdlHly-9oEGm11VTr_iQ",
        "https://play-lh.googleusercontent.com/yBJ1sGeM_eO8YfX-lqrNfPYIaswaC-dCw1csrZOyD6ITG7xfMqUq3nmgGNe_Hv1E4w",
        "https://play-lh.googleusercontent.com/X9aG9oNn9zZbI36AVXnDqzuRQTcogbZKPLbLyWkbmQQy2j0CRizPZJX4Xz935USY-A8",
        "https://play-lh.googleusercontent.com/KeG1x0bwnrev-FJbTjUBsENlNX87a_4vstaz5B7yuDXsrHivKfaQ87fhtNFKD0EjvR1l",
        "https://play-lh.googleusercontent.com/CnkWLDYN25mQm_P1kZLcyKiZxnifwIEIg8KwrthG60dtjxeU8wzhCXazxw_yeslNMysD",
        "https://play-lh.googleusercontent.com/M9ue_W7CxgXh9Po-9Caqmo6kzsNHucTZWp1zFf2exER8bo3TCNGXqMB_4otOyeocAJc",
        "https://play-lh.googleusercontent.com/be88TX-_Gr--Wr6nhpTCU8RElea_WmWHVeQ3eF-90yongEe7hls4kI__QYeBSvE_0nA",
        "https://play-lh.googleusercontent.com/xThFpJIuS_l-MrbK58eExZyJJRFxdCMNNomV9ZG2icN6aLtpjAWMAuYoLKG6FCB98zM",
        "https://play-lh.googleusercontent.com/A5qQ08pUxADMCi3dupQ05glOPtpiGFw9KJK3NmyY2jJ3HomdaKr4ht2St8sQRqlCqRCs",
        "https://play-lh.googleusercontent.com/9jgY1qGCai_9IlPGWGswh6_zD7lzdy_vdm8gYejPHOSVTR1kdFaiJrblwr_z_CaKWjC_",
        "https://play-lh.googleusercontent.com/krpPF6UrPaNVRLqSWTsU5eHOJjCkftn9BVTyxv_UZPDjBw0rKyyDQfMrewcFjF6L2h0E",
        "https://play-lh.googleusercontent.com/tpGGpOkZBtjHC_yf369GU3KbGT4rCvElpaI3R48pimj4T0ryrPdwhbOFrLPtQ3kXOKd9",
        "https://play-lh.googleusercontent.com/HSJ2yPrJxQzJoaKXENj5heR_71PTHBMceoxgMZzVXcV58Y-lD-ol82cb7-tFUAYA2kY",
        "https://play-lh.googleusercontent.com/ltw5iMf8UCQow68CRmLWk4QtJjrdfyHUoVMqFDZ1iBCJGJfvHow40_aGontjAC3fdg",
        "https://play-lh.googleusercontent.com/-x_v44rm5o71w-uD8UuwuRCSNZ10OWqYqgj6wsKi0JkrEJwOlbTBSoJRvA3aCN6XYd4",
        "https://play-lh.googleusercontent.com/WBDmwgh8MR4Cxyc9x5hkoRk--V23PFJDyE_eMdjAN039prqGgF9FEvl22beskx7pUler",
        "https://play-lh.googleusercontent.com/dPVss_a3Z8gHV_TPicWNaNdY1brRePVQaBu2NQJoNrxeJ8RnhkWS4v0n_bSJGST1vA",
        "https://play-lh.googleusercontent.com/xb295JCLDkRu1qlkhF4qiddCd4s5WlRnHXthUKQw2Tz_acYGdiqj6rxOo1tFQo59hQ",
        "https://play-lh.googleusercontent.com/sJePeYjSItM3jOhcKrX9oW5Yot_7z6DnuJTSowlsbol10tS2gygZKGhM615EB_L4VA",
        "https://play-lh.googleusercontent.com/w-_cjSD79Cbqf6IjYhxOOS11wHmZeKOtegM5cbKbBT7D0AHq-6DqRoqfKKVJG_WwWQ",
        "https://play-lh.googleusercontent.com/fKYTjKsPHtBOgcGSFq_FdGCAuKZRY1UJysFO8SW2qN-keySJxTInTNOE5ebtOZ8rvw",
        "https://play-lh.googleusercontent.com/bpyl4wtc00BxVcj-5UlkECqM7GyAFg6FWUYhToDGhqAuRt7d3d6Qi69mvC2d_kmrqAHP",
        "https://play-lh.googleusercontent.com/zR7XKGrFBiTd2__1DvE7He1y2l33E4Tg-qHxfD8VKEvLCBgL4rbpadR7U5fELXb4rg",
        "https://play-lh.googleusercontent.com/NHOqWWhRsfaUx0YF5R90oMv06dlcqKZuuMAvugw6vTvSy3qy-lRrDZQSJPlMj4Ou4J8",
        "https://play-lh.googleusercontent.com/bRrZSOI9y2BkFvnVaRjK3Jzae26pwvdlNnBDrKCIUxCdD-uUu09HIMpwVad1TrZVKLQ",
        "https://play-lh.googleusercontent.com/bVRRznZy7d05YJJCVu0lVEkeoslyGnic1mgfSXOI2nRBSzz1WJ2Qhjg-ArEFfzveMdkk",
        "https://play-lh.googleusercontent.com/GVPTDqFYafYxIO9q_sdJHwGv0g4Vv0D2TPkjS9wroQ2elHct8mV1Rl_IWniqz2lYV1np"
    ],
    "video": "",
    "videoImage": "",
    "contentRating": "Everyone",
    "contentRatingDescription": "",
    "adSupported": false,
    "containsAds": false,
    "released": "Feb 7, 2012",
    "updated": 1733436114,
    "version": "Varies with device",
    "comments": [],
    "appId": "com.android.chrome",
    "url": "https://play.google.com/store/apps/details?id=com.android.chrome&hl=en&gl=us"
}
```

### `GooglePlayCategory`

Represents app categories with:

- `name`: Category name.
- `id`: Category ID.

## 💧 Configuration

### Logging

The library includes a logger for debugging. You can customize or disable logging by modifying the `logger` instance in `NetworkUtils`.

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

This library is intended for educational and personal use. Scraping data from Google Play Store may violate its terms of service. Use responsibly and at your own risk.
