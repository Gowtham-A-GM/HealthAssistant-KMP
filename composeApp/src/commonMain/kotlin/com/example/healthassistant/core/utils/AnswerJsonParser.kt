package com.example.healthassistant.core.utils

fun extractReadableValue(json: String): String {

    return try {

        if (json.contains("selected_option_label")) {

            Regex("\"selected_option_label\":\"(.*?)\"")
                .find(json)?.groupValues?.get(1) ?: ""

        } else if (json.contains("number_value")) {

            Regex("\"number_value\":(\\d+)")
                .find(json)?.groupValues?.get(1) ?: ""

        } else if (json.contains("\"value\"")) {

            Regex("\"value\":\"(.*?)\"")
                .find(json)?.groupValues?.get(1) ?: ""

        } else ""

    } catch (e: Exception) {
        ""
    }
}