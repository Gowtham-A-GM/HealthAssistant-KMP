package com.example.healthassistant.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object NetworkClient {

    val httpClient = HttpClient {

        // 🔍 NETWORK LOGGING
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        // 🔥 JSON CONFIG
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                }
            )
        }
    }
}
