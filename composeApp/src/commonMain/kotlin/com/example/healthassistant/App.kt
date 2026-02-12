package com.example.healthassistant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.healthassistant.designsystem.HealthAssistantTheme
import com.example.healthassistant.presentation.navigation.AppScreen
import com.example.healthassistant.presentation.navigation.BottomNavBar
import com.example.healthassistant.presentation.assessment.AssessmentScreen
import com.example.healthassistant.presentation.history.*
import com.example.healthassistant.presentation.home.HomeScreen
//import com.example.healthassistant.presentation.home.HomeViewModel
import com.example.healthassistant.presentation.news.NewsScreen
import com.example.healthassistant.presentation.assessment.AssessmentViewModel
import com.example.healthassistant.data.remote.assessment.AssessmentApiImpl
import com.example.healthassistant.data.repository.AssessmentRepositoryImpl
import com.example.healthassistant.core.network.NetworkClient
import com.example.healthassistant.presentation.home.HomeViewModel
//import com.example.healthassistant.presentation.home.HomeTab
import com.example.healthassistant.core.stt.SpeechToTextManager
import com.example.healthassistant.core.database.DatabaseDriverFactory
import com.example.healthassistant.db.HealthDatabase
import com.example.healthassistant.data.local.assessment.AssessmentLocalDataSourceImpl


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
    speechToTextManager: SpeechToTextManager,
    database: HealthDatabase
) {

    HealthAssistantTheme {

        val local = remember {
            AssessmentLocalDataSourceImpl(database)
        }

        var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }

        val api = remember {
            AssessmentApiImpl(
                client = NetworkClient.httpClient,
                baseUrl = "https://878a-2405-201-e012-2038-e93d-f96a-9ece-bfa8.ngrok-free.app"
            )
        }

        val repository = remember {
            AssessmentRepositoryImpl(api, local)
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
                    AppScreen.Home -> HomeScreen(
                        viewModel = HomeViewModel(
                            onStartAssessment = {
                                currentScreen = AppScreen.Assessment
                            }
                        )
                    )

                    AppScreen.Assessment -> AssessmentScreen(
                        viewModel = assessmentViewModel,
                        onExit = { currentScreen = AppScreen.Home },
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


