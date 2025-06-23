package io.github.kdroidfilter.storekit.apklinkresolver.core.utils

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class FileUtilsTest {
    
    @Test
    fun testGetFileSizeFromUrl_ValidUrl() = runBlocking {
        // Given
        // Using a reliable URL that should have a Content-Length header
        val url = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
        
        // When
        val fileSize = FileUtils.getFileSizeFromUrl(url)
        
        // Then
        println("[DEBUG_LOG] File size for $url: $fileSize bytes")
        assertTrue(fileSize > 0, "File size should be greater than 0 for a valid URL")
    }
    
    @Test
    fun testGetFileSizeFromUrl_InvalidUrl() = runBlocking {
        // Given
        val invalidUrl = "https://invalid.url.that.does.not.exist.example.com/file.txt"
        
        // When
        val fileSize = FileUtils.getFileSizeFromUrl(invalidUrl)
        
        // Then
        println("[DEBUG_LOG] File size for invalid URL: $fileSize bytes")
        assertTrue(fileSize == -1L, "File size should be -1 for an invalid URL")
    }
    
    @Test
    fun testGetFileSizeFromUrl_NoContentLengthHeader() = runBlocking {
        // Given
        // Some servers might not provide a Content-Length header
        // Using a URL that might use chunked transfer encoding
        val url = "https://httpbin.org/stream/10"
        
        // When
        val fileSize = FileUtils.getFileSizeFromUrl(url)
        
        // Then
        println("[DEBUG_LOG] File size for URL without Content-Length: $fileSize bytes")
        // The result could be -1 (if no Content-Length) or > 0 (if Content-Length is provided)
        assertTrue(fileSize == -1L || fileSize > 0L, 
            "File size should be either -1 (no Content-Length) or greater than 0")
    }
}