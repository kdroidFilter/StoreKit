package io.github.kdroidfilter.storekit.authenticity

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for the InstallationSourceDetector class.
 */
@RunWith(AndroidJUnit4::class)
class InstallationSourceDetectorTest {

    /**
     * Test detecting the installation source of an installed app.
     * This test assumes that the Android system package is installed on the device.
     */
    @Test
    fun testDetectInstallationSourceForSystemApp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageName = "android" // Android system package should always be installed

        val installationInfo = InstallationSourceDetector.detectInstallationSource(context, packageName)

        // The Android system package should be detected as a system app
        assertTrue("Android system package should be a system app", installationInfo.isSystemApp)
        println("[DEBUG_LOG] Android system package installation info: $installationInfo")
    }

    /**
     * Test detecting the installation source of a non-existent app.
     */
    @Test
    fun testDetectInstallationSourceForNonExistentApp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageName = "com.nonexistent.app.that.does.not.exist"

        val installationInfo = InstallationSourceDetector.detectInstallationSource(context, packageName)

        // A non-existent package should be detected as sideloaded
        assertEquals("Non-existent package should be detected as sideloaded", InstallationSource.SIDELOADED, installationInfo.source)
        assertFalse("Non-existent package should not be a system app", installationInfo.isSystemApp)
        println("[DEBUG_LOG] Non-existent package installation info: $installationInfo")
    }

    /**
     * Test the isSystemApp method with a system app.
     */
    @Test
    fun testIsSystemAppWithSystemApp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageName = "android" // Android system package should always be installed

        val isSystemApp = InstallationSourceDetector.isSystemApp(context, packageName)

        // The Android system package should be a system app
        assertTrue("Android system package should be a system app", isSystemApp)
        println("[DEBUG_LOG] Android system package is system app: $isSystemApp")
    }

    /**
     * Test the isSystemApp method with a non-system app.
     * This test tries to find a non-system app on the device.
     */
    @Test
    fun testIsSystemAppWithNonSystemApp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageManager = context.packageManager

        // Get all installed packages
        val installedPackages = packageManager.getInstalledPackages(0)

        // Find a non-system app
        val nonSystemPackage = installedPackages.find { 
            (it.applicationInfo?.flags ?: 0) and ApplicationInfo.FLAG_SYSTEM == 0 
        }?.packageName

        if (nonSystemPackage != null) {
            val isSystemApp = InstallationSourceDetector.isSystemApp(context, nonSystemPackage)

            // The non-system package should not be a system app
            assertFalse("Non-system package should not be a system app", isSystemApp)
            println("[DEBUG_LOG] Non-system package ($nonSystemPackage) is system app: $isSystemApp")
        } else {
            println("[DEBUG_LOG] No non-system package found on the device, test skipped")
        }
    }

    /**
     * Test that prints the installation info of all installed packages.
     */
    @Test
    fun testPrintAllInstalledPackagesInstallationInfo() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageManager = context.packageManager

        // Get all installed packages
        val installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        println("[DEBUG_LOG] Found ${installedPackages.size} installed packages")
        println("[DEBUG_LOG] ===== INSTALLED APPLICATIONS WITH THEIR SOURCES =====")

        // Iterate through each package and print its installation info
        for (packageInfo in installedPackages) {
            val packageName = packageInfo.packageName
            val appName = try {
                packageInfo.applicationInfo?.loadLabel(packageManager)?.toString() ?: "Unknown"
            } catch (e: Exception) {
                "Unknown"
            }
            val installationInfo = InstallationSourceDetector.detectInstallationSource(context, packageName)
            val isSystemApp = (packageInfo.applicationInfo?.flags ?: 0) and ApplicationInfo.FLAG_SYSTEM != 0
            val installerName = installationInfo.installerName
            val source = installationInfo.source

            println("[DEBUG_LOG] App: $appName")
            println("[DEBUG_LOG]   - Package: $packageName")
            println("[DEBUG_LOG]   - Source: $source")
            println("[DEBUG_LOG]   - Installer: $installerName")
            println("[DEBUG_LOG]   - System App: $isSystemApp")
            println("[DEBUG_LOG] ------------------------------")
        }
        println("[DEBUG_LOG] ===== END OF INSTALLED APPLICATIONS =====")
    }

    /**
     * Test the classification of different installation sources.
     */
    @Test
    fun testClassifyInstaller() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Test Google Play Store
        val googlePlayInfo = createInstallationInfoWithMockInstaller(context, "com.android.vending")
        assertEquals("Google Play Store should be classified correctly", InstallationSource.GOOGLE_PLAY, googlePlayInfo.source)

        // Test Amazon Appstore
        val amazonInfo = createInstallationInfoWithMockInstaller(context, "com.amazon.venezia")
        assertEquals("Amazon Appstore should be classified correctly", InstallationSource.AMAZON, amazonInfo.source)

        // Test Samsung Galaxy Store
        val samsungInfo = createInstallationInfoWithMockInstaller(context, "com.sec.android.app.samsungapps")
        assertEquals("Samsung Galaxy Store should be classified correctly", InstallationSource.SAMSUNG, samsungInfo.source)

        // Test Huawei AppGallery
        val huaweiInfo = createInstallationInfoWithMockInstaller(context, "com.huawei.appmarket")
        assertEquals("Huawei AppGallery should be classified correctly", InstallationSource.HUAWEI, huaweiInfo.source)

        // Test unknown installer
        val unknownInfo = createInstallationInfoWithMockInstaller(context, "com.unknown.installer")
        assertEquals("Unknown installer should be classified as OTHER", InstallationSource.OTHER, unknownInfo.source)

        println("[DEBUG_LOG] Installation source classification tests completed successfully")
    }

    /**
     * Helper method to create an InstallationInfo object with a mock installer package.
     */
    private fun createInstallationInfoWithMockInstaller(context: Context, installerPackage: String): InstallationInfo {
        // This is a simplified way to test the classification logic without actually installing apps
        // We're directly calling the private method using reflection
        val method = InstallationSourceDetector.Companion::class.java.getDeclaredMethod("classifyInstaller", String::class.java)
        method.isAccessible = true
        return method.invoke(InstallationSourceDetector.Companion, installerPackage) as InstallationInfo
    }
}
