package io.github.kdroidfilter.storekit.apklinkresolver.core.service

import io.github.kdroidfilter.storekit.apkcombo.core.model.ApkComboApplicationInfo
import io.github.kdroidfilter.storekit.apkcombo.scraper.services.getApkComboApplicationInfo
import io.github.kdroidfilter.storekit.apklinkresolver.core.model.ApkLinkInfo
import io.github.kdroidfilter.storekit.apklinkresolver.core.utils.FileUtils
import io.github.kdroidfilter.storekit.aptoide.api.services.AptoideService
import io.github.kdroidfilter.storekit.aptoide.core.model.AptoideApplicationInfo
import io.github.kdroidfilter.storekit.fdroid.api.services.FDroidService
import io.github.kdroidfilter.storekit.fdroid.core.model.FDroidPackageInfo
import io.github.kdroidfilter.storekit.apkpure.core.model.ApkPureApplicationInfo
import io.github.kdroidfilter.storekit.apkpure.scraper.services.getApkPureApplicationInfo
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Service for retrieving APK download links from various sources based on configured priority.
 */
class ApkLinkResolverService {
    private val logger = KotlinLogging.logger {}
    private val aptoideService = AptoideService()
    private val fdroidService = FDroidService()

    /**
     * Retrieves an APK download link for the specified package name.
     * The sources are tried in the order specified by the [ApkSourcePriority] configuration.
     *
     * @param packageName The package name of the application
     * @return An [ApkLinkInfo] containing the download link and related information
     * @throws IllegalArgumentException if the application cannot be found in any of the configured sources
     */
    suspend fun getApkDownloadLink(packageName: String): ApkLinkInfo {
        logger.info { "Retrieving APK download link for package: $packageName" }

        val priorityOrder = ApkSourcePriority.getPriorityOrder()
        logger.info { "Using source priority order: $priorityOrder" }

        val exceptions = mutableListOf<Exception>()

        for (source in priorityOrder) {
            try {
                when (source) {
                    ApkSource.APKCOMBO -> {
                        logger.info { "Trying to get download link from APKCombo" }
                        val appInfo = getApkComboApplicationInfo(packageName)
                        return createApkLinkInfoFromApkCombo(appInfo)
                    }
                    ApkSource.APTOIDE -> {
                        logger.info { "Trying to get download link from Aptoide" }
                        val appInfo = aptoideService.getAppMetaByPackageName(packageName)
                        return createApkLinkInfoFromAptoide(appInfo)
                    }
                    ApkSource.FDROID -> {
                        logger.info { "Trying to get download link from F-Droid" }
                        val packageInfo = fdroidService.getPackageInfo(packageName)
                        return createApkLinkInfoFromFDroid(packageInfo)
                    }
                    ApkSource.APKPURE -> {
                        logger.info { "Trying to get download link from APKPure" }
                        val appInfo = getApkPureApplicationInfo(packageName)
                        return createApkLinkInfoFromApkPure(appInfo)
                    }
                }
            } catch (e: Exception) {
                logger.warn { "Failed to get download link from $source: ${e.message}" }
                exceptions.add(e)
            }
        }

        // If we get here, all sources failed
        val errorMessage = buildString {
            append("Failed to retrieve APK download link for package: $packageName. ")
            append("Tried the following sources: ")
            priorityOrder.forEachIndexed { index, source ->
                if (index > 0) append(", ")
                append(source.name)
            }
            append(". Errors: ")
            exceptions.forEachIndexed { index, exception ->
                if (index > 0) append("; ")
                append("${priorityOrder[index].name}: ${exception.message}")
            }
        }

        logger.error { errorMessage }
        throw IllegalArgumentException(errorMessage)
    }

    /**
     * Creates an [ApkLinkInfo] from an [ApkComboApplicationInfo].
     */
    private suspend fun createApkLinkInfoFromApkCombo(appInfo: ApkComboApplicationInfo): ApkLinkInfo {
        // Get file size from download link
        val fileSize = FileUtils.getFileSizeFromUrl(appInfo.downloadLink)

        return ApkLinkInfo(
            packageName = appInfo.appId,
            downloadLink = appInfo.downloadLink,
            source = ApkSource.APKCOMBO.name,
            version = appInfo.version,
            versionCode = appInfo.versionCode,
            title = appInfo.title,
            fileSize = fileSize
        )
    }

    /**
     * Creates an [ApkLinkInfo] from an [AptoideApplicationInfo].
     */
    private suspend fun createApkLinkInfoFromAptoide(appInfo: AptoideApplicationInfo): ApkLinkInfo {
        // Aptoide provides the download link in the file.path property
        val downloadLink = if (appInfo.file.path.isNotEmpty()) {
            appInfo.file.path
        } else {
            appInfo.file.path_alt
        }

        // Get file size from download link
        val fileSize = FileUtils.getFileSizeFromUrl(downloadLink)

        return ApkLinkInfo(
            packageName = appInfo.packageName.ifEmpty { appInfo.package_ },
            downloadLink = downloadLink,
            source = ApkSource.APTOIDE.name,
            version = appInfo.file.vername,
            versionCode = appInfo.file.vercode.toString(),
            title = appInfo.name,
            fileSize = fileSize
        )
    }

    /**
     * Creates an [ApkLinkInfo] from an [ApkPureApplicationInfo].
     */
    private suspend fun createApkLinkInfoFromApkPure(appInfo: ApkPureApplicationInfo): ApkLinkInfo {
        val fileSize = FileUtils.getFileSizeFromUrl(appInfo.downloadLink)
        return ApkLinkInfo(
            packageName = appInfo.appId,
            downloadLink = appInfo.downloadLink,
            source = ApkSource.APKPURE.name,
            version = appInfo.version,
            versionCode = appInfo.versionCode,
            title = appInfo.title,
            fileSize = fileSize
        )
    }

    /**
     * Creates an [ApkLinkInfo] from a [FDroidPackageInfo].
     */
    private suspend fun createApkLinkInfoFromFDroid(packageInfo: FDroidPackageInfo): ApkLinkInfo {
        // Get the download link for the suggested version
        val downloadLink = packageInfo.getSuggestedVersionDownloadLink()
            ?: throw IllegalArgumentException("No download link available for package: ${packageInfo.packageName}")

        // Get file size from download link
        val fileSize = FileUtils.getFileSizeFromUrl(downloadLink)

        // Find the suggested version details
        val suggestedVersion = packageInfo.packages.find { it.versionCode == packageInfo.suggestedVersionCode }

        return ApkLinkInfo(
            packageName = packageInfo.packageName,
            downloadLink = downloadLink,
            source = ApkSource.FDROID.name,
            version = suggestedVersion?.versionName ?: "",
            versionCode = packageInfo.suggestedVersionCode.toString(),
            title = packageInfo.packageName, // F-Droid API doesn't provide app title in the package info
            fileSize = fileSize
        )
    }
}
