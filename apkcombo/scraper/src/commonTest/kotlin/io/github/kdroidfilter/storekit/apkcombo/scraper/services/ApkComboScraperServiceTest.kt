package io.github.kdroidfilter.storekit.apkcombo.scraper.services

import io.github.kdroidfilter.storekit.apkcombo.core.model.ApkComboApplicationInfo
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class ApkComboScraperServiceTest {

    @Test
    fun testGetApkComboApplicationInfo_ValidPackage() = runBlocking {
        // Given
        val packageName = "net.calj.android" // Using the same example as in the sample

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
}
