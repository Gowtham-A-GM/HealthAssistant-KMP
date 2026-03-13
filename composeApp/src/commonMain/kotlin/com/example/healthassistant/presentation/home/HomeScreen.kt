package com.example.healthassistant.presentation.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import com.example.healthassistant.core.logger.AppLogger
import com.example.healthassistant.core.utils.t
import com.example.healthassistant.presentation.home.components.CareTipBottomSheet
import com.example.healthassistant.presentation.home.components.EmergencyBottomSheetWrapper
import kotlin.collections.listOf

// ── 10 rotating health tips ────────────────────────────────────────────────
private data class CareTip(
    val title: String,
    val message: String,
    val reasons: List<String>
)

private val careTips = listOf(
    CareTip(
        title = "Take a Short Walk",
        message = "Take a short walk today to refresh your body and mind.",
        reasons = listOf(
            "Improves blood circulation and boosts energy.",
            "Reduces stress and clears your mind.",
            "Helps keep your body active."
        )
    ),
    CareTip(
        title = "Drink More Water",
        message = "Stay hydrated by drinking at least 8 glasses of water today.",
        reasons = listOf(
            "Flushes toxins and supports kidney health.",
            "Keeps your skin clear and glowing.",
            "Improves concentration and reduces fatigue."
        )
    ),
    CareTip(
        title = "Practice Deep Breathing",
        message = "Take 5 minutes to breathe deeply and relax your body.",
        reasons = listOf(
            "Lowers blood pressure and heart rate.",
            "Reduces anxiety and promotes calmness.",
            "Increases oxygen supply to the brain."
        )
    ),
    CareTip(
        title = "Eat a Fruit Today",
        message = "Add at least one fresh fruit to your diet today.",
        reasons = listOf(
            "Rich in vitamins and natural antioxidants.",
            "Boosts immunity and fights infections.",
            "Provides natural energy with no added sugar."
        )
    ),
    CareTip(
        title = "Get Enough Sleep",
        message = "Aim for 7–8 hours of quality sleep tonight.",
        reasons = listOf(
            "Allows your body to repair and regenerate cells.",
            "Improves memory, mood, and focus.",
            "Reduces risk of heart disease and diabetes."
        )
    ),
    CareTip(
        title = "Stretch Your Body",
        message = "Spend 5–10 minutes stretching your muscles today.",
        reasons = listOf(
            "Relieves muscle tension and stiffness.",
            "Improves flexibility and posture.",
            "Reduces risk of injury during daily activities."
        )
    ),
    CareTip(
        title = "Limit Screen Time",
        message = "Take a 20-minute break from screens every hour.",
        reasons = listOf(
            "Reduces eye strain and headaches.",
            "Improves sleep quality at night.",
            "Gives your mind a needed rest."
        )
    ),
    CareTip(
        title = "Eat a Balanced Meal",
        message = "Make sure today's meals include protein, carbs, and vegetables.",
        reasons = listOf(
            "Provides all essential nutrients your body needs.",
            "Keeps blood sugar levels stable throughout the day.",
            "Supports muscle health and energy levels."
        )
    ),
    CareTip(
        title = "Connect With Someone",
        message = "Call or message a friend or family member today.",
        reasons = listOf(
            "Social connection reduces feelings of loneliness.",
            "Boosts mood and emotional wellbeing.",
            "Strengthens relationships and support networks."
        )
    ),
    CareTip(
        title = "Practice Gratitude",
        message = "Write down three things you are grateful for today.",
        reasons = listOf(
            "Shifts focus from stress to positivity.",
            "Improves mental health and emotional resilience.",
            "Helps you appreciate life's small joys."
        )
    )
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onEmergencyAction: (EmergencyAction) -> Unit
) {
    val state by viewModel.state.collectAsState()

    var activeSheet by remember { mutableStateOf<HomeSheetType?>(null) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var pendingEmergencyAction by remember { mutableStateOf<EmergencyAction?>(null) }

    // Care Tip cycling index — persists across sheet open/close
    var tipIndex by remember { mutableStateOf(0) }

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

                HomeEvent.SettingsClicked -> {
                    viewModel.onEvent(HomeEvent.SettingsClicked)
                }

                else -> viewModel.onEvent(event)
            }
        }
    )

    // 🔹 Emergency Bottom Sheet
    if (activeSheet != null) {
        EmergencyBottomSheetWrapper(
            visible = activeSheet == HomeSheetType.Emergency,
            onDismiss = { activeSheet = null },
            onConfirmAction = { action ->
                activeSheet = null
                pendingEmergencyAction = action
                showConfirmationDialog = true
            }
        )
    }

    // 🔹 Care Tip Bottom Sheet
    if (activeSheet == HomeSheetType.Reminder) {
        val tip = careTips[tipIndex % careTips.size]
        CareTipBottomSheet(
            tipTitle = t(tip.title),
            tipMessage = t(tip.message),
            reasons = tip.reasons.map { t(it) },
            tipNumber = tipIndex % careTips.size + 1,
            totalTips = careTips.size,
            onSkip = {
                // Close button
                activeSheet = null
            },
            onDone = {
                // Next Tip button: advance index and reopen with next tip
                tipIndex = (tipIndex + 1) % careTips.size
                activeSheet = null
                activeSheet = HomeSheetType.Reminder
            },
            onDismiss = {
                activeSheet = null
            }
        )
    }

    // 🔹 Emergency Confirmation Dialog
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
                    Text(t("YES"))
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
                    Text(t("NO"))
                }
            },
            title = { Text(t("Confirm Action")) },
            text = { Text(t("Are you sure you want to proceed?")) }
        )
    }
}