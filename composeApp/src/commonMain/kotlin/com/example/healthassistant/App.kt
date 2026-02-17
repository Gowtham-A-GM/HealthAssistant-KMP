package com.example.healthassistant

import NewsApiImpl
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.healthassistant.core.logger.AppLogger
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
import com.example.healthassistant.core.platform.PlatformBackHandler

import com.example.healthassistant.core.stt.SpeechToTextManager
import com.example.healthassistant.core.tts.TextToSpeechManager
import com.example.healthassistant.data.local.assessment.AssessmentLocalDataSourceImpl
import com.example.healthassistant.data.local.profile.ProfileLocalDataSourceImpl
import com.example.healthassistant.data.local.report.ReportLocalDataSourceImpl
import com.example.healthassistant.data.repository.NewsRepositoryImpl
import com.example.healthassistant.db.HealthDatabase
import com.example.healthassistant.presentation.assessment.AssessmentCauseDetailScreen
import com.example.healthassistant.presentation.assessment.AssessmentReportScreen
//import com.example.healthassistant.data.local.assessment.AssessmentLocalDataSourceImpl
import com.example.healthassistant.presentation.home.HomeScreen
import com.example.healthassistant.presentation.home.HomeViewModel
import com.example.healthassistant.presentation.news.NewsViewModel

@Composable
fun App(
    speechToTextManager: SpeechToTextManager,
    ttsManager: TextToSpeechManager,
    database: HealthDatabase,
    newsApiKey: String
) {

    HealthAssistantTheme {

        var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }

        PlatformBackHandler {

            currentScreen = when (currentScreen) {

                is AppScreen.AssessmentCauseDetail ->
                    AppScreen.AssessmentReport

                AppScreen.AssessmentReport ->
                    AppScreen.Assessment

                AppScreen.Assessment ->
                    AppScreen.Home

                is AppScreen.CauseDetail ->
                    AppScreen.HistoryDetail

                AppScreen.HistoryDetail ->
                    AppScreen.History

                AppScreen.History ->
                    AppScreen.Home

                AppScreen.News ->
                    AppScreen.Home

                AppScreen.Home ->
                    AppScreen.Home

                else ->
                    AppScreen.Home
            }
        }


        // ✅ API
        val api = remember {
            AssessmentApiImpl(
                client = NetworkClient.httpClient,
                baseUrl = "https://9bad-2405-201-e012-2038-1824-3bef-9800-63e3.ngrok-free.app"
            )
        }

        val sessionLocal = remember {
            AssessmentLocalDataSourceImpl(database)
        }

        val profileLocal = remember {
            ProfileLocalDataSourceImpl(database)
        }

        val reportLocal = remember {
            ReportLocalDataSourceImpl(database)
        }


        val repository = remember {
            AssessmentRepositoryImpl(
                api = api,
                sessionLocal = sessionLocal,
                profileLocal = profileLocal,
                reportLocal = reportLocal
            )
        }




        // ✅ ViewModel
        val assessmentViewModel = remember {
            AssessmentViewModel(
                repository = repository,
                speechToTextManager = speechToTextManager,
                ttsManager = ttsManager
            )
        }

        // News
        val newsApi = remember {
//            AppLogger.d("APP", "News API Key: $newsApiKey")
            NewsApiImpl(
                client = NetworkClient.httpClient,
                apiKey = newsApiKey
            )
        }

        val newsRepository = remember {
            NewsRepositoryImpl(newsApi)
        }

        val newsViewModel = remember {
            NewsViewModel(newsRepository)
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
                            currentScreen = AppScreen.AssessmentReport
                        }
                    )

                    AppScreen.AssessmentReport -> {

                        val state = assessmentViewModel.state.collectAsState().value
                        val report = state.report

                        report?.let {
                            AssessmentReportScreen(
                                report = it,
                                onBack = { currentScreen = AppScreen.Home },
                                onCauseClick = { cause ->
                                    currentScreen = AppScreen.AssessmentCauseDetail(cause)
                                },
                                onGoHome = {
                                    assessmentViewModel.endAssessment {
                                        currentScreen = AppScreen.Home
                                    }
                                }
                            )

                        }
                    }




                    is AppScreen.AssessmentCauseDetail ->
                        AssessmentCauseDetailScreen(
                            cause = (currentScreen as AppScreen.AssessmentCauseDetail).cause,
                            onBack = { currentScreen = AppScreen.AssessmentReport }
                        )




                    AppScreen.History -> HistoryScreen(
                        onItemClick = {
                            currentScreen = AppScreen.HistoryDetail
                        }
                    )

                    AppScreen.News -> NewsScreen(
                        viewModel = newsViewModel
                    )


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
