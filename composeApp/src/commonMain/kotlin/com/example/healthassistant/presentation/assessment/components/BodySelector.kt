package com.example.healthassistant.presentation.assessment.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BodySelector(
    selectedRegionId: String?,
    onBodyPartClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        BodyMap(
            selectedRegionId = selectedRegionId,
            onBodyPartSelected = onBodyPartClick
        )
    }
}
