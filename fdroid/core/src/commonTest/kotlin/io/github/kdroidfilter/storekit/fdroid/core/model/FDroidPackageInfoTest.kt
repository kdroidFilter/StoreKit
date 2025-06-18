package io.github.kdroidfilter.storekit.fdroid.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FDroidPackageInfoTest {

    @Test
    fun testGetDownloadLink() {
        // Given
        val packageName = "net.thunderbird.android"
        val suggestedVersionCode = 10L
        val packages = listOf(
            FDroidPackageVersion(versionName = "10.0", versionCode = 10),
            FDroidPackageVersion(versionName = "9.0", versionCode = 9),
            FDroidPackageVersion(versionName = "8.2", versionCode = 8)
        )
        val packageInfo = FDroidPackageInfo(
            packageName = packageName,
            suggestedVersionCode = suggestedVersionCode,
            packages = packages
        )

        // When - Test with valid version code
        val downloadLink = packageInfo.getDownloadLink(10)

        // Then
        assertEquals(
            "https://f-droid.org/repo/net.thunderbird.android_10.apk",
            downloadLink,
            "Download link should be correctly formatted"
        )

        // When - Test with another valid version code
        val downloadLink2 = packageInfo.getDownloadLink(9)

        // Then
        assertEquals(
            "https://f-droid.org/repo/net.thunderbird.android_9.apk",
            downloadLink2,
            "Download link should be correctly formatted for version 9"
        )

        // When - Test with invalid version code
        val invalidDownloadLink = packageInfo.getDownloadLink(999)

        // Then
        assertNull(
            invalidDownloadLink,
            "Download link should be null for non-existent version code"
        )
    }

    @Test
    fun testGetSuggestedVersionDownloadLink() {
        // Given
        val packageName = "net.thunderbird.android"
        val suggestedVersionCode = 10L
        val packages = listOf(
            FDroidPackageVersion(versionName = "10.0", versionCode = 10),
            FDroidPackageVersion(versionName = "9.0", versionCode = 9),
            FDroidPackageVersion(versionName = "8.2", versionCode = 8)
        )
        val packageInfo = FDroidPackageInfo(
            packageName = packageName,
            suggestedVersionCode = suggestedVersionCode,
            packages = packages
        )

        // When
        val suggestedDownloadLink = packageInfo.getSuggestedVersionDownloadLink()

        // Then
        assertEquals(
            "https://f-droid.org/repo/net.thunderbird.android_10.apk",
            suggestedDownloadLink,
            "Suggested version download link should be correctly formatted"
        )

        // Given - Test with non-existent suggested version
        val packageInfoWithInvalidSuggested = FDroidPackageInfo(
            packageName = packageName,
            suggestedVersionCode = 999,
            packages = packages
        )

        // When
        val invalidSuggestedDownloadLink = packageInfoWithInvalidSuggested.getSuggestedVersionDownloadLink()

        // Then
        assertNull(
            invalidSuggestedDownloadLink,
            "Download link should be null for non-existent suggested version code"
        )
    }
}