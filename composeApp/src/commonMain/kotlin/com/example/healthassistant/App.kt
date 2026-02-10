package com.example.healthassistant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.healthassistant.designsystem.HealthAssistantTheme
import com.example.healthassistant.navigation.AppScreen
import com.example.healthassistant.navigation.BottomNavBar
import com.example.healthassistant.presentation.assessment.AssessmentScreen
import com.example.healthassistant.presentation.history.*
import com.example.healthassistant.presentation.home.HomeScreen
//import com.example.healthassistant.presentation.home.HomeViewModel
import com.example.healthassistant.presentation.news.NewsScreen
import com.example.healthassistant.presentation.assessment.AssessmentViewModel
import com.example.healthassistant.presentation.assessment.data.AssessmentApiImpl
import com.example.healthassistant.presentation.assessment.data.AssessmentRepositoryImpl
import com.example.healthassistant.presentation.assessment.data.network.NetworkClient
import com.example.healthassistant.presentation.home.HomeState
import com.example.healthassistant.presentation.home.HomeViewModel
//import com.example.healthassistant.presentation.home.HomeTab
import com.example.healthassistant.stt.FakeSpeechToTextManager
import com.example.healthassistant.stt.SpeechToTextManager


//@Composable
//fun App() {
//
//    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
//
//    HealthAssistantTheme {
//
//        Column {
//
//            // ───── Screen Content ─────
//            Box(modifier = Modifier.weight(1f)) {
//                when (val screen = currentScreen) {
//
//                    AppScreen.Home -> HomeScreen(
//                        viewModel = HomeViewModel(
//                            onStartAssessment = {
//                                currentScreen = AppScreen.AssessmentStart
//                            }
//                        )
//                    )
//
//                    AppScreen.AssessmentStart -> AssessmentStartScreen(
//                        onMyselfClick = {
//                            // next step later
//                        },
//                        onSomeoneElseClick = {
//                            // next step later
//                        },
//                        onExit = {
//                            currentScreen = AppScreen.Home
//                        }
//                    )
//
//                    AppScreen.History -> HistoryScreen(
//                        onItemClick = {
//                            currentScreen = AppScreen.HistoryDetail
//                        }
//                    )
//
//                    AppScreen.News -> NewsScreen()
//
//                    AppScreen.HistoryDetail -> HistoryDetailScreen(
//                        onBack = { currentScreen = AppScreen.History },
//                        onCauseClick = { cause ->
//                            currentScreen = AppScreen.CauseDetail(cause)
//                        }
//                    )
//
//                    is AppScreen.CauseDetail -> CauseDetailScreen(
//                        title = screen.title,
//                        onBack = { currentScreen = AppScreen.HistoryDetail }
//                    )
//                }
//
//            }
//
//            // ───── Bottom Navigation (ONLY for main tabs) ─────
//            BottomNavBar(
//                selected = currentScreen,
//                onHomeClick = { currentScreen = AppScreen.Home },
//                onHistoryClick = { currentScreen = AppScreen.History },
//                onNewsClick = { currentScreen = AppScreen.News }
//            )
//        }
//    }
//}

@Composable
fun App(
    speechToTextManager: SpeechToTextManager
) {
    HealthAssistantTheme {

        var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }

        // Shared backend
        val api = remember {
            AssessmentApiImpl(
                client = NetworkClient.httpClient,
//                baseUrl = "http://10.0.2.2:8000"
                baseUrl = "https://0672-2405-201-e012-2038-3816-a61f-53e9-6ddf.ngrok-free.app"
            )
        }

        val repository = remember {
            AssessmentRepositoryImpl(api)
        }

        val assessmentViewModel = remember {
            AssessmentViewModel(
                repository = repository,
                speechToTextManager = speechToTextManager
            )
        }

        Column {

            Box(modifier = Modifier.weight(1f)) {
                when (currentScreen) {

//                    AppScreen.Home -> HomeScreen(
//                        viewModel = HomeViewModel(
//                            onStartAssessment = {
//                                currentScreen = AppScreen.Assessment
//                            }
//                        )
//                    )

                    AppScreen.Home -> HomeScreen(
                        viewModel = HomeViewModel(
                            onStartAssessment = {
                                currentScreen = AppScreen.Assessment
                            }
                        )
                    )

                    AppScreen.Assessment -> AssessmentScreen(
                        viewModel = assessmentViewModel,
                        onExit = {
                            currentScreen = AppScreen.Home
                        },
                        onReportGenerated = {
                            currentScreen = AppScreen.HistoryDetail
                        }
                    )

                    AppScreen.History -> HistoryScreen(
                        onItemClick = {
                            currentScreen = AppScreen.HistoryDetail
                        }
                    )

                    AppScreen.News -> NewsScreen()

                    AppScreen.HistoryDetail -> HistoryDetailScreen(
                        onBack = { currentScreen = AppScreen.History },
                        onCauseClick = { cause ->
                            currentScreen = AppScreen.CauseDetail(cause)
                        }
                    )

                    is AppScreen.CauseDetail -> CauseDetailScreen(
                        title = (currentScreen as AppScreen.CauseDetail).title,
                        onBack = { currentScreen = AppScreen.HistoryDetail }
                    )
                }
            }

            if (currentScreen != AppScreen.Assessment) {
                BottomNavBar(
                    selected = currentScreen,
                    onHomeClick = { currentScreen = AppScreen.Home },
                    onHistoryClick = { currentScreen = AppScreen.History },
                    onNewsClick = { currentScreen = AppScreen.News }
                )
            }

        }
    }
}

//@Composable
//fun App() {
//    HealthAssistantTheme {
//
//        // TEMP state just to render HomeScreen
//        val homeState = remember {
//            HomeState(
//                userName = "Gowtham",
//                userImageRes = null,
//                selectedTab = HomeTab.HOME,
//                suggestions = listOf(
//                    "I have a headache",
//                    "I feel tired",
//                    "Stomach pain",
//                    "Chest pain",
//                    "Fever symptoms"
//                ),
//                quickHelpItems = emptyList()
//            )
//        }
//
//        HomeScreen(
//            state = homeState,
//            onEvent = { event ->
//                println("HomeEvent: $event")
//            }
//        )
//    }
//}


