package com.example.healthassistant

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.healthassistant.designsystem.HealthAssistantTheme
import com.example.healthassistant.stt.AndroidSpeechToTextManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.healthassistant.core.utils.appContext
import com.example.healthassistant.db.HealthDatabase
import com.example.healthassistant.tts.AndroidTextToSpeechManager

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
fun AndroidApp(database: HealthDatabase) {
    HealthAssistantTheme {

        val context = LocalContext.current
        appContext = context.applicationContext


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

        val ttsManager = remember {
            AndroidTextToSpeechManager(context)
        }


        // 👇 CALL SHARED APP (THIS IS THE KEY)
        App(
            speechToTextManager = speechToTextManager,
            ttsManager = ttsManager,
            database = database,
            newsApiKey = BuildConfig.NEWS_API_KEY
        )


    }
}
