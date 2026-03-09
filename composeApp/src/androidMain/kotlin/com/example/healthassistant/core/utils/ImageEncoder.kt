package com.example.healthassistant.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
actual fun compressAndEncodeProfileImage(bytes: ByteArray): String {
    // Decode to Bitmap
    val original = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        ?: return Base64.encode(bytes) // fallback: no compression

    // Scale down to max 256x256 while preserving aspect ratio
    val maxDim = 256
    val scale = minOf(maxDim.toFloat() / original.width, maxDim.toFloat() / original.height, 1f)
    val targetW = (original.width * scale).toInt().coerceAtLeast(1)
    val targetH = (original.height * scale).toInt().coerceAtLeast(1)
    val scaled = if (scale < 1f) Bitmap.createScaledBitmap(original, targetW, targetH, true) else original

    // Compress as JPEG, reducing quality until under 200 KB
    val stream = ByteArrayOutputStream()
    var quality = 80
    do {
        stream.reset()
        scaled.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        quality -= 10
    } while (stream.size() > 200 * 1024 && quality > 10)

    if (scaled !== original) scaled.recycle()

    return Base64.encode(stream.toByteArray())
}
