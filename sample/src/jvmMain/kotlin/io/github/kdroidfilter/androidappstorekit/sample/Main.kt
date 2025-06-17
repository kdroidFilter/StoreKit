package io.github.kdroidfilter.androidappstorekit.sample

import io.github.kdroidfilter.androidappstorekit.aptoide.api.services.AptoideService
import io.github.kdroidfilter.androidappstorekit.gplay.scrapper.services.getGooglePlayApplicationInfo
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Sample application demonstrating the usage of AndroidAppStoreKit library
 * to fetch app information from both Google Play Store and Aptoide Store.
 */
fun main() {
    val logger = KotlinLogging.logger {}
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    logger.info { "Starting AndroidAppStoreKit Sample Application" }
    
    runBlocking {
        try {
            // Example 1: Fetch app information from Google Play Store
            logger.info { "Example 1: Fetching app information from Google Play Store" }
            val googlePlayAppId = "com.android.chrome"
            logger.info { "Fetching information for app: $googlePlayAppId" }
            
            val googlePlayAppInfo = getGooglePlayApplicationInfo(googlePlayAppId)
            logger.info { "Successfully fetched information for: ${googlePlayAppInfo.title}" }
            logger.info { "App details:\n${json.encodeToString(googlePlayAppInfo)}" }
            
            // Example 2: Fetch app information from Aptoide Store
            logger.info { "Example 2: Fetching app information from Aptoide Store" }
            val aptoideService = AptoideService()
            val aptoideAppPackageName = "com.waze"
            logger.info { "Fetching information for app: $aptoideAppPackageName" }
            
            val aptoideAppInfo = aptoideService.getAppMetaByPackageName(aptoideAppPackageName)
            logger.info { "Successfully fetched information for: ${aptoideAppInfo.name}" }
            logger.info { "App details:\n${json.encodeToString(aptoideAppInfo)}" }
            
            // Print summary of both apps
            logger.info { "Summary:" }
            logger.info { "Google Play - ${googlePlayAppInfo.title}:" }
            logger.info { "  - Developer: ${googlePlayAppInfo.developer}" }
            logger.info { "  - Rating: ${googlePlayAppInfo.score} (${googlePlayAppInfo.ratings} ratings)" }
            logger.info { "  - Installs: ${googlePlayAppInfo.installs}" }
            
            logger.info { "Aptoide - ${aptoideAppInfo.name}:" }
            logger.info { "  - Developer: ${aptoideAppInfo.developer.name}" }
            logger.info { "  - Rating: ${aptoideAppInfo.stats.rating.avg} (${aptoideAppInfo.stats.rating.total} ratings)" }
            logger.info { "  - Downloads: ${aptoideAppInfo.stats.downloads}" }
            
        } catch (e: Exception) {
            logger.error(e) { "Error occurred while fetching app information" }
        }
    }
    
    logger.info { "AndroidAppStoreKit Sample Application completed" }
}