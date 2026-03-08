package com.example.healthassistant.data.remote.firebase

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class FirebaseVitalsRepositoryImpl : FirebaseVitalsRepository {

    private val VITALS_URL =
        "https://studio-2202668842-5944b-default-rtdb.firebaseio.com/devices/device1/latest/vitals.json"

    // Minimal client — no JWT injection, just JSON
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    override suspend fun fetchLatestVitals(): FirebaseVitalsDto {
        return client.get(VITALS_URL).body()
    }
}
