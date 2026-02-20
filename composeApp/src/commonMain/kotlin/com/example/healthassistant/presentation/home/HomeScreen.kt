package com.example.healthassistant.presentation.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.presentation.home.components.CareTipBottomSheet
import com.example.healthassistant.presentation.home.components.EmergencyBottomSheetWrapper

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onEmergencyAction: (EmergencyAction) -> Unit
) {
    val state by viewModel.state.collectAsState()

    var activeSheet by remember { mutableStateOf<HomeSheetType?>(null) }

    var showConfirmationDialog by remember { mutableStateOf(false) }

    var pendingEmergencyAction by remember { mutableStateOf<EmergencyAction?>(null) }


    HomeContent(
        state = state,
        onEvent = { event ->
            when (event) {

                is HomeEvent.QuickHelpClicked -> {
                    when (event.item.title) {
                        "Emergency" -> activeSheet = HomeSheetType.Emergency
                        "Care Tip" -> activeSheet = HomeSheetType.Reminder
                        "Previous Check" -> {
                            viewModel.onEvent(event)
                        }
                    }
                }

                else -> viewModel.onEvent(event)
            }
        }
    )

    // 🔹 Bottom Sheet Controller
    if (activeSheet != null) {

        EmergencyBottomSheetWrapper(
            visible = activeSheet == HomeSheetType.Emergency,
            onDismiss = { activeSheet = null },
            onConfirmAction = { action ->

                // Close bottom sheet
                activeSheet = null

                // Store action
                pendingEmergencyAction = action

                // Show confirmation dialog
                showConfirmationDialog = true
            }
        )
    }

    if (activeSheet == HomeSheetType.Reminder) {

        CareTipBottomSheet(
            tipTitle = "Today's Care Tip",
            tipMessage = "Take a short walk today to refresh your body and mind.",
            reasons = listOf(
                "Improves blood circulation and boosts energy.",
                "Reduces stress and clears your mind.",
                "Helps keep your body active."
            ),
            onSkip = {
                activeSheet = null
            },
            onDone = {
                activeSheet = null
            },
            onDismiss = {
                activeSheet = null
            }
        )
    }

    if (showConfirmationDialog && pendingEmergencyAction != null) {

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false
                        pendingEmergencyAction?.let {
                            AppLogger.d("EMERGENCY", "User confirmed action: $it")
                            onEmergencyAction(it)
                        }
                    }
                ) {
                    Text("YES")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false
                        activeSheet = HomeSheetType.Emergency
                        AppLogger.d("EMERGENCY", "User cancelled confirmation. Reopening sheet.")
                    }
                ) {
                    Text("NO")
                }
            },
            title = {
                Text("Confirm Action")
            },
            text = {
                Text("Are you sure you want to proceed?")
            }
        )
    }

}