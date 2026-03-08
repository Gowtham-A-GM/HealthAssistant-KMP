package com.example.healthassistant.data.remote.firebase

interface FirebaseVitalsRepository {
    suspend fun fetchLatestVitals(): FirebaseVitalsDto
}
