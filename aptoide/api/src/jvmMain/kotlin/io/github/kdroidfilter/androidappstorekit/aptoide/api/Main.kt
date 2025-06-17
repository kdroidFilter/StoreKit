package io.github.kdroidfilter.androidappstorekit.aptoide.api

import io.github.kdroidfilter.androidappstorekit.aptoide.api.services.AptoideService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

fun main() {
    println("Testing Aptoide API client...")
    
    val aptoideService = AptoideService()
    
    runBlocking {
        try {
            // Fetch app metadata for Aptoide app
            val appInfo = aptoideService.getAppMetaByPackageName("com.google.android.gms", "en")
            val json = Json { prettyPrint = true }.encodeToString(appInfo)
            println("$json")
            
        } catch (e: Exception) {
            println("Error fetching app metadata: ${e.message}")
            e.printStackTrace()
        }
    }
}