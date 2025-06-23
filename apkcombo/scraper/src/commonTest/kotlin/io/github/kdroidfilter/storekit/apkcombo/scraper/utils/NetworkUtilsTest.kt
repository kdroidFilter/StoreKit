package io.github.kdroidfilter.storekit.apkcombo.scraper.utils

import io.github.kdroidfilter.storekit.apkcombo.scraper.constants.BASE_APKCOMBO_URL
import kotlin.test.Test
import kotlin.test.assertEquals

class NetworkUtilsTest {

    @Test
    fun testCleanDownloadLink_WithR2Prefix() {
        // Given
        val encodedUrl = "https%3A%2F%2Fexample.com%2Fapp.apk"
        val link = "/r2?u=$encodedUrl"
        
        // When
        val cleanedLink = cleanDownloadLink(link)
        
        // Then
        assertEquals("https://example.com/app.apk", cleanedLink, 
            "Should decode URL-encoded part after 'u='")
        println("[DEBUG_LOG] Original link: $link")
        println("[DEBUG_LOG] Cleaned link: $cleanedLink")
    }
    
    @Test
    fun testCleanDownloadLink_WithSlashPrefix() {
        // Given
        val link = "/download/app.apk"
        
        // When
        val cleanedLink = cleanDownloadLink(link)
        
        // Then
        assertEquals("$BASE_APKCOMBO_URL$link", cleanedLink, 
            "Should prepend the base APKCombo URL")
        println("[DEBUG_LOG] Original link: $link")
        println("[DEBUG_LOG] Cleaned link: $cleanedLink")
    }
    
    @Test
    fun testCleanDownloadLink_WithFullUrl() {
        // Given
        val link = "https://example.com/app.apk"
        
        // When
        val cleanedLink = cleanDownloadLink(link)
        
        // Then
        assertEquals(link, cleanedLink, 
            "Should return the link unchanged")
        println("[DEBUG_LOG] Original link: $link")
        println("[DEBUG_LOG] Cleaned link: $cleanedLink")
    }
    
    @Test
    fun testCleanDownloadLink_WithInvalidEncoding() {
        // Given
        val invalidEncodedUrl = "https%3A%2F%2Fexample.com%2Fapp.apk%"  // Invalid % at the end
        val link = "/r2?u=$invalidEncodedUrl"
        
        // When
        val cleanedLink = cleanDownloadLink(link)
        
        // Then
        assertEquals(link, cleanedLink, 
            "Should return the original link when decoding fails")
        println("[DEBUG_LOG] Original link with invalid encoding: $link")
        println("[DEBUG_LOG] Cleaned link: $cleanedLink")
    }
}