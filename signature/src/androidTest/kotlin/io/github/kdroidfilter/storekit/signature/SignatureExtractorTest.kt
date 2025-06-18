package io.github.kdroidfilter.storekit.signature

import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test for the SignatureExtractor class.
 */
@RunWith(AndroidJUnit4::class)
class SignatureExtractorTest {

    /**
     * Test extracting the signature of an installed app.
     * This test assumes that the Android system package is installed on the device.
     */
    @Test
    fun testExtractSignatureFromInstalledApp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageName = "android" // Android system package should always be installed

        val signature = SignatureExtractor.extractSha1Signature(context, packageName)

        // The Android system package should have a signature
        assertNotNull("Signature should not be null for installed package", signature)
        println("Android system package signature: $signature")
    }

    /**
     * Test extracting the signature of a non-existent app.
     */
    @Test
    fun testExtractSignatureFromNonExistentApp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageName = "com.nonexistent.app.that.does.not.exist"

        val signature = SignatureExtractor.extractSha1Signature(context, packageName)

        // A non-existent package should return null
        assertNull("Signature should be null for non-existent package", signature)
    }

    /**
     * Test that prints the signatures of all installed packages.
     */
    @Test
    fun testPrintAllInstalledPackagesSignatures() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageManager = context.packageManager

        // Get all installed packages
        val installedPackages = packageManager.getInstalledPackages(0)

        println("[DEBUG_LOG] Found ${installedPackages.size} installed packages")

        // Iterate through each package and print its signature
        for (packageInfo in installedPackages) {
            val packageName = packageInfo.packageName
            val signature = SignatureExtractor.extractSha1Signature(context, packageName)

            println("[DEBUG_LOG] Package: $packageName, Signature: $signature")
        }
    }

    /**
     * Test that verifies Chrome's signature if it's installed.
     * Chrome's signature should match the expected value.
     */
    @Test
    fun testChromeSignature() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageManager = context.packageManager
        val chromePackageName = "com.android.chrome"
        val expectedSignature = "38918a453d07199354f8b19af05ec6562ced5788"

        try {
            // Check if Chrome is installed
            packageManager.getPackageInfo(chromePackageName, 0)

            // Chrome is installed, extract its signature
            val signature = SignatureExtractor.extractSha1Signature(context, chromePackageName)

            // Verify that the signature is not null
            assertNotNull("Chrome signature should not be null", signature)

            // Verify that the signature matches the expected value
            assertEquals("Chrome signature should match the expected value", expectedSignature, signature)

            println("[DEBUG_LOG] Chrome signature verified: $signature")
        } catch (e: PackageManager.NameNotFoundException) {
            // Chrome is not installed, skip the test
            println("[DEBUG_LOG] Chrome is not installed on this device, test skipped")
        }
    }
}
