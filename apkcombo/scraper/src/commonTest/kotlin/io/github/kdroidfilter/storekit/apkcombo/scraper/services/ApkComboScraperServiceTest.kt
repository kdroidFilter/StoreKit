package io.github.kdroidfilter.storekit.apkcombo.scraper.services

import io.github.kdroidfilter.storekit.apkcombo.core.model.ApkComboApplicationInfo
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import kotlin.test.fail

class ApkComboScraperServiceTest {

    @Test
    fun testGetApkComboApplicationInfo_ValidPackage() = runBlocking {
        // Given
        val packageName = "com.google.android.gm"

        try {
            // When
            val appInfo: ApkComboApplicationInfo = getApkComboApplicationInfo(packageName)

            // Then
            assertNotNull(appInfo, "App info should not be null")
            assertEquals(packageName, appInfo.appId, "App ID should match the package name")
            assertNotEquals("", appInfo.title, "Title should not be empty")
            assertNotEquals("", appInfo.version, "Version should not be empty")
            assertNotEquals("", appInfo.downloadLink, "Download link should not be empty")
            assertTrue(appInfo.url.contains(packageName), "URL should contain the package name")

            println("[DEBUG_LOG] Retrieved app info for $packageName:")
            println("[DEBUG_LOG] Title: ${appInfo.title}")
            println("[DEBUG_LOG] Version: ${appInfo.version}")
            println("[DEBUG_LOG] Version Code: ${appInfo.versionCode}")
            println("[DEBUG_LOG] Download Link: ${appInfo.downloadLink}")
            println("[DEBUG_LOG] URL: ${appInfo.url}")
        } catch (e: Exception) {
            println("[DEBUG_LOG] Error retrieving app info: ${e.message}")
            throw e
        }
    }

    @Test
    fun testGetApkComboApplicationInfo_InvalidPackage() = runBlocking {
        // Given
        val invalidPackageName = "com.invalid.package.that.does.not.exist"

        // When/Then
        val exception = assertFailsWith<IllegalArgumentException> {
            getApkComboApplicationInfo(invalidPackageName)
        }

        println("[DEBUG_LOG] Expected exception message: ${exception.message}")
        assertTrue(exception.message?.contains(invalidPackageName) ?: false, 
            "Exception message should contain the invalid package name")
    }

    @Test
    fun testGetApkComboApplicationInfo_PopularApp() = runBlocking {
        // Given
        val packageName = "com.facebook.katana" // Facebook is a popular app that should be available

        try {
            // When
            val appInfo: ApkComboApplicationInfo = getApkComboApplicationInfo(packageName)

            // Then
            assertNotNull(appInfo, "App info should not be null")
            assertEquals(packageName, appInfo.appId, "App ID should match the package name")
            assertTrue(appInfo.title.contains("Facebook"), "Title should contain 'Facebook'")
            assertNotEquals("", appInfo.version, "Version should not be empty")
            assertNotEquals("", appInfo.downloadLink, "Download link should not be empty")

            println("[DEBUG_LOG] Retrieved app info for Facebook:")
            println("[DEBUG_LOG] Title: ${appInfo.title}")
            println("[DEBUG_LOG] Version: ${appInfo.version}")
            println("[DEBUG_LOG] Version Code: ${appInfo.versionCode}")
            println("[DEBUG_LOG] Download Link: ${appInfo.downloadLink}")
            println("[DEBUG_LOG] URL: ${appInfo.url}")
        } catch (e: Exception) {
            println("[DEBUG_LOG] Error retrieving app info: ${e.message}")
            throw e
        }
    }

    @Test
    fun testGetApkComboApplicationInfo_MultiplePackages() = runBlocking {
        // Given
        val validPackages = listOf(
            "com.google.android.gm",          // Gmail
            "com.facebook.katana",            // Facebook
            "com.spotify.music",               // Spotify
            "fm.jewishmusic.application"
        )

        // These packages might not be available or might return errors
        val potentiallyUnavailablePackages = listOf(
            "com.whatsapp"                    // WhatsApp - might be unavailable (410 Gone)
        )

        val invalidPackages = listOf(
            "com.invalid.package.that.does.not.exist",
            "com.another.invalid.package.123456"
        )

        // Test valid packages
        println("[DEBUG_LOG] Testing ${validPackages.size} valid packages")
        for (packageName in validPackages) {
            try {
                // When
                val appInfo: ApkComboApplicationInfo = getApkComboApplicationInfo(packageName)

                // Then
                assertNotNull(appInfo, "App info should not be null for $packageName")
                assertEquals(packageName, appInfo.appId, "App ID should match the package name for $packageName")
                assertNotEquals("", appInfo.title, "Title should not be empty for $packageName")
                assertNotEquals("", appInfo.version, "Version should not be empty for $packageName")
                assertNotEquals("", appInfo.downloadLink, "Download link should not be empty for $packageName")

                println("[DEBUG_LOG] Successfully retrieved app info for $packageName:")
                println("[DEBUG_LOG] Title: ${appInfo.title}")
                println("[DEBUG_LOG] Version: ${appInfo.version}")
                println("[DEBUG_LOG] Version Code: ${appInfo.versionCode}")
            } catch (e: Exception) {
                println("[DEBUG_LOG] Error retrieving app info for $packageName: ${e.message}")
                fail("Failed to retrieve app info for valid package $packageName: ${e.message}")
            }
        }

        // Test potentially unavailable packages
        if (potentiallyUnavailablePackages.isNotEmpty()) {
            println("[DEBUG_LOG] Testing ${potentiallyUnavailablePackages.size} potentially unavailable packages")
            for (packageName in potentiallyUnavailablePackages) {
                try {
                    // When
                    val appInfo: ApkComboApplicationInfo = getApkComboApplicationInfo(packageName)

                    // Then - if we get here, the package was available
                    assertNotNull(appInfo, "App info should not be null for $packageName")
                    assertEquals(packageName, appInfo.appId, "App ID should match the package name for $packageName")
                    assertNotEquals("", appInfo.title, "Title should not be empty for $packageName")

                    println("[DEBUG_LOG] Successfully retrieved app info for potentially unavailable package $packageName:")
                    println("[DEBUG_LOG] Title: ${appInfo.title}")
                    println("[DEBUG_LOG] Version: ${appInfo.version}")
                    println("[DEBUG_LOG] Version Code: ${appInfo.versionCode}")
                } catch (e: Exception) {
                    // Log the error but don't fail the test
                    println("[DEBUG_LOG] Expected error for potentially unavailable package $packageName: ${e.message}")
                }
            }
        }

        // Test invalid packages
        println("[DEBUG_LOG] Testing ${invalidPackages.size} invalid packages")
        for (invalidPackageName in invalidPackages) {
            try {
                getApkComboApplicationInfo(invalidPackageName)
                fail("Expected exception for invalid package $invalidPackageName, but none was thrown")
            } catch (e: IllegalArgumentException) {
                // Expected exception
                println("[DEBUG_LOG] Expected exception for $invalidPackageName: ${e.message}")
                assertTrue(e.message?.contains(invalidPackageName) ?: false, 
                    "Exception message should contain the invalid package name")
            } catch (e: Exception) {
                println("[DEBUG_LOG] Unexpected exception type for $invalidPackageName: ${e::class.simpleName} - ${e.message}")
                fail("Expected IllegalArgumentException for invalid package $invalidPackageName, but got ${e::class.simpleName}")
            }
        }
    }
}
