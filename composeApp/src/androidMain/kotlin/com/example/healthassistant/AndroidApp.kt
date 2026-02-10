package com.example.healthassistant

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.healthassistant.designsystem.HealthAssistantTheme
import com.example.healthassistant.presentation.assessment.AssessmentScreen
import com.example.healthassistant.presentation.assessment.AssessmentViewModel
import com.example.healthassistant.presentation.assessment.data.AssessmentApiImpl
import com.example.healthassistant.presentation.assessment.data.AssessmentRepositoryImpl
import com.example.healthassistant.presentation.assessment.data.network.createHttpClient
import com.example.healthassistant.stt.AndroidSpeechToTextManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat

//@Composable
//fun AndroidApp() {
//    HealthAssistantTheme {
//
//        val context = LocalContext.current
//
//        var micGranted by remember {
//            mutableStateOf(
//                ContextCompat.checkSelfPermission(
//                    context,
//                    android.Manifest.permission.RECORD_AUDIO
//                ) == PackageManager.PERMISSION_GRANTED
//            )
//        }
//
//        val permissionLauncher =
//            rememberLauncherForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) { granted ->
//                micGranted = granted
//            }
//
//        LaunchedEffect(Unit) {
//            if (!micGranted) {
//                permissionLauncher.launch(
//                    android.Manifest.permission.RECORD_AUDIO
//                )
//            }
//        }
//
//        if (!micGranted) {
//            // Optional: show UI explaining why mic is needed
//            return@HealthAssistantTheme
//        }
//
//        // 👇 STT creation ONLY after permission granted
//        val speechToTextManager = remember {
//            AndroidSpeechToTextManager(context)
//        }
//
//        val api = remember {
//            AssessmentApiImpl(
//                client = createHttpClient(),
//                baseUrl = "http://10.0.2.2:8000"
//            )
//        }
//
//        val repository = remember {
//            AssessmentRepositoryImpl(api)
//        }
//
//        val viewModel = remember {
//            AssessmentViewModel(
//                repository = repository,
//                speechToTextManager = speechToTextManager
//            )
//        }
//
//        AssessmentScreen(
//            viewModel = viewModel,
//            onExit = {}
//        )
//    }
//}


@Composable
fun AndroidApp() {
    HealthAssistantTheme {

        val context = LocalContext.current

        var micGranted by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val permissionLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                micGranted = granted
            }

        LaunchedEffect(Unit) {
            if (!micGranted) {
                permissionLauncher.launch(
                    android.Manifest.permission.RECORD_AUDIO
                )
            }
        }

        if (!micGranted) return@HealthAssistantTheme

        // ✅ Android-only STT
        val speechToTextManager = remember {
            AndroidSpeechToTextManager(context)
        }

        // 👇 CALL SHARED APP (THIS IS THE KEY)
        App(
            speechToTextManager = speechToTextManager
        )
    }
}
