
import com.kdroid.gplayscrapper.services.getGooglePlayApplicationInfo
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true

    }
    runBlocking {
        val appId = "com.android.chrome"
        val appInfo = getGooglePlayApplicationInfo(appId, )
        println(json.encodeToString(appInfo))
    }
}