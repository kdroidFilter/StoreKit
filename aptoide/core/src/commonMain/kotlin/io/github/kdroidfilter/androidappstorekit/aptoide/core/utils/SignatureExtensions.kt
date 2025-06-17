package io.github.kdroidfilter.androidappstorekit.aptoide.core.utils

import io.github.kdroidfilter.androidappstorekit.aptoide.core.model.AptoideSignature

fun AptoideSignature.normalizeSha1(): String {
    if (this.sha1.isEmpty()) return ""
    // Remove all colons from the traditional format to get the raw hexadecimal string
    return this.sha1.replace(":", "").lowercase()
}

