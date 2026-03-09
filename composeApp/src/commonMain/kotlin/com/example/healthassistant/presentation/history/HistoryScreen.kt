package com.example.healthassistant.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.healthassistant.domain.model.assessment.Report

@Composable
fun HistoryScreen(
    reports: List<Report>,
    onItemClick: (Report) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)

        if (reports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No assessment reports yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Box(
                    modifier = Modifier.width(2.dp).fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Spacer(Modifier.height(4.dp))
                    reports.forEach { report ->
                        val (dateStr, timeStr) = formatReportDateTime(report.generatedAt)
                        HistoryItem(
                            date = dateStr,
                            title = report.topic,
                            time = timeStr,
                            status = urgencyToHistoryStatus(report.urgencyLevel),
                            onClick = { onItemClick(report) }
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
