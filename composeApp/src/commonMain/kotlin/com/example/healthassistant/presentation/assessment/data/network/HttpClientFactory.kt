package com.example.healthassistant.presentation.assessment.data.network

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient
