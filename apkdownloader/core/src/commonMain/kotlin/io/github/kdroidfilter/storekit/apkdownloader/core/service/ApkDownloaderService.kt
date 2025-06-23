package io.github.kdroidfilter.storekit.apkdownloader.core.service

import io.github.kdroidfilter.storekit.apkcombo.core.model.ApkComboApplicationInfo
import io.github.kdroidfilter.storekit.apkcombo.scraper.services.getApkComboApplicationInfo
import io.github.kdroidfilter.storekit.apkdownloader.core.model.ApkDownloadInfo
import io.github.kdroidfilter.storekit.apkdownloader.core.utils.FileUtils
import io.github.kdroidfilter.storekit.aptoide.api.services.AptoideService
import io.github.kdroidfilter.storekit.aptoide.core.model.AptoideApplicationInfo
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Service for retrieving APK download links from various sources based on configured priority.
 */
class ApkDownloaderService {
    private val logger = KotlinLogging.logger {}
    private val aptoideService = AptoideService()

    /**
     * Retrieves an APK download link for the specified package name.
     * The sources are tried in the order specified by the [ApkSourcePriority] configuration.
     *
     * @param packageName The package name of the application
     * @return An [ApkDownloadInfo] containing the download link and related information
     * @throws IllegalArgumentException if the application cannot be found in any of the configured sources
     */
    suspend fun getApkDownloadLink(packageName: String): ApkDownloadInfo {
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
                        return createApkDownloadInfoFromApkCombo(appInfo)
                    }
                    ApkSource.APTOIDE -> {
                        logger.info { "Trying to get download link from Aptoide" }
                        val appInfo = aptoideService.getAppMetaByPackageName(packageName)
                        return createApkDownloadInfoFromAptoide(appInfo)
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
     * Creates an [ApkDownloadInfo] from an [ApkComboApplicationInfo].
     */
    private suspend fun createApkDownloadInfoFromApkCombo(appInfo: ApkComboApplicationInfo): ApkDownloadInfo {
        // Get file size from download link
        val fileSize = FileUtils.getFileSizeFromUrl(appInfo.downloadLink)

        return ApkDownloadInfo(
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
     * Creates an [ApkDownloadInfo] from an [AptoideApplicationInfo].
     */
    private suspend fun createApkDownloadInfoFromAptoide(appInfo: AptoideApplicationInfo): ApkDownloadInfo {
        // Aptoide provides the download link in the file.path property
        val downloadLink = if (appInfo.file.path.isNotEmpty()) {
            appInfo.file.path
        } else {
            appInfo.file.path_alt
        }

        // Get file size from download link
        val fileSize = FileUtils.getFileSizeFromUrl(downloadLink)

        return ApkDownloadInfo(
            packageName = appInfo.packageName.ifEmpty { appInfo.package_ },
            downloadLink = downloadLink,
            source = ApkSource.APTOIDE.name,
            version = appInfo.file.vername,
            versionCode = appInfo.file.vercode.toString(),
            title = appInfo.name,
            fileSize = fileSize
        )
    }
}
