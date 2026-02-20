package com.example.healthassistant.core.logger

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object AppLogger {

    private val prettyJson = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun d(tag: String, message: String) {
        println("[$tag] $message")
    }

    fun logJson(tag: String, label: String, obj: Any) {
        try {
            val json = prettyJson.encodeToString(obj)
            println("[$tag] $label →\n$json")
        } catch (e: Exception) {
            println("[$tag] Failed to log JSON → ${e.message}")
        }
    }
}