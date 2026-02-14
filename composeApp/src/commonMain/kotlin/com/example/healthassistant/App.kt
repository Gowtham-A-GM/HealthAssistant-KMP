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

import com.example.healthassistant.presentation.news.NewsScreen
import com.example.healthassistant.presentation.assessment.AssessmentViewModel
import com.example.healthassistant.data.remote.assessment.AssessmentApiImpl
import com.example.healthassistant.data.repository.AssessmentRepositoryImpl
import com.example.healthassistant.core.network.NetworkClient

import com.example.healthassistant.core.stt.SpeechToTextManager
import com.example.healthassistant.db.HealthDatabase
import com.example.healthassistant.data.local.assessment.AssessmentLocalDataSourceImpl

//import com.example.healthassistant.presentation.oldhome.HomeScreen
//import com.example.healthassistant.presentation.oldhome.HomeViewModel



//@Composable
//fun App(
//    speechToTextManager: SpeechToTextManager,
//    database: HealthDatabase
//) {
//
//    HealthAssistantTheme {
//
//        val local = remember {
//            AssessmentLocalDataSourceImpl(database)
//        }
//
//        var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
//
//        val api = remember {
//            AssessmentApiImpl(
//                client = NetworkClient.httpClient,
//                baseUrl = "https://878a-2405-201-e012-2038-e93d-f96a-9ece-bfa8.ngrok-free.app"
//            )
//        }
//
//        val repository = remember {
//            AssessmentRepositoryImpl(api, local)
//        }
//
//        val assessmentViewModel = remember {
//            AssessmentViewModel(
//                repository = repository,
//                speechToTextManager = speechToTextManager
//            )
//        }
//
//        Column {
//            Box(modifier = Modifier.weight(1f)) {
//                when (currentScreen) {
//                    AppScreen.Home -> HomeScreen(
//                        viewModel = HomeViewModel(
//                            onStartAssessment = {
//                                currentScreen = AppScreen.Assessment
//                            }
//                        )
//                    )
//
//                    AppScreen.Assessment -> AssessmentScreen(
//                        viewModel = assessmentViewModel,
//                        onExit = { currentScreen = AppScreen.Home },
//                        onReportGenerated = {
//                            currentScreen = AppScreen.HistoryDetail
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
//                        title = (currentScreen as AppScreen.CauseDetail).title,
//                        onBack = { currentScreen = AppScreen.HistoryDetail }
//                    )
//                }
//            }
//
//            if (currentScreen != AppScreen.Assessment) {
//                BottomNavBar(
//                    selected = currentScreen,
//                    onHomeClick = { currentScreen = AppScreen.Home },
//                    onHistoryClick = { currentScreen = AppScreen.History },
//                    onNewsClick = { currentScreen = AppScreen.News }
//                )
//            }
//        }
//    }
//}






import com.example.healthassistant.presentation.home.HomeScreen
import com.example.healthassistant.presentation.home.HomeViewModel

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
                baseUrl = "https://bc7a-49-37-212-5.ngrok-free.app"
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

