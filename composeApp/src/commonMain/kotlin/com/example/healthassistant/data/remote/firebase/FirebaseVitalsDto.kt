package com.example.healthassistant.data.remote.firebase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FirebaseVitalsDto(
    @SerialName("heart_rate") val heartRate: Double? = null,
    @SerialName("spo2") val spo2: Double? = null,
    @SerialName("temperature") val temperature: Double? = null
)
