package com.example.healthassistant.presentation.assessment.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import com.example.healthassistant.core.bodymap.BodyMapProvider
import com.example.healthassistant.designsystem.AppColors

// Original SVG viewport dimensions
private const val SVG_WIDTH = 596f
private const val SVG_HEIGHT = 1137f

@Composable
fun BodyMap(
    selectedRegionId: String?,
    onBodyPartSelected: (String) -> Unit
) {
    val regions = remember { BodyMapProvider.loadRegions() }

    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    if (canvasWidth == 0f || canvasHeight == 0f) return@detectTapGestures

                    val scale = minOf(canvasWidth / SVG_WIDTH, canvasHeight / SVG_HEIGHT)
                    val offsetX = (canvasWidth - SVG_WIDTH * scale) / 2f
                    val offsetY = (canvasHeight - SVG_HEIGHT * scale) / 2f

                    // Convert tap to SVG coordinate space
                    val svgX = (tap.x - offsetX) / scale
                    val svgY = (tap.y - offsetY) / scale

                    regions.forEach { region ->
                        val bounds = region.path.getBounds()
                        if (
                            svgX >= bounds.left &&
                            svgX <= bounds.right &&
                            svgY >= bounds.top &&
                            svgY <= bounds.bottom
                        ) {
                            onBodyPartSelected(region.id)
                        }
                    }
                }
            }
    ) {
        canvasWidth = size.width
        canvasHeight = size.height

        val scale = minOf(size.width / SVG_WIDTH, size.height / SVG_HEIGHT)
        val offsetX = (size.width - SVG_WIDTH * scale) / 2f
        val offsetY = (size.height - SVG_HEIGHT * scale) / 2f

        withTransform({
            translate(left = offsetX, top = offsetY)
            scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero)
        }) {
            regions.forEach { region ->
                val isSelected = region.id == selectedRegionId
                val fillColor = if (isSelected)
                    AppColors.darkBlue
                else
                    AppColors.blue.copy(alpha = 0.35f)

                drawPath(
                    path = region.path,
                    color = fillColor,
                    style = Fill
                )
                drawPath(
                    path = region.path,
                    color = Color.White.copy(alpha = 0.7f),
                    style = Stroke(width = 1.8f)
                )
            }
        }
    }
}
