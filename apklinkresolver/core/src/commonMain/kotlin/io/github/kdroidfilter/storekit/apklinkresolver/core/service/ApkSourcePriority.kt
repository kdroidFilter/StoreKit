package io.github.kdroidfilter.storekit.apklinkresolver.core.service

/**
 * Enum representing the available APK sources.
 */
enum class ApkSource {
    APKPURE,
    APTOIDE,
    APKCOMBO,
    FDROID,
}

/**
 * Singleton class for configuring the priority of APK sources.
 * This allows users to specify which sources should be tried first when retrieving APK download links.
 */
object ApkSourcePriority {
    /**
     * The default priority order for APK sources.
     */
    private val defaultOrder: List<ApkSource> = listOf(ApkSource.APKPURE, ApkSource.APKCOMBO, ApkSource.FDROID, ApkSource.APTOIDE)
    private var priorityOrder: List<ApkSource> = defaultOrder

    /**
     * Sets the priority order for APK sources.
     * The sources will be tried in the order specified.
     *
     * @param sources The ordered list of APK sources to try
     */
    fun setPriorityOrder(sources: List<ApkSource>) {
        require(sources.isNotEmpty()) { "Priority list cannot be empty" }
        require(sources.toSet().size == sources.size) { "Priority list cannot contain duplicates" }
        // Allow partial ordering; unspecified sources will be tried after in their default order
                // Previously required all sources; relaxing to support dynamic additions like APKPURE
                // No strict check here beyond non-empty and no duplicates.

        // Merge with default order to append unspecified sources at the end in their default order
        val remaining = defaultOrder.filterNot { sources.contains(it) }
        priorityOrder = sources.toList() + remaining
    }

    /**
     * Gets the current priority order for APK sources.
     *
     * @return The ordered list of APK sources
     */
    fun getPriorityOrder(): List<ApkSource> = priorityOrder.toList()

    /**
     * Resets the priority order to the default.
     */
    fun resetToDefault() {
        priorityOrder = defaultOrder
    }
}
