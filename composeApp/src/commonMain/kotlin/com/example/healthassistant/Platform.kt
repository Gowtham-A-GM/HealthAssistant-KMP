package com.example.healthassistant

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform