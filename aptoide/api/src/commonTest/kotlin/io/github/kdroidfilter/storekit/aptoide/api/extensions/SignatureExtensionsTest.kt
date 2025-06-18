package io.github.kdroidfilter.storekit.aptoide.api.extensions

import io.github.kdroidfilter.storekit.aptoide.core.model.AptoideSignature
import kotlin.test.Test
import kotlin.test.assertEquals

class SignatureExtensionsTest {

    @Test
    fun testToFormattedSha1() {
        // Given
        val signature = AptoideSignature(
            sha1 = "35:B4:38:FE:1B:C6:9D:97:5D:C8:70:2D:C1:6A:B6:9E:BF:65:F2:6F",
            owner = "Test Owner"
        )

        // When
        val formattedSha1 = signature.toFormattedSha1()

        // Then
        assertEquals("35b438fe1bc69d975dc8702dc16ab69ebf65f26f", formattedSha1)
    }

    @Test
    fun testToFormattedSha1WithEmptySha1() {
        // Given
        val signature = AptoideSignature(
            sha1 = "",
            owner = "Test Owner"
        )

        // When
        val formattedSha1 = signature.toFormattedSha1()

        // Then
        assertEquals("", formattedSha1)
    }

    @Test
    fun testToFormattedSha1WithoutColons() {
        // Given
        val signature = AptoideSignature(
            sha1 = "35B438FE1BC69D975DC8702DC16AB69EBF65F26F",
            owner = "Test Owner"
        )

        // When
        val formattedSha1 = signature.toFormattedSha1()

        // Then
        assertEquals("35b438fe1bc69d975dc8702dc16ab69ebf65f26f", formattedSha1)
    }

    @Test
    fun testSpecificSha1Value() {
        // Given
        val signature = AptoideSignature(
            sha1 = "38:91:8A:45:3D:07:19:93:54:F8:B1:9A:F0:5E:C6:56:2C:ED:57:88",
            owner = "Aptoide"
        )

        // When
        val formattedSha1 = signature.toFormattedSha1()

        // Then
        assertEquals("38918a453d07199354f8b19af05ec6562ced5788", formattedSha1)
    }
}
