package com.example.healthassistant.presentation.assessment


import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.designsystem.AppColors
import com.example.healthassistant.designsystem.AppTypography
import com.example.healthassistant.domain.model.assessment.ResponseOption
import com.example.healthassistant.presentation.assessment.components.AssessmentTopHeader
import com.example.healthassistant.presentation.assessment.components.AvatarSection
import com.example.healthassistant.presentation.assessment.components.BottomAssessmentControls
import com.example.healthassistant.presentation.assessment.components.GradientOptionButton
import com.example.healthassistant.presentation.assessment.components.GradientTextInput
import com.example.healthassistant.presentation.assessment.components.QuestionSection
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
    var showExitDialog by remember { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(state.report) {
        if (state.report != null) {
            onReportGenerated()
        }
    }

    LaunchedEffect(state.sessionId) {
        if (state.sessionId.isEmpty()) {
            AppLogger.d("UI", "AssessmentScreen launched → calling startAssessment()")
            viewModel.startAssessment()
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("End Assessment") },
            text = { Text("Assessment will be ended. Do you want to continue?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        viewModel.endAssessment { onExit() }
                    }
                ) {
                    Text("YES", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }


    LaunchedEffect(state.isLoading, state.isGeneratingReport) {

        if (state.isLoading || state.isGeneratingReport) {

            // Wait 3 seconds before showing loader
            kotlinx.coroutines.delay(1000)

            // If still loading after delay → show
            if (state.isLoading || state.isGeneratingReport) {
                showLoading = true
            }

        } else {
            // Immediately hide when loading finishes
            showLoading = false
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AssessmentTopHeader(
                    onClose = { showExitDialog = true }
                )

                Spacer(modifier = Modifier.height(32.dp))

                AvatarSection(
                    isMuted = state.isMuted,
                    isSpeaking = !state.isMuted
                )

                Spacer(modifier = Modifier.height(32.dp))

                state.currentQuestion?.let { question ->

                    QuestionSection(text = question.text)

                    Spacer(modifier = Modifier.height(28.dp))

                    when (question.responseType) {

                        "text", "number" -> {

                            GradientTextInput(
                                value = state.typedText,
                                onValueChange = {
                                    viewModel.onEvent(
                                        AssessmentEvent.TextChanged(it)
                                    )
                                },
                                onSubmit = {
                                    viewModel.onEvent(
                                        AssessmentEvent.SendText
                                    )
                                }
                            )
                        }

                        "single_choice", "multi_choice" -> {

                            val options =
                                question.responseOptions ?: emptyList()

                            if (options.size <= 4) {

                                options.forEach { option ->

                                    GradientOptionButton(
                                        text = option.label,
                                        onClick = {
                                            viewModel.onEvent(
                                                AssessmentEvent.OptionSelected(option.id)
                                            )
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
                                }

                            } else {

                                StyledDropdown(
                                    options = options,
                                    questionId = question.id,
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

                Spacer(modifier = Modifier.weight(1f))

            }

            AnimatedVisibility(
                visible = showLoading
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.6f)
                        )
                        .pointerInput(Unit) {},
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (state.isGeneratingReport)
                                "Generating your medical report..."
                            else
                                "Preparing next question..."
                        )
                    }
                }
            }

            BottomAssessmentControls(
                isMuted = state.isMuted,
                isMicOn = state.isListening, // or your mic state
                onMicClick = {
                    viewModel.onEvent(AssessmentEvent.MicClicked)
                },
                onVolumeClick = {
                    viewModel.onEvent(AssessmentEvent.VolumeClicked)
                },
                onExit = { showExitDialog = true },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledDropdown(
    options: List<ResponseOption>,
    questionId: String,
    onOptionSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember(questionId) {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    if (selectedLabel.isEmpty())
                        "Select an option"
                    else
                        selectedLabel
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF5A8DEE),
                unfocusedBorderColor = Color(0xFFB0C4FF)
            )
        )

        // 👇 Invisible click layer over entire field
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp)
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
