package io.github.kdroidfilter.storekit.aptoide.api.extensions

import io.github.kdroidfilter.storekit.aptoide.core.model.AptoideSignature

/**
 * Extension functions for [AptoideSignature] class.
 */

/**
 * Converts the SHA1 signature format from "XX:XX:XX..." to a continuous lowercase hex string.
 * 
 * Example: "35:B4:38:FE:1B:C6:9D:97:5D:C8:70:2D:C1:6A:B6:9E:BF:65:F2:6F" becomes "35b438fe1bc69d975dc8702dc16ab69ebf65f26f"
 *
 * @return The SHA1 signature as a continuous lowercase hex string.
 */
fun AptoideSignature.toFormattedSha1(): String {
    return sha1.replace(":", "").lowercase()
}