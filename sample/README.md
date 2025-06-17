# AndroidAppStoreKit Sample Module

This module provides a sample application demonstrating how to use the AndroidAppStoreKit library to fetch app information from both Google Play Store and Aptoide Store.

## Features

- Fetches app information from Google Play Store using the scraper service
- Fetches app information from Aptoide Store using the API service
- Displays detailed information about apps from both stores
- Provides a comparison of key metrics (developer, rating, installs/downloads)

## How to Run

You can run the sample application using Gradle:

```bash
./gradlew :sample:run
```

## Code Example

The sample application demonstrates:

1. How to initialize and use the Google Play scraper service:
```kotlin
val googlePlayAppInfo = getGooglePlayApplicationInfo("com.android.chrome")
```

2. How to initialize and use the Aptoide API service:
```kotlin
val aptoideService = AptoideService()
val aptoideAppInfo = aptoideService.getAppMetaByPackageName("com.waze")
```

3. How to access and display app information:
```kotlin
// Google Play app info
println("Title: ${googlePlayAppInfo.title}")
println("Developer: ${googlePlayAppInfo.developer}")
println("Rating: ${googlePlayAppInfo.score}")

// Aptoide app info
println("Name: ${aptoideAppInfo.name}")
println("Developer: ${aptoideAppInfo.developer.name}")
println("Rating: ${aptoideAppInfo.stats.rating.avg}")
```

## Dependencies

The sample module depends on:
- Google Play modules (scrapper and core)
- Aptoide modules (api and core)
- Ktor client libraries for HTTP requests
- Kotlinx libraries for coroutines and serialization
- Logging libraries

## Notes

This sample application is for demonstration purposes only. When using the library in a production environment, consider implementing proper error handling, caching, and rate limiting to avoid overloading the services.