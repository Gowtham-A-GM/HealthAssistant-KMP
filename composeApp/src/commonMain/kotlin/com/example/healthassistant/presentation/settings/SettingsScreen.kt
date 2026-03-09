package com.example.healthassistant.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.healthassistant.core.utils.LanguageState
import com.example.healthassistant.core.utils.t

data class SettingsItem(
    val title: String,
    val subtitle: String = ""
)

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onProfileClick: () -> Unit,
    onMedicalClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    val currentLang = LanguageState.currentLanguageName()

    val items = listOf(
        SettingsItem("Profile Data"),
        SettingsItem("Medical Data"),
        SettingsItem("Language", currentLang),
        SettingsItem("Logout")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = t("Settings"),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {

            items(items) { item ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            when (item.title) {

                                "Profile Data" -> onProfileClick()

                                "Medical Data" -> onMedicalClick()

                                "Language" -> onLanguageClick()

                                "Logout" -> onLogoutClick()
                            }
                        }
                        .padding(vertical = 16.dp)
                ) {
                    Column {
                        Text(text = t(item.title))
                        if (item.subtitle.isNotEmpty()) {
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Divider()
            }
        }
    }
}