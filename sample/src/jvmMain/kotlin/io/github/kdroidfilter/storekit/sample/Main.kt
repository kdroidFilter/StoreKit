package io.github.kdroidfilter.storekit.sample

import io.github.kdroidfilter.androidappstorekit.gplay.scrapper.services.getGooglePlayApplicationInfo
import io.github.kdroidfilter.storekit.aptoide.api.services.AptoideService
import io.github.kdroidfilter.storekit.aptoide.core.model.*
import io.github.kdroidfilter.storekit.fdroid.api.services.FDroidService
import io.github.kdroidfilter.storekit.fdroid.core.model.*
import io.github.kdroidfilter.storekit.gplay.core.model.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Sample application that creates example instances of Aptoide, F-Droid, and Google Play models
 * and prints them as JSON.
 */
fun main() {
    // Create a pretty-printed JSON formatter
    val json = Json { 
        prettyPrint = true 
        encodeDefaults = true
    }
    runBlocking {

        // Create an example Google Play application info
        val gplayApp = getGooglePlayApplicationInfo("com.waze")

        val aptoideService = AptoideService()

        // Create an example Aptoide application info
        val aptoideApp = aptoideService.getAppMetaByPackageName("com.waze")

        // Print the Google Play example as JSON
        println("=== Google Play Example ===")
        println(json.encodeToString(gplayApp))
        println()

        // Print the Aptoide example as JSON
        println("=== Aptoide Example ===")
        println(json.encodeToString(aptoideApp))
        println()

        val fdroidService = FDroidService()

        // Create an example F-Droid package info
        val fdroidPackage = fdroidService.getPackageInfo("org.fdroid.fdroid")

        // Print the F-Droid example as JSON
        println("=== F-Droid Example ===")
        println(json.encodeToString(fdroidPackage))
    }
}
