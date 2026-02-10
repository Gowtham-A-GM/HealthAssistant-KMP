//package com.example.healthassistant.presentation.home
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.healthassistant.designsystem.AppShapes
//import com.example.healthassistant.designsystem.AppTypography
//import com.example.healthassistant.designsystem.AppColors
//import androidx.compose.foundation.layout.Box
//
//
//
//@Composable
//fun HomeScreen(
//    state: HomeState,
//    onEvent: (HomeEvent) -> Unit
//) {
//    Box(modifier = Modifier.fillMaxSize()) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(bottom = 80.dp) // space for bottom nav
//        ) {
//            TopBarSection(state)
//            GreetingSection(state)
//            Spacer(Modifier.height(16.dp))
//            MainHealthCard(onEvent)
//            SuggestionsSection()
//            QuickHelpSection()
//        }
//
//        BottomNavigation(
//            modifier = Modifier.align(Alignment.BottomCenter),
//            selected = state.selectedTab,
//            onTabSelected = { onEvent(HomeEvent.TabChanged(it)) }
//        )
//    }
//}
//
//@Composable
//fun TopBarSection(state: HomeState) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 12.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        CircularAvatar(imageRes = state.userImageRes)
//
//        SettingsButton(onClick = { /* later */ })
//    }
//}
//
//@Composable
//fun GreetingSection(state: HomeState) {
//    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//        Text(
//            text = "Good Morning, ${state.userName}",
//            style = AppTypography.bodyMedium,
//            color = AppColors.textSecondary
//        )
//
//        Spacer(Modifier.height(4.dp))
//
//        Text(
//            text = "How Are You Today ?",
//            style = AppTypography.titleLarge,
//            color = AppColors.textPrimary
//        )
//    }
//}
//
//@Composable
//fun MainHealthCard(onEvent: (HomeEvent) -> Unit) {
//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//            .background(
//                color = AppColors.surface,
//                shape = AppShapes.medium
//            )
//            .padding(12.dp)
//    ) {
//
//        HealthInfoRow()
//
//        Spacer(Modifier.height(12.dp))
//
//        StartAssessmentButton {
//            onEvent(HomeEvent.StartAssessment)
//        }
//    }
//}
//
//@Composable
//fun SuggestionsSection() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "You can ask like this",
//            style = AppTypography.titleMedium,
//            color = AppColors.textPrimary
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        // Chips will come later
//    }
//}
//
//@Composable
//fun QuickHelpSection() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 16.dp)
//    ) {
//        Text(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            text = "Quick Help",
//            style = AppTypography.titleMedium,
//            color = AppColors.textPrimary
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        // Horizontal cards will come later
//    }
//}
//
//@Composable
//fun BottomNavigation(
//    modifier: Modifier = Modifier,
//    selected: HomeTab,
//    onTabSelected: (HomeTab) -> Unit
//) {
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(72.dp)
//            .background(AppColors.surface)
//    ) {
//        // Will implement exact UI later
//    }
//}
//
//@Composable
//fun CircularAvatar(imageRes: Int?) {
//    Box(
//        modifier = Modifier
//            .size(40.dp)
//            .background(
//                color = AppColors.grayLight,
//                shape = AppShapes.circle
//            )
//    )
//}
//
//
//
//@Composable
//fun SettingsButton(onClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .size(36.dp)
//            .background(
//                color = AppColors.surface,
//                shape = AppShapes.small
//            )
//            .clickable { onClick() },
//        contentAlignment = Alignment.Center
//    ) {
//        // Settings icon will go here later
//    }
//}
//
//
//@Composable
//fun HealthInfoRow() {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Image + text later
//        Text(
//            text = "Not feeling well?\nI can help you understand your health",
//            style = AppTypography.bodyMedium,
//            color = AppColors.textPrimary
//        )
//    }
//}
//
//@Composable
//fun StartAssessmentButton(onClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(48.dp)
//            .background(
//                color = AppColors.primary,
//                shape = AppShapes.medium
//            )
//            .clickable { onClick() },
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Start Symptom Assessment",
//            style = AppTypography.bodyMedium,
//            color = AppColors.onPrimary
//        )
//    }
//}
//
//
//
//
//
