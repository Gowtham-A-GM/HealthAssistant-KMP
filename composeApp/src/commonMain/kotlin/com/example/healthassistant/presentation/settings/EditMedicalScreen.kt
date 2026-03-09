package com.example.healthassistant.presentation.settings

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
fun EditMedicalScreen(
    viewModel: EditMedicalViewModel,
    onBack: () -> Unit
) {

    val state = viewModel.state.value
    LaunchedEffect(Unit) {
        viewModel.reload()
    }

    LaunchedEffect(state.isSuccess) {

        if (state.isSuccess) {
            onBack()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            Text(
                text = "Edit Medical Data",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))
        }

        items(MedicalQuestionConfig.questions.filter { question ->
            // Hide follow-up questions when their parent answer is "No"
            when (question.id) {
                "q_condition_details" -> state.answers["q_past_conditions"] == "Yes"
                "q_surgery_details"   -> state.answers["q_surgeries"] == "Yes"
                "q_medication_details" -> state.answers["q_current_medication"] == "Yes"
                "q_allergy_details"   -> state.answers["q_allergies"] == "Yes"
                else -> true
            }
        }) { question ->

            val value = (state.answers[question.id] ?: "").let {
                if (it == "Not Applicable") "" else it
            }

            QuestionInput(
                question = question.copy(value = value),
                onValueChange = {
                    viewModel.updateAnswer(question.id, it)
                },
                isRequiredError = false
            )

            Spacer(Modifier.height(12.dp))
        }

        item {

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.saveMedical() },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Save")
            }
        }
    }
}