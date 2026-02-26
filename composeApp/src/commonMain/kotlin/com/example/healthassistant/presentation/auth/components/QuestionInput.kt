package com.example.healthassistant.presentation.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthassistant.presentation.auth.model.QuestionUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionInput(
    question: QuestionUiModel,
    onValueChange: (String) -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = question.questionText,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        when (question.type) {

            "text", "number" -> {
                OutlinedTextField(
                    value = question.value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            "single_choice" -> {
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {

                    OutlinedTextField(
                        value = question.value,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        question.options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onValueChange(option)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}