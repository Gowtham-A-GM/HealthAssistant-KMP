package com.example.healthassistant.presentation.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // ───── Header ─────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Health News",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Divider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )

        // ───── News List ─────
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            NewsItem(
                description = "Health officials have reported an increase in fever-related illnesses in several areas. People are advised to monitor symptoms and take basic precautions.",
                postedTime = "10 hours"
            )

            NewsItem(
                description = "Medical experts say that dehydration is a common cause of headaches and weakness during summer. Drinking enough water is essential for overall health.",
                postedTime = "1 day"
            )
        }
    }
}
