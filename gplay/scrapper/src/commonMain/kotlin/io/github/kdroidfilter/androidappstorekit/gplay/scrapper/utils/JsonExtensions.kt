package io.github.kdroidfilter.androidappstorekit.gplay.scrapper.utils

import kotlinx.serialization.json.*

/**
 * Provides utility functions and extensions for working with JSON data using Kotlin serialization.
 * This object contains parsing capabilities and helper functions to safely interact with JSON elements.
 *
 * Properties:
 * - jsonParser: A Json instance to handle JSON serialization and deserialization with options
 *   to ignore unknown keys.
 *
 * Functions:
 * - JsonElement?.asStringOrNull: Extension function to safely retrieve a JSON element as a nullable String.
 * - JsonElement?.asLongOrNull: Extension function to safely retrieve a JSON element as a nullable Long.
 * - JsonElement?.asDoubleOrNull: Extension function to safely retrieve a JSON element as a nullable Double.
 * - jsonElementToBool: Converts a JSON element to a Boolean based on its numeric value, treating 0 as false and others as true.
 * - microsToPrice: Converts a micro-unit value stored in a JSON element to a double representing a price.
 * - nestedLookup: Navigates through a JSON structure using a sequence of indices, resolving keys as strings and array positions.
 */
internal object JsonExtensions {

    // JSON parser
     val jsonParser = Json { ignoreUnknownKeys = true }

    // Helper Extensions
    fun JsonElement?.asStringOrNull(): String? = (this as? JsonPrimitive)?.contentOrNull
    fun JsonElement?.asLongOrNull(): Long? = (this as? JsonPrimitive)?.longOrNull
    fun JsonElement?.asDoubleOrNull(): Double? = (this as? JsonPrimitive)?.doubleOrNull

    fun jsonElementToBool(e: JsonElement?): Boolean {
        // In Python code, bool is determined by direct casting. Often s == 0 => false else true
        val v = e?.asLongOrNull() ?: return false
        return v != 0L
    }

    fun microsToPrice(e: JsonElement?): Double {
        val v = e?.asLongOrNull() ?: return 0.0
        return v / 1000000.0
    }

    /**
     * Safely navigates into a Json structure based on a list of indexes.
     * Each index tries to access either a JsonArray index or a JsonObject key (converted to string).
     */
    fun nestedLookup(source: JsonElement?, indexes: List<Int>): JsonElement? {
        var current: JsonElement? = source
        for (p in indexes) {
            current = when (current) {
                is JsonArray -> if (p < current.size) current[p] else null
                is JsonObject -> current[p.toString()]
                else -> null
            }
            if (current == null) return null
        }
        return current
    }
}