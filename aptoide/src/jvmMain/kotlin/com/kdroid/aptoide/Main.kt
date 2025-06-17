package com.kdroid.aptoide

import com.kdroid.aptoide.services.AptoideService
import kotlinx.coroutines.runBlocking

fun main() {
    println("Testing Aptoide API client...")
    
    val aptoideService = AptoideService()
    
    runBlocking {
        try {
            // Fetch app metadata for Aptoide app
            val appInfo = aptoideService.getAppMetaByPackageName("com.waze", "fr")
            
            println("App metadata: $appInfo")
            
        } catch (e: Exception) {
            println("Error fetching app metadata: ${e.message}")
            e.printStackTrace()
        }
    }
}