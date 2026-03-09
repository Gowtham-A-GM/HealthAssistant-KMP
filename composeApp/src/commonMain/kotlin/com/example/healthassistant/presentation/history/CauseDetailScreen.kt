package com.example.healthassistant.presentation.history

import androidx.compose.runtime.Composable
import com.example.healthassistant.domain.model.assessment.PossibleCause
import com.example.healthassistant.presentation.assessment.AssessmentCauseDetailScreen

@Composable
fun CauseDetailScreen(cause: PossibleCause, onBack: () -> Unit) {
    AssessmentCauseDetailScreen(cause = cause, onBack = onBack)
}
