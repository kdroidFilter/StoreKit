
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
        val appId = "com.openai.chatgpt"
        val appInfo = getGooglePlayApplicationInfo(appId, "fr", "fr" )
        println(json.encodeToString(appInfo))
    }
}