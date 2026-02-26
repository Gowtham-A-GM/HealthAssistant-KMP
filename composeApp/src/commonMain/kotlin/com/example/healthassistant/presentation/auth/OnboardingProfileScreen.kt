package com.example.healthassistant.presentation.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthassistant.presentation.auth.components.QuestionInput
import com.example.healthassistant.presentation.auth.model.QuestionUiModel
import com.example.healthassistant.presentation.auth.questions.ProfileQuestionConfig

@Composable
fun OnboardingProfileScreen(
    viewModel: OnboardingProfileViewModel,
    onProfileCompleted: () -> Unit
) {

    val state = viewModel.state.value

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onProfileCompleted()
            viewModel.resetSuccess()
        }
    }

    val gender = state.answers["q_gender"]

    val allQuestions =
        ProfileQuestionConfig.questions +
                if (gender == "female")
                    ProfileQuestionConfig.femaleConditional
                else emptyList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                "Complete Your Profile",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))
        }

        items(allQuestions) { question ->

            val currentValue =
                viewModel.getValueForQuestion(question.id)

            QuestionInput(
                question = question.copy(value = currentValue),
                onValueChange = {
                    viewModel.onDynamicValueChange(question.id, it)
                }
            )
        }

        item {
            Button(
                onClick = { viewModel.submitProfile() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
        }
    }
}