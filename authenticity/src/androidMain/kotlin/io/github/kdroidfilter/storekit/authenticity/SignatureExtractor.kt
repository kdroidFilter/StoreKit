package io.github.kdroidfilter.storekit.authenticity

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.content.pm.SigningInfo
import android.os.Build
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Utility class for extracting app signatures in SHA1 format from installed Android applications.
 */
class SignatureExtractor {
    companion object {
        /**
         * Extracts the SHA1 signature of an installed Android application.
         *
         * @param context The Android context.
         * @param packageName The package name of the application.
         * @return The SHA1 signature of the application, or null if the application is not installed or the signature cannot be extracted.
         */
        fun extractSha1Signature(context: Context, packageName: String): String? {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    // For Android P (API 28) and above, use GET_SIGNING_CERTIFICATES
                    val packageInfo = context.packageManager.getPackageInfo(
                        packageName, 
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )
                    val signingInfo = packageInfo.signingInfo
                    val signatures = signingInfo?.apkContentsSigners
                    if (signatures != null && signatures.isNotEmpty()) {
                        convertSignatureToSha1(signatures[0])
                    } else {
                        null
                    }
                } else {
                    // For older versions, use GET_SIGNATURES with safe calls
                    @Suppress("DEPRECATION")
                    val packageInfo = context.packageManager.getPackageInfo(
                        packageName, 
                        PackageManager.GET_SIGNATURES
                    )
                    val signatures = packageInfo.signatures
                    if (signatures != null && signatures.isNotEmpty()) {
                        convertSignatureToSha1(signatures[0])
                    } else {
                        null
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                null
            } catch (e: NoSuchAlgorithmException) {
                null
            } catch (e: Exception) {
                null
            }
        }

        /**
         * Converts a signature to SHA1 format.
         *
         * @param signature The signature to convert.
         * @return The SHA1 representation of the signature.
         * @throws NoSuchAlgorithmException If the SHA1 algorithm is not available.
         */
        @Throws(NoSuchAlgorithmException::class)
        private fun convertSignatureToSha1(signature: Signature): String {
            val md = MessageDigest.getInstance("SHA1")
            md.update(signature.toByteArray())
            return bytesToHexString(md.digest())
        }

        /**
         * Converts a byte array to a hexadecimal string.
         *
         * @param bytes The byte array to convert.
         * @return The hexadecimal string representation of the byte array.
         */
        private fun bytesToHexString(bytes: ByteArray): String {
            val sb = StringBuilder()
            for (b in bytes) {
                val hex = Integer.toHexString(0xFF and b.toInt())
                if (hex.length == 1) {
                    sb.append('0')
                }
                sb.append(hex)
            }
            return sb.toString()
        }

        /**
         * Verifies if a given string is a valid SHA1 signature.
         *
         * @param sha1String The SHA1 string to verify.
         * @return True if the string is a valid SHA1 signature, false otherwise.
         */
        fun isValidSha1Signature(sha1String: String): Boolean {
            // A valid SHA1 hash is 40 characters long (20 bytes * 2 hex chars per byte)
            if (sha1String.length != 40) {
                return false
            }

            // Check if all characters are valid hexadecimal digits
            return sha1String.all { char ->
                char in '0'..'9' || char in 'a'..'f' || char in 'A'..'F'
            }
        }

        /**
         * Verifies if the signature of an installed package matches a provided SHA1 signature.
         *
         * @param context The Android context.
         * @param packageName The package name of the application to verify.
         * @param expectedSha1 The expected SHA1 signature to compare against.
         * @return True if the signatures match, false otherwise (including if the package is not installed
         *         or the signature cannot be extracted, or if the provided SHA1 is invalid).
         */
        fun verifyPackageSignature(context: Context, packageName: String, expectedSha1: String): Boolean {
            // First, validate the expected SHA1 signature
            if (!isValidSha1Signature(expectedSha1)) {
                return false
            }

            // Extract the actual signature from the installed package
            val actualSha1 = extractSha1Signature(context, packageName) ?: return false

            // Compare the signatures (case-insensitive comparison)
            return actualSha1.equals(expectedSha1, ignoreCase = true)
        }
    }
}