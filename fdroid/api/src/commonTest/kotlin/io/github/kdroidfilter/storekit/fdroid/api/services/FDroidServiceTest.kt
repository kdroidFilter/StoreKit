package io.github.kdroidfilter.storekit.fdroid.api.services

import io.github.kdroidfilter.storekit.fdroid.core.model.FDroidPackageInfo
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FDroidServiceTest {

    @Test
    fun testRetrievePackageInfo() = runBlocking {
        // Given
        val fdroidService = FDroidService()
        val packageName = "org.fdroid.fdroid" // Using F-Droid app itself as an example
        
        try {
            // When
            val packageInfo: FDroidPackageInfo = fdroidService.getPackageInfo(packageName)
            
            // Then
            assertNotNull(packageInfo, "Package info should not be null")
            assertEquals(packageName, packageInfo.packageName, "Package name should match the requested one")
            assertTrue(packageInfo.suggestedVersionCode > 0, "Suggested version code should be positive")
            
            // Verify packages list
            assertNotNull(packageInfo.packages, "Packages list should not be null")
            assertTrue(packageInfo.packages.isNotEmpty(), "Packages list should not be empty")
            
            // Verify at least one package has valid version info
            val firstPackage = packageInfo.packages.firstOrNull()
            assertNotNull(firstPackage, "First package should not be null")
            assertTrue(firstPackage.versionName.isNotEmpty(), "Version name should not be empty")
            assertTrue(firstPackage.versionCode > 0, "Version code should be positive")
            
            // Debug logging
            println("[DEBUG_LOG] Retrieved package info for: ${packageInfo.packageName}")
            println("[DEBUG_LOG] Suggested version code: ${packageInfo.suggestedVersionCode}")
            println("[DEBUG_LOG] Number of packages: ${packageInfo.packages.size}")
            println("[DEBUG_LOG] First package version name: ${firstPackage.versionName}")
            println("[DEBUG_LOG] First package version code: ${firstPackage.versionCode}")
            
        } catch (e: Exception) {
            println("[DEBUG_LOG] Error retrieving package info: ${e.message}")
            throw e
        }
    }
}