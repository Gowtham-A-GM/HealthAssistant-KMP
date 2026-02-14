package com.example.healthassistant

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthassistant.designsystem.HealthAssistantTheme
import com.example.healthassistant.presentation.home.HomeContent
import com.example.healthassistant.presentation.home.HomeState

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun HomePreview() {

    HealthAssistantTheme {
        HomeContent(
            state = HomeState(
                userName = "Gowtham"
            ),
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
