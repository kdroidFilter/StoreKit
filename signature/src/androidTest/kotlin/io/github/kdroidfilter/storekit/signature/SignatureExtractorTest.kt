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
    /**
     * Test that verifies Chrome's signature using the verifyPackageSignature function.
     * This test checks if the verifyPackageSignature function correctly validates Chrome's signature.
     */
    @Test
    fun testVerifyPackageSignatureWithChrome() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageManager = context.packageManager
        val chromePackageName = "com.android.chrome"
        val expectedSignature = "38918a453d07199354f8b19af05ec6562ced5788"

        try {
            // Check if Chrome is installed
            packageManager.getPackageInfo(chromePackageName, 0)

            // Verify Chrome's signature using the verifyPackageSignature function
            val isSignatureValid = SignatureExtractor.verifyPackageSignature(
                context, 
                chromePackageName, 
                expectedSignature
            )

            // The signature should be valid
            assertEquals("Chrome signature verification should succeed", true, isSignatureValid)
            println("[DEBUG_LOG] Chrome signature verification succeeded")

            // Test with an incorrect signature to ensure the function can detect invalid signatures
            val incorrectSignature = "0000000000000000000000000000000000000000"
            val isIncorrectSignatureValid = SignatureExtractor.verifyPackageSignature(
                context, 
                chromePackageName, 
                incorrectSignature
            )

            // The incorrect signature should be invalid
            assertEquals("Incorrect signature verification should fail", false, isIncorrectSignatureValid)
            println("[DEBUG_LOG] Incorrect signature verification failed as expected")

        } catch (e: PackageManager.NameNotFoundException) {
            // Chrome is not installed, skip the test
            println("[DEBUG_LOG] Chrome is not installed on this device, test skipped")
        }
    }

    /**
     * Test the isValidSha1Signature function with various inputs.
     * This test verifies that the function correctly identifies valid and invalid SHA1 signatures.
     */
    @Test
    fun testIsValidSha1Signature() {
        // Valid SHA1 signatures (40 characters, hex only)
        val validSignature1 = "38918a453d07199354f8b19af05ec6562ced5788" // Chrome's signature
        val validSignature2 = "0123456789abcdef0123456789abcdef01234567"
        val validSignature3 = "ABCDEF0123456789ABCDEF0123456789ABCDEF01" // Uppercase is also valid

        // Invalid SHA1 signatures
        val invalidSignature1 = "too_short" // Too short
        val invalidSignature2 = "38918a453d07199354f8b19af05ec6562ced5788extra" // Too long
        val invalidSignature3 = "38918a453d07199354f8b19af05ec6562ced578g" // Contains invalid character 'g'
        val invalidSignature4 = "38918a453d07199354f8b19af05ec6562ced578!" // Contains invalid character '!'
        val invalidSignature5 = "" // Empty string

        // Test valid signatures
        assertEquals("Valid signature 1 should be recognized as valid", true, SignatureExtractor.isValidSha1Signature(validSignature1))
        assertEquals("Valid signature 2 should be recognized as valid", true, SignatureExtractor.isValidSha1Signature(validSignature2))
        assertEquals("Valid signature 3 should be recognized as valid", true, SignatureExtractor.isValidSha1Signature(validSignature3))

        // Test invalid signatures
        assertEquals("Invalid signature 1 should be recognized as invalid", false, SignatureExtractor.isValidSha1Signature(invalidSignature1))
        assertEquals("Invalid signature 2 should be recognized as invalid", false, SignatureExtractor.isValidSha1Signature(invalidSignature2))
        assertEquals("Invalid signature 3 should be recognized as invalid", false, SignatureExtractor.isValidSha1Signature(invalidSignature3))
        assertEquals("Invalid signature 4 should be recognized as invalid", false, SignatureExtractor.isValidSha1Signature(invalidSignature4))
        assertEquals("Invalid signature 5 should be recognized as invalid", false, SignatureExtractor.isValidSha1Signature(invalidSignature5))

        println("[DEBUG_LOG] isValidSha1Signature tests completed successfully")
    }
}
