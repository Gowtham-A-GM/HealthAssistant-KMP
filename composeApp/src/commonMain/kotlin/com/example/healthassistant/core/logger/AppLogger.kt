package com.example.healthassistant.core.logger

object AppLogger {
    fun d(tag: String, message: String) {
        println("[$tag] $message")
    }
}