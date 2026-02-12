package com.example.healthassistant.presentation.assessment


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.graphics.Color

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign


import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase


@Composable
fun AssessmentScreen(
    viewModel: AssessmentViewModel,
    onExit: () -> Unit,
    onReportGenerated: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    // 🔥 HARD EXIT — DO NOT RENDER ANY UI
    if (state.phase == AssessmentPhase.REPORT) {
        return
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.phase) {
        if (state.phase == AssessmentPhase.REPORT) {
            onReportGenerated()
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(paddingValues)
                .statusBarsPadding()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ───── Top Bar ─────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Step ${state.step} of ${state.totalSteps}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

            }

            Spacer(modifier = Modifier.height(40.dp))

            // ───── Illustration Placeholder ─────
            Box(
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ───── Question ─────
            when (state.phase) {

                AssessmentPhase.CHOOSE_USER -> {
                    ChooseUserScreen(
                        onMyself = {
                            viewModel.onEvent(AssessmentEvent.MyselfSelected)
                        },
                        onSomeoneElse = {
                            viewModel.onEvent(AssessmentEvent.SomeoneElseSelected)
                        }
                    )
                }

                AssessmentPhase.INIT -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Setting things up…",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }


                AssessmentPhase.PREDEFINED -> {

                    Text(
                        text = state.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (state.options.isNotEmpty()) {
                        // 🔘 BUTTON QUESTIONS
                        state.options.forEach { option ->
                            Button(
                                onClick = {
                                    viewModel.onEvent(
                                        AssessmentEvent.OptionSelected(option.id)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 40.dp)
                                    .height(52.dp),
                                shape = RoundedCornerShape(26.dp)
                            ) {
                                Text(option.label)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    } else {
                        // ✍️ TEXT QUESTIONS

                        OutlinedTextField(
                            value = state.typedText,
                            onValueChange = {
                                viewModel.onEvent(
                                    AssessmentEvent.TextChanged(it)
                                )
                            },
                            placeholder = {
                                Text("Type your answer here")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            shape = RoundedCornerShape(20.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.onEvent(AssessmentEvent.SendText)
                            },
                            enabled = state.typedText.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Send")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You can also answer using voice 🎤",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }


                AssessmentPhase.LLM -> {

                    // 🧠 Assistant message
                    state.assistantMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // 📝 Input box for user response
                    OutlinedTextField(
                        value = state.typedText,
                        onValueChange = {
                            viewModel.onEvent(AssessmentEvent.TextChanged(it))
                        },
                        placeholder = {
                            Text("Type your response here")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.onEvent(AssessmentEvent.SendText)
                        },
                        enabled = state.typedText.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Send")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You can also answer using voice 🎤",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }




                AssessmentPhase.REPORT -> {

                    // Intentionally empty.
                    // Navigation is handled by LaunchedEffect
                }

                AssessmentPhase.END -> {

                    Text(
                        text = "Assessment completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                else -> {
                    // INIT or unknown
                    CircularProgressIndicator()
                }
            }



            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (state.recognizedSpeech.isNotBlank()) {
                    Text(
                        text = state.recognizedSpeech,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                BottomAssessmentControls(
                    onMicClick = {
                        viewModel.onEvent(AssessmentEvent.MicClicked)
                    },
                    onExit = {
                        viewModel.onEvent(AssessmentEvent.ExitClicked)
                    }
                )

            }


        }
    }
}

@Composable
fun BottomAssessmentControls(
    onMicClick: () -> Unit,
    onExit: () -> Unit
)
{
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionIcon(Icons.Default.Videocam)
                ActionIcon(
                    icon = Icons.Default.Mic,
                    onClick = onMicClick
                )

                ActionIcon(Icons.Default.VolumeUp)
                ActionIcon(Icons.Default.MoreVert)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Exit button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(Color.Red, CircleShape)
                    .clickable { onExit() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
private fun ActionIcon(
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ChooseUserScreen(
    onMyself: () -> Unit,
    onSomeoneElse: () -> Unit
) {
    Column {
        Text("For whom are you taking this assessment?")

        Button(onClick = onMyself) {
            Text("Myself")
        }

        Button(onClick = onSomeoneElse) {
            Text("Someone else")
        }
    }
}
