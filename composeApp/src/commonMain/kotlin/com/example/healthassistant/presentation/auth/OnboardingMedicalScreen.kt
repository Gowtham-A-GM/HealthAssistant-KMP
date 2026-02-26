package com.example.healthassistant.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthassistant.presentation.auth.components.QuestionInput
import com.example.healthassistant.presentation.auth.questions.MedicalQuestionConfig

@Composable
fun OnboardingMedicalScreen(
    viewModel: OnboardingMedicalViewModel,
    onMedicalCompleted: () -> Unit
) {

    val state = viewModel.state.value

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onMedicalCompleted()
            viewModel.resetSuccess()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                "Medical Information",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))
        }

        items(MedicalQuestionConfig.questions) { baseQuestion ->

            val currentValue = viewModel.getValueForQuestion(baseQuestion.id)

            QuestionInput(
                question = baseQuestion.copy(value = currentValue),
                onValueChange = { value ->
                    viewModel.onDynamicValueChange(baseQuestion.id, value)
                }
            )
        }

        item {
            Button(
                onClick = { viewModel.submitMedical() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
        }
    }
}