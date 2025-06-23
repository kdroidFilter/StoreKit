package io.github.kdroidfilter.storekit.apkdownloader.core.service

/**
 * Enum representing the available APK sources.
 */
enum class ApkSource {
    APTOIDE,
    APKCOMBO
}

/**
 * Singleton class for configuring the priority of APK sources.
 * This allows users to specify which sources should be tried first when retrieving APK download links.
 */
object ApkSourcePriority {
    /**
     * The default priority order for APK sources.
     */
    private var priorityOrder: List<ApkSource> = listOf(ApkSource.APTOIDE, ApkSource.APKCOMBO)

    /**
     * Sets the priority order for APK sources.
     * The sources will be tried in the order specified.
     *
     * @param sources The ordered list of APK sources to try
     */
    fun setPriorityOrder(sources: List<ApkSource>) {
        require(sources.isNotEmpty()) { "Priority list cannot be empty" }
        require(sources.toSet().size == sources.size) { "Priority list cannot contain duplicates" }
        require(sources.toSet() == ApkSource.entries.toSet()) { "Priority list must contain all APK sources" }
        
        priorityOrder = sources.toList()
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
        priorityOrder = listOf(ApkSource.APTOIDE, ApkSource.APKCOMBO)
    }
}