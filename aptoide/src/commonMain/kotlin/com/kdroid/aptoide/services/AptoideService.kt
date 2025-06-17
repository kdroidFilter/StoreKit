package com.kdroid.aptoide.services

import com.kdroid.aptoide.constants.APP_GET_META_PATH
import com.kdroid.aptoide.constants.BASE_APTOIDE_API_URL
import com.kdroid.aptoide.core.model.AptoideApplicationInfo
import com.kdroid.aptoide.core.model.AptoideResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

/**
 * A service class for interacting with the Aptoide API.
 * This class provides methods to fetch app metadata from the Aptoide API.
 */
class AptoideService {
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
     * Fetches application metadata from the Aptoide API using the package name.
     *
     * @param packageName The package name of the application.
     * @param language The language code for the content localization. Defaults to "en".
     * @return An instance of [AptoideApplicationInfo] containing the application's metadata.
     * @throws IllegalArgumentException if the application with the given package name does not exist or is not accessible.
     */
    suspend fun getAppMetaByPackageName(packageName: String, language: String = "en"): AptoideApplicationInfo {
        logger.info { "Fetching app metadata for package name: $packageName" }
        val url = "$BASE_APTOIDE_API_URL$APP_GET_META_PATH"

        val response = client.get(url) {
            parameter("package_name", packageName)
            parameter("language", language)
        }

        if (!response.status.isSuccess()) {
            throw IllegalArgumentException("Application with package name: $packageName does not exist or is not accessible. HTTP status: ${response.status}")
        }

        val responseText = response.bodyAsText()
        val aptoideResponse = json.decodeFromString<AptoideResponse>(responseText)
        logger.info { "Successfully fetched app metadata for package name: $packageName" }

        return aptoideResponse.data
    }

    /**
     * Fetches application metadata from the Aptoide API using the app ID.
     *
     * @param appId The ID of the application.
     * @param language The language code for the content localization. Defaults to "en".
     * @return An instance of [AptoideApplicationInfo] containing the application's metadata.
     * @throws IllegalArgumentException if the application with the given ID does not exist or is not accessible.
     */
    suspend fun getAppMetaById(appId: Long, language: String = "en"): AptoideApplicationInfo {
        logger.info { "Fetching app metadata for app ID: $appId" }
        val url = "$BASE_APTOIDE_API_URL$APP_GET_META_PATH"

        val response = client.get(url) {
            parameter("app_id", appId)
            parameter("language", language)
        }

        if (!response.status.isSuccess()) {
            throw IllegalArgumentException("Application with app ID: $appId does not exist or is not accessible. HTTP status: ${response.status}")
        }

        val responseText = response.bodyAsText()
        val aptoideResponse = json.decodeFromString<AptoideResponse>(responseText)
        logger.info { "Successfully fetched app metadata for app ID: $appId" }

        return aptoideResponse.data
    }

    /**
     * Fetches application metadata from the Aptoide API using the APK MD5 sum.
     *
     * @param md5sum The MD5 sum of the APK file.
     * @param language The language code for the content localization. Defaults to "en".
     * @return An instance of [AptoideApplicationInfo] containing the application's metadata.
     * @throws IllegalArgumentException if the application with the given MD5 sum does not exist or is not accessible.
     */
    suspend fun getAppMetaByMd5sum(md5sum: String, language: String = "en"): AptoideApplicationInfo {
        logger.info { "Fetching app metadata for APK MD5 sum: $md5sum" }
        val url = "$BASE_APTOIDE_API_URL$APP_GET_META_PATH"

        val response = client.get(url) {
            parameter("apk_md5sum", md5sum)
            parameter("language", language)
        }

        if (!response.status.isSuccess()) {
            throw IllegalArgumentException("Application with APK MD5 sum: $md5sum does not exist or is not accessible. HTTP status: ${response.status}")
        }

        val responseText = response.bodyAsText()
        val aptoideResponse = json.decodeFromString<AptoideResponse>(responseText)
        logger.info { "Successfully fetched app metadata for APK MD5 sum: $md5sum" }

        return aptoideResponse.data
    }
}
