package com.example.healthassistant.presentation.assessment.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

object NetworkClient {

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
}
