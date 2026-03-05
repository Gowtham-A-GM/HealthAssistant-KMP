package com.example.healthassistant.presentation.assessment.visual

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector


import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.core.utils.t
import com.example.healthassistant.domain.model.assessment.ResponseOption
import com.example.healthassistant.presentation.assessment.AssessmentEvent
import com.example.healthassistant.presentation.assessment.AssessmentState
import com.example.healthassistant.presentation.assessment.components.AssessmentTopHeader
import com.example.healthassistant.presentation.assessment.components.AvatarSection
import com.example.healthassistant.presentation.assessment.components.BottomAssessmentControls
import com.example.healthassistant.presentation.assessment.components.GradientOptionButton
import com.example.healthassistant.presentation.assessment.components.GradientTextInput
import com.example.healthassistant.presentation.assessment.components.QuestionSection


@Composable
fun VisualBodySelector(
    state: AssessmentState,
    onEvent: (AssessmentEvent) -> Unit
) {
    val stack = state.visualNavigationStack

    if (!state.isVisualModeActive) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            if (stack.isNotEmpty()) {
                Text(
                    text = "Back",
                    modifier = Modifier.clickable {
                        onEvent(AssessmentEvent.VisualBackPressed)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            val currentLevel = resolveCurrentLevel(stack)

            currentLevel.forEach { item ->

                Button(
                    onClick = {
                        if (item is BodyPart && item.subParts.isNotEmpty()) {
                            onEvent(
                                AssessmentEvent.BodyPartSelected(item.id)
                            )
                        } else if (item is BodyPart && item.symptoms.isNotEmpty()) {
                            onEvent(
                                AssessmentEvent.BodyPartSelected(item.id)
                            )
                        } else if (item is VisualSymptom) {
                            onEvent(
                                AssessmentEvent.VisualSymptomSelected(
                                    bodyPath = stack,
                                    symptomId = item.id,
                                    symptomLabel = item.label
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        when (item) {
                            is BodyPart -> item.label
                            is VisualSymptom -> item.label
                            else -> ""
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

fun resolveCurrentLevel(stack: List<String>): List<Any> {
    var currentParts = BodyStructureProvider.bodyParts
    var currentPart: BodyPart? = null

    for (id in stack) {
        currentPart = currentParts.find { it.id == id }
        currentParts = currentPart?.subParts ?: emptyList()
    }

    return if (currentPart == null) {
        BodyStructureProvider.bodyParts
    } else {
        if (currentPart.subParts.isNotEmpty())
            currentPart.subParts
        else
            currentPart.symptoms
    }
}