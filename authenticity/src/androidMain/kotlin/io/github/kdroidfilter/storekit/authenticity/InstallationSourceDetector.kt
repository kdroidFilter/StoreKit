package io.github.kdroidfilter.storekit.authenticity

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

/**
 * Enum representing different installation sources for Android applications.
 */
enum class InstallationSource {
    GOOGLE_PLAY,
    AMAZON,
    SAMSUNG,
    HUAWEI,
    XIAOMI,
    OPPO,
    VIVO,
    ONEPLUS,
    SIDELOADED,
    SYSTEM,
    OTHER
}

/**
 * Data class containing information about the installation source of an application.
 *
 * @property installerPackage The package name of the installer, or null if unknown.
 * @property installerName A human-readable name of the installer.
 * @property source The categorized installation source (including SYSTEM when applicable).
 */
data class InstallationInfo(
    val installerPackage: String?,
    val installerName: String,
    val source: InstallationSource,
    val isSystemApp: Boolean = source == InstallationSource.SYSTEM
)

/**
 * Utility class for detecting the installation source of Android applications.
 */
class InstallationSourceDetector {

    companion object {
        /**
         * Detects the installation source of an Android application.
         *
         * @param context The Android context.
         * @param packageName The package name of the application.
         * @return Information about the installation source.
         */
        fun detectInstallationSource(context: Context, packageName: String): InstallationInfo {
            val installerPackage = getInstallerPackageName(context, packageName)

            return if (installerPackage.isNullOrEmpty()) {
                // Si aucun installateur n'est identifié, on considère l'app comme sideloaded
                InstallationInfo(null, "Sideloaded/Unknown", InstallationSource.SIDELOADED)
            } else {
                classifyInstaller(installerPackage)
            }
        }

        /**
         * Checks if an application is a system app.
         *
         * @param context The Android context.
         * @param packageName The package name of the application.
         * @return True if the application is a system app, false otherwise.
         */
        fun isSystemApp(context: Context, packageName: String): Boolean {
            return try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(packageName, 0)

                // Check if the app is installed in the system partition
                (packageInfo.applicationInfo?.flags ?: 0) and ApplicationInfo.FLAG_SYSTEM != 0
            } catch (e: Exception) {
                false
            }
        }

        /**
         * Gets the package name of the installer for an application.
         *
         * @param context The Android context.
         * @param packageName The package name of the application.
         * @return The package name of the installer, or null if it cannot be determined.
         */
        private fun getInstallerPackageName(context: Context, packageName: String): String? {
            return try {
                val packageManager = context.packageManager

                // Si c'est une app système, on retourne un identifiant spécial
                if (isSystemApp(context, packageName)) {
                    return "android.system"
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    packageManager.getInstallSourceInfo(packageName).installingPackageName
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.getInstallerPackageName(packageName)
                }
            } catch (e: Exception) {
                null
            }
        }

        /**
         * Classifies the installer package name into a more specific installation source.
         *
         * @param installerPackage The package name of the installer.
         * @return Information about the classified installation source.
         */
        private fun classifyInstaller(installerPackage: String): InstallationInfo {
            return when {
                installerPackage == "android.system" ->
                    InstallationInfo(installerPackage, "Système Android", InstallationSource.SYSTEM)

                installerPackage.contains("google", ignoreCase = true) || 
                installerPackage == "com.android.vending" -> 
                    InstallationInfo(installerPackage, "Google Play Store", InstallationSource.GOOGLE_PLAY)

                installerPackage.contains("amazon", ignoreCase = true) || 
                installerPackage == "com.amazon.venezia" -> 
                    InstallationInfo(installerPackage, "Amazon Appstore", InstallationSource.AMAZON)

                installerPackage.contains("samsung", ignoreCase = true) || 
                installerPackage == "com.sec.android.app.samsungapps" -> 
                    InstallationInfo(installerPackage, "Samsung Galaxy Store", InstallationSource.SAMSUNG)

                installerPackage.contains("huawei", ignoreCase = true) || 
                installerPackage == "com.huawei.appmarket" -> 
                    InstallationInfo(installerPackage, "Huawei AppGallery", InstallationSource.HUAWEI)

                installerPackage.contains("xiaomi", ignoreCase = true) || 
                installerPackage == "com.xiaomi.market" -> 
                    InstallationInfo(installerPackage, "Xiaomi GetApps", InstallationSource.XIAOMI)

                installerPackage.contains("oppo", ignoreCase = true) || 
                installerPackage == "com.oppo.market" -> 
                    InstallationInfo(installerPackage, "OPPO App Market", InstallationSource.OPPO)

                installerPackage.contains("vivo", ignoreCase = true) || 
                installerPackage == "com.vivo.appstore" -> 
                    InstallationInfo(installerPackage, "Vivo App Store", InstallationSource.VIVO)

                installerPackage.contains("oneplus", ignoreCase = true) || 
                installerPackage == "com.oneplus.market" -> 
                    InstallationInfo(installerPackage, "OnePlus App Market", InstallationSource.ONEPLUS)

                else -> InstallationInfo(installerPackage, "Other Store", InstallationSource.OTHER)
            }
        }
    }
}
