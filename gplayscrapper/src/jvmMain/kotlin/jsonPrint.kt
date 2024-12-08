
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
        val appId = "com.MizrahiTefahot.nh"
        val appInfo = getGooglePlayApplicationInfo(appId, "he", "il" )
        println(json.encodeToString(appInfo))
    }
}