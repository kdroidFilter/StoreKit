package com.kdroid.gplayscrapper.utils

import com.kdroid.gplayscrapper.constants.keyRegex
import com.kdroid.gplayscrapper.constants.valueRegex
import com.kdroid.gplayscrapper.utils.JsonExtensions.jsonParser
import com.kdroid.gplayscrapper.utils.NetworkUtils.logger
import kotlinx.serialization.json.JsonElement

/**
 * Parses datasets from a list of script strings by extracting keys and corresponding JSON elements.
 *
 * @param scripts A list of strings, each representing a script containing potential JSON data.
 * @return A map where the keys are extracted keys from the scripts, and the values are the parsed JSON elements.
 */
// Parse datasets from extracted scripts
internal fun parseDataSetsFromScripts(scripts: List<String>): Map<String, JsonElement> {
    return scripts.mapNotNull { script ->
        val keyMatch = keyRegex.find(script)
        val valueMatch = valueRegex.find(script)

        if (keyMatch != null && valueMatch != null) {
            val key = keyMatch.groupValues[1]
            val jsonStr = valueMatch.groupValues[1]
            try {
                val jsonElement = jsonParser.parseToJsonElement(jsonStr)
                key to jsonElement
            } catch (e: Exception) {
                logger.error(e) { "Failed to parse JSON" }
                null
            }
        } else null
    }.toMap()
}