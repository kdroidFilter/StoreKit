package io.github.kdroidfilter.storekit.fdroid.api.services

import io.github.kdroidfilter.storekit.fdroid.api.constants.BASE_FDROID_API_URL
import io.github.kdroidfilter.storekit.fdroid.api.constants.PACKAGES_PATH
import io.github.kdroidfilter.storekit.fdroid.core.model.FDroidPackageInfo
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

/**
 * A service class for interacting with the F-Droid API.
 * This class provides methods to fetch package information from the F-Droid API.
 */
class FDroidService {
    private val logger = KotlinLogging.logger {}
    private val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Fetches package information from the F-Droid API using the package name.
     *
     * @param packageName The package name of the application.
     * @return An instance of [FDroidPackageInfo] containing the package information.
     * @throws IllegalArgumentException if the package with the given package name does not exist or is not accessible.
     */
    suspend fun getPackageInfo(packageName: String): FDroidPackageInfo {
        logger.info { "Fetching package information for package name: $packageName" }
        val url = "$BASE_FDROID_API_URL$PACKAGES_PATH/$packageName"

        val response = client.get(url)

        if (!response.status.isSuccess()) {
            throw IllegalArgumentException("Package with package name: $packageName does not exist or is not accessible. HTTP status: ${response.status}")
        }

        val responseText = response.bodyAsText()
        val fdroidPackageInfo = json.decodeFromString<FDroidPackageInfo>(responseText)
        logger.info { "Successfully fetched package information for package name: $packageName" }

        return fdroidPackageInfo
    }
}