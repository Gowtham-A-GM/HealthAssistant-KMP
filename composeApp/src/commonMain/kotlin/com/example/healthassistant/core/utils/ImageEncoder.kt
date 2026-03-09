package com.example.healthassistant.core.utils

/**
 * Compresses raw image bytes down to ≤200 KB and returns a base64 string.
 * Small enough for SQLite cursor window (< 2 MB limit on Android).
 */
expect fun compressAndEncodeProfileImage(bytes: ByteArray): String