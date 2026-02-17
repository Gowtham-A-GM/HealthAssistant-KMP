package com.example.healthassistant.presentation.assessment


import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign


import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.domain.model.assessment.ResponseOption
import com.example.healthassistant.presentation.assessment.model.AssessmentPhase
import healthassistant.composeapp.generated.resources.Res
import healthassistant.composeapp.generated.resources.img_avatar
import org.jetbrains.compose.resources.painterResource
import healthassistant.composeapp.generated.resources.img_user_avatar


@Composable
fun AssessmentScreen(
    viewModel: AssessmentViewModel,
    onExit: () -> Unit,
    onReportGenerated: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.report) {
        if (state.report != null) {
            onReportGenerated()
        }
    }



    LaunchedEffect(key1 = state.sessionId) {
        if (state.sessionId.isEmpty()) {
            AppLogger.d("UI", "AssessmentScreen launched → calling startAssessment()")
            viewModel.startAssessment()
        }
    }



    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {

            if (state.isGeneratingReport) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            // 🔹 Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp) // small safe space
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (state.isLoading) {
                    Spacer(modifier = Modifier.height(120.dp))
                    CircularProgressIndicator()
                    return@Column
                }

                // ───────── TOP BAR ─────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "New Assessment",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Avatar
                Box(contentAlignment = Alignment.Center) {

                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                CircleShape
                            )
                    )

                    Image(
                        painter = painterResource(Res.drawable.img_avatar),
                        contentDescription = "Assessment Avatar",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                    )
                }


                Spacer(modifier = Modifier.height(32.dp))

                state.currentQuestion?.let { question ->

                    Text(
                        text = question.text,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    when (question.responseType) {

                        // 🔢 NUMBER OR TEXT INPUT
                        "number", "text" -> {

                            OutlinedTextField(
                                value = state.typedText,
                                onValueChange = {
                                    viewModel.onEvent(
                                        AssessmentEvent.TextChanged(it)
                                    )
                                },
                                placeholder = { Text("Type your answer") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp),
                                shape = RoundedCornerShape(20.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

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
                                Text("Submit")
                            }
                        }

                        // 🔘 SINGLE / MULTI CHOICE
                        "single_choice", "multi_choice" -> {

                            val options = question.responseOptions ?: emptyList()

                            if (options.size <= 4) {
                                // 🔘 Show as buttons
                                options.forEach { option ->

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
                                // ⬇️ Show as dropdown
                                OptionDropdown(
                                    options = options,
                                    onOptionSelected = { selectedId ->
                                        viewModel.onEvent(
                                            AssessmentEvent.OptionSelected(selectedId)
                                        )
                                    }
                                )
                            }
                        }

                    }
                }
            }

            // 🔹 FIXED Bottom Bar
            BottomAssessmentControls(
                isMuted = state.isMuted,
                onMicClick = {
                    viewModel.onEvent(AssessmentEvent.MicClicked)
                },
                onVolumeClick = {
                    viewModel.onEvent(AssessmentEvent.VolumeClicked)
                },
                onExit = {
                    viewModel.onEvent(AssessmentEvent.ExitClicked)
                    onExit()
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionDropdown(
    options: List<ResponseOption>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("Select an option") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {

        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select an option") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp)   // 👈 IMPORTANT
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        selectedLabel = option.label
                        expanded = false
                        onOptionSelected(option.id)
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}




@Composable
fun BottomAssessmentControls(
    isMuted: Boolean,
    onMicClick: () -> Unit,
    onVolumeClick: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
)

{
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(16.dp),
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

                // 🎤 MIC (STT)
                ActionIcon(
                    icon = Icons.Default.Mic,
                    onClick = onMicClick
                )

                // 🔊 VOLUME (TTS)
                ActionIcon(
                    icon = if (isMuted)
                        Icons.Default.VolumeOff
                    else
                        Icons.Default.VolumeUp,
                    onClick = onVolumeClick
                )

                // ⋮ More
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
