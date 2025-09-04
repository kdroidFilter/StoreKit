package io.github.kdroidfilter.storekit.sample

import io.github.kdroidfilter.storekit.gplay.scrapper.services.getGooglePlayApplicationInfo
import io.github.kdroidfilter.storekit.apklinkresolver.core.service.ApkSourcePriority
import io.github.kdroidfilter.storekit.apklinkresolver.core.service.ApkLinkResolverService
import io.github.kdroidfilter.storekit.apklinkresolver.core.service.ApkSource

import io.github.kdroidfilter.storekit.aptoide.api.services.AptoideService
import io.github.kdroidfilter.storekit.fdroid.api.services.FDroidService
import io.github.kdroidfilter.storekit.apkpure.scraper.services.getApkPureApplicationInfo
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

/**
 * Sample application that creates example instances of Aptoide, F-Droid, Google Play, APKPure, and APK Downloader models
 * and prints them as JSON.
 */
fun main() {

    ApkSourcePriority.setPriorityOrder(
        listOf(
            ApkSource.APKPURE,
            ApkSource.APKCOMBO,
            ApkSource.FDROID,
            ApkSource.APTOIDE,
        )
    )

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
        val fdroidPackage = fdroidService.getPackageInfo("net.thunderbird.android")

        // Print the F-Droid example as JSON
        println("=== F-Droid Example ===")
        println(json.encodeToString(fdroidPackage))
        println()

        // APKPure example
        try {
            val apkpureApp = getApkPureApplicationInfo("com.citycar.flutter")
            println("=== APKPure Example ===")
            println(json.encodeToString(apkpureApp))
            println()
        } catch (e: Exception) {
            println("Error retrieving APKPure info: ${e.message}")
        }

        // APK Downloader example
        println("=== APK Downloader Example ===")

        // Create an instance of the APK Downloader service
        val apkLinkResolverService = ApkLinkResolverService()

        try {
            // Get download link for a package using the custom priority
            val downloadInfo = apkLinkResolverService.getApkDownloadLink("com.unicell.pangoandroid")

            println("Download info for com.apple.bnd:")
            println(json.encodeToString(downloadInfo))
            println()
            println("Source: ${downloadInfo.source}")
            println("Title: ${downloadInfo.title}")
            println("Version: ${downloadInfo.version}")
            println("Version Code: ${downloadInfo.versionCode}")
            println("Download Link: ${downloadInfo.downloadLink}")
            println("File Size: ${downloadInfo.fileSize} bytes")
        } catch (e: Exception) {
            println("Error retrieving download link: ${e.message}")
        } finally {
            // Reset to default priority
            ApkSourcePriority.resetToDefault()
        }
    }
}
