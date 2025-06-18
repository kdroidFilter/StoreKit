package io.github.kdroidfilter.storekit.aptoide.api.services

import io.github.kdroidfilter.storekit.aptoide.api.extensions.toFormattedSha1
import io.github.kdroidfilter.storekit.aptoide.core.model.AptoideApplicationInfo
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AptoideServiceTest {

    @Test
    fun testRetrieveAndVerifySignature() = runBlocking {
        // Given
        val aptoideService = AptoideService()
        val packageName = "com.android.chrome" // Using Chrome as an example
        val expectedSignature = "38918a453d07199354f8b19af05ec6562ced5788"
        
        try {
            // When
            val appInfo: AptoideApplicationInfo = aptoideService.getAppMetaByPackageName(packageName)
            
            // Then
            assertNotNull(appInfo, "App info should not be null")
            assertNotNull(appInfo.file, "File info should not be null")
            assertNotNull(appInfo.file.signature, "Signature should not be null")
            
            val signature = appInfo.file.signature
            assertNotNull(signature.sha1, "SHA1 should not be null")
            
            // Format the signature using the extension function
            val formattedSha1 = signature.toFormattedSha1()
            
            // Verify the signature matches the expected value
            assertEquals(expectedSignature, formattedSha1, "Signature should match the expected value")
            
            println("[DEBUG_LOG] Retrieved signature: ${signature.sha1}")
            println("[DEBUG_LOG] Formatted signature: $formattedSha1")
            println("[DEBUG_LOG] Expected signature: $expectedSignature")
            
        } catch (e: Exception) {
            println("[DEBUG_LOG] Error retrieving app info: ${e.message}")
            throw e
        }
    }
}