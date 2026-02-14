package com.example.healthassistant.presentation.home

import androidx.compose.runtime.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsState()

    HomeContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

