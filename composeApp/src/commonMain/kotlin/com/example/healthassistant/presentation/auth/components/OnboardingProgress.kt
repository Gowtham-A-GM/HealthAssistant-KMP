package com.example.healthassistant.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthassistant.core.utils.t
import com.example.healthassistant.designsystem.AppColors
import com.example.healthassistant.designsystem.AppTypography

@Composable
fun OnboardingProgress(
    currentStep: Int,
    totalSteps: Int,
    title: String
) {
    val progress = currentStep.toFloat() / totalSteps.toFloat()

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = t("Step $currentStep of $totalSteps"),
            style = AppTypography.bodySmall().copy(fontSize = 12.sp),
            color = AppColors.dustyGray
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = AppColors.blue,
            trackColor = AppColors.dustyGray.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = t(title),
            style = AppTypography.h1(),
            color = AppColors.textPrimary
        )
    }
}
