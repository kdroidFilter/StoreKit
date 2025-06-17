package io.github.kdroidfilter.androidappstorekit.aptoide.api.test

import io.github.kdroidfilter.androidappstorekit.aptoide.api.services.AptoideService
import io.github.kdroidfilter.androidappstorekit.aptoide.core.model.AptoideApplicationInfo
import io.github.kdroidfilter.androidappstorekit.aptoide.core.model.AptoideInfo
import io.github.kdroidfilter.androidappstorekit.aptoide.core.model.AptoideResponse
import io.github.kdroidfilter.androidappstorekit.aptoide.core.model.AptoideTime
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.*

class AptoideServiceTest {
    private lateinit var mockEngine: MockEngine
    private lateinit var mockClient: HttpClient
    private lateinit var aptoideService: AptoideService
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @BeforeTest
    fun setup() {
        mockEngine = MockEngine { request ->
            val url = request.url.toString()
            val parameters = request.url.parameters

            when {
                // Test for getAppMetaByPackageName
                url.contains("/app/getMeta") && parameters["package_name"] == "com.example.app" -> {
                    val response = createMockResponse("com.example.app", 123L, "Example App")
                    respond(
                        content = ByteReadChannel(response),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                // Test for getAppMetaById
                url.contains("/app/getMeta") && parameters["app_id"] == "456" -> {
                    val response = createMockResponse("com.example.appbyid", 456L, "Example App By ID")
                    respond(
                        content = ByteReadChannel(response),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                // Test for getAppMetaByMd5sum
                url.contains("/app/getMeta") && parameters["apk_md5sum"] == "abc123" -> {
                    val response = createMockResponse("com.example.appbymd5", 789L, "Example App By MD5")
                    respond(
                        content = ByteReadChannel(response),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                // Test for error case
                url.contains("/app/getMeta") && parameters["package_name"] == "com.nonexistent.app" -> {
                    respond(
                        content = ByteReadChannel("{ \"error\": \"App not found\" }"),
                        status = HttpStatusCode.NotFound,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                // Default case
                else -> {
                    respond(
                        content = ByteReadChannel("{ \"error\": \"Unknown request\" }"),
                        status = HttpStatusCode.BadRequest,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }

        mockClient = HttpClient(mockEngine) {
            install(Logging) {
                level = LogLevel.INFO
            }
        }

        // Create a test version of AptoideService that uses our mock client
        aptoideService = TestAptoideService(mockClient)
    }

    @Test
    fun testGetAppMetaByPackageName() = runTest {
        val appInfo = aptoideService.getAppMetaByPackageName("com.example.app")

        assertNotNull(appInfo)
        assertEquals("com.example.app", appInfo.packageName)
        assertEquals(123L, appInfo.id)
        assertEquals("Example App", appInfo.name)
    }

    @Test
    fun testGetAppMetaById() = runTest {
        val appInfo = aptoideService.getAppMetaById(456L)

        assertNotNull(appInfo)
        assertEquals("com.example.appbyid", appInfo.packageName)
        assertEquals(456L, appInfo.id)
        assertEquals("Example App By ID", appInfo.name)
    }

    @Test
    fun testGetAppMetaByMd5sum() = runTest {
        val appInfo = aptoideService.getAppMetaByMd5sum("abc123")

        assertNotNull(appInfo)
        assertEquals("com.example.appbymd5", appInfo.packageName)
        assertEquals(789L, appInfo.id)
        assertEquals("Example App By MD5", appInfo.name)
    }

    @Test
    fun testGetAppMetaByPackageNameError() = runTest {
        assertFailsWith<IllegalArgumentException> {
            aptoideService.getAppMetaByPackageName("com.nonexistent.app")
        }
    }

    private fun createMockResponse(packageName: String, id: Long, name: String): String {
        val appInfo = AptoideApplicationInfo(
            id = id,
            name = name,
            packageName = packageName
        )

        val response = AptoideResponse(
            info = AptoideInfo(
                status = "OK",
                time = AptoideTime(
                    seconds = 0.1,
                    human = "0.1 seconds"
                )
            ),
            data = appInfo
        )

        return json.encodeToString(response)
    }

    // Test implementation of AptoideService that uses our mock client
    private class TestAptoideService(private val client: HttpClient) : AptoideService() {
        override fun getHttpClient(): HttpClient {
            return client
        }
    }
}