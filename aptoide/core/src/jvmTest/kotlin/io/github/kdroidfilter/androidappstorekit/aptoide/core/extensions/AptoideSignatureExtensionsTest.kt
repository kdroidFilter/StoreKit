package io.github.kdroidfilter.androidappstorekit.aptoide.core.extensions

import io.github.kdroidfilter.androidappstorekit.aptoide.core.model.AptoideSignature
import kotlin.test.Test
import kotlin.test.assertEquals

class AptoideSignatureExtensionsTest {

    @Test
    fun testSha1ToHexString() {
        // Given
        val signature = AptoideSignature(
            sha1 = "35:B4:38:FE:1B:C6:9D:97:5D:C8:70:2D:C1:6A:B6:9E:BF:65:F2:6F",
            owner = "Test Owner"
        )
        
        // When
        val result = signature.sha1ToHexString()
        
        // Then
        assertEquals("35b438fe1bc69d975dc8702dc16ab69ebf65f26f", result)
    }
    
    @Test
    fun testSha1ToHexStringWithEmptyString() {
        // Given
        val signature = AptoideSignature(
            sha1 = "",
            owner = "Test Owner"
        )
        
        // When
        val result = signature.sha1ToHexString()
        
        // Then
        assertEquals("", result)
    }
    
    @Test
    fun testSha1ToHexStringWithLowercase() {
        // Given
        val signature = AptoideSignature(
            sha1 = "aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99:aa:bb:cc:dd",
            owner = "Test Owner"
        )
        
        // When
        val result = signature.sha1ToHexString()
        
        // Then
        assertEquals("aabbccddeeff00112233445566778899aabbccdd", result)
    }
}