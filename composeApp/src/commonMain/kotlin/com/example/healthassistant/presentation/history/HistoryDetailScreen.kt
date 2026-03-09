package com.example.healthassistant.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.healthassistant.core.utils.t
import com.example.healthassistant.domain.model.assessment.PossibleCause
import com.example.healthassistant.domain.model.assessment.Report
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// ─── Screen ───

@Composable
fun HistoryDetailScreen(
    report: Report,
    onBack: () -> Unit,
    onCauseClick: (PossibleCause) -> Unit,
    onDownloadPdf: (Report) -> Unit = {}
) {
    val (dateStr, timeStr) = formatReportDateTime(report.generatedAt)
    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "←", modifier = Modifier.clickable { onBack() }, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(12.dp))
            Text(text = t(report.topic), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
            Spacer(Modifier.height(20.dp))
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
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = { onDownloadPdf(report) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(t("Download Health Report"))
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─── Shared date/urgency utils ───

fun formatReportDateTime(isoString: String): Pair<String, String> {
    return try {
        val instant = Instant.parse(isoString)
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val date = "${local.dayOfMonth} ${months[local.monthNumber - 1]} ${local.year}"
        val hour = local.hour
        val minute = local.minute
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        Pair(date, "$displayHour:${minute.toString().padStart(2, '0')} $amPm")
    } catch (_: Exception) {
        Pair(isoString.take(10), "")
    }
}

fun urgencyDisplayText(level: String): String = when (level) {
    "emergency", "red_emergency" -> "EMERGENCY"
    "yellow_doctor_visit", "doctor_visit_recommended" -> "DOCTOR VISIT"
    "green_home_care", "self_care" -> "SELF CARE"
    else -> level.replace("_", " ").uppercase()
}

fun urgencyColor(level: String): Color = when (level) {
    "emergency", "red_emergency" -> Color.Red
    "yellow_doctor_visit", "doctor_visit_recommended" -> Color(0xFFFFA000)
    else -> Color(0xFF2E7D32)
}

// ─── Shared composables ───

@Composable
fun SectionTitle(text: String) {
    Text(text = text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun Bullet(text: String) {
    Row(modifier = Modifier.padding(bottom = 6.dp)) {
        Text("• ")
        Text(text = t(text), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun CauseItem(title: String, description: String, onTellMore: () -> Unit) {
    Text(text = t(title), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = t(description), style = MaterialTheme.typography.bodySmall)
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Button(
            onClick = onTellMore,
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(t("Tell me more"), style = MaterialTheme.typography.bodySmall)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CauseItemWithProgress(index: Int, cause: PossibleCause, onTellMore: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$index. ${t(cause.title)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        cause.subtitle?.let { sub ->
            Spacer(Modifier.height(2.dp))
            Text(text = t(sub), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(4.dp))
        Text(text = t(cause.shortDescription), style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        val outOf10 = (cause.detail.percentage / 10).coerceIn(0, 10)
        Text(
            text = "$outOf10 out of 10 has this",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { cause.detail.percentage / 100f },
                modifier = Modifier.weight(1f).height(6.dp),
                color = when (cause.severity) {
                    "severe" -> Color.Red
                    "moderate" -> Color(0xFFFFA000)
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = onTellMore,
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(t("Tell me more"), style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun PersonDot(active: Boolean) {
    Box(
        modifier = Modifier.size(16.dp).background(
            color = if (active) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            shape = MaterialTheme.shapes.small
        )
    )
}
