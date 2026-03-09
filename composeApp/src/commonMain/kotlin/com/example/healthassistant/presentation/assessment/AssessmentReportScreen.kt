package com.example.healthassistant.presentation.assessment

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.healthassistant.core.platform.PlatformBackHandler
import com.example.healthassistant.core.utils.t
import com.example.healthassistant.domain.model.assessment.PossibleCause
import com.example.healthassistant.domain.model.assessment.Report
import com.example.healthassistant.presentation.history.Bullet
import com.example.healthassistant.presentation.history.CauseItemWithProgress
import com.example.healthassistant.presentation.history.SectionTitle
import com.example.healthassistant.presentation.history.formatReportDateTime
import com.example.healthassistant.presentation.history.urgencyColor
import com.example.healthassistant.presentation.history.urgencyDisplayText

@Composable
fun AssessmentReportScreen(
    report: Report,
    onBack: () -> Unit,
    onCauseClick: (PossibleCause) -> Unit,
    onGoHome: () -> Unit,
    onAskChatbot: () -> Unit,
    onDownloadPdf: (Report) -> Unit = {}
) {
    val (dateStr, timeStr) = formatReportDateTime(report.generatedAt)
    var showExitDialog by remember { mutableStateOf(false) }

    PlatformBackHandler { showExitDialog = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = t("Assessment Report"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = dateStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                Text(text = timeStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(4.dp))
            report.patientInfo?.let {
                Text(
                    text = t("${it.name}, ${it.gender}, ${it.age} years old"),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = t("Primary Symptom: ${report.topic}"),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = urgencyDisplayText(report.urgencyLevel),
                    color = urgencyColor(report.urgencyLevel),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(24.dp))
            SectionTitle(t("Summary"))
            report.summary.forEach { Bullet(it) }
            Spacer(Modifier.height(20.dp))
            SectionTitle(t("Possible Causes"))
            report.possibleCauses.forEachIndexed { i, cause ->
                CauseItemWithProgress(index = i + 1, cause = cause, onTellMore = { onCauseClick(cause) })
            }
            Spacer(Modifier.height(20.dp))
            SectionTitle(t("What you were advised"))
            report.advice.forEach { Bullet(it) }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { showExitDialog = true },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(t("Go to Home Screen"))
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { onDownloadPdf(report) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(t("Download Health Report"))
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onAskChatbot,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(t("Ask Remy Chatbot"))
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(t("Exit Assessment?")) },
            text = { Text(t("Are you sure you want to exit? This will end your assessment session.")) },
            confirmButton = {
                TextButton(onClick = { showExitDialog = false; onGoHome() }) {
                    Text(t("Yes, Exit"), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(t("Stay"))
                }
            }
        )
    }
}
