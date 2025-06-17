package io.github.kdroidfilter.androidappstorekit.aptoide.core.extensions

import io.github.kdroidfilter.androidappstorekit.aptoide.core.model.AptoideSignature

/**
 * Extension functions for [AptoideSignature] class.
 */

/**
 * Converts the SHA-1 signature from colon-separated format to a continuous lowercase hexadecimal string.
 * 
 * Example:
 * Input: "35:B4:38:FE:1B:C6:9D:97:5D:C8:70:2D:C1:6A:B6:9E:BF:65:F2:6F"
 * Output: "35b438fe1bc69d975dc8702dc16ab69ebf65f26f"
 * 
 * @return The SHA-1 signature as a continuous lowercase hexadecimal string.
 */
fun AptoideSignature.sha1ToHexString(): String {
    return sha1.replace(":", "").lowercase()
}