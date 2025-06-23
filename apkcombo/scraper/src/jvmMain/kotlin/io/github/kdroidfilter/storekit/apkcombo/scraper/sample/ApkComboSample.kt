package io.github.kdroidfilter.storekit.apkcombo.scraper.sample

import io.github.kdroidfilter.storekit.apkcombo.scraper.services.getApkComboApplicationInfo
import kotlinx.coroutines.runBlocking

/**
 * Sample application demonstrating the usage of the APKCombo scraper.
 */
fun main() = runBlocking {
    try {
        // Example package name
        val packageName = "com.github.android"
        
        // Fetch app information from APKCombo
        val appInfo = getApkComboApplicationInfo(packageName)
        
        // Display the results
        println("App ID: ${appInfo.appId}")
        println("Version: ${appInfo.version}")
        println("Version Code: ${appInfo.versionCode}")
        println("Download Link: ${appInfo.downloadLink}")
        println("URL: ${appInfo.url}")
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}