package com.example.healthassistant.presentation.assessment.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthassistant.core.bodymap.BodyRegionData
import com.example.healthassistant.core.bodymap.SubPart
import com.example.healthassistant.core.bodymap.SymptomOption
import com.example.healthassistant.designsystem.AppColors
import com.example.healthassistant.designsystem.AppTypography

@Composable
fun BodyPartBottomSheet(
    regionData: BodyRegionData,
    isSubmitting: Boolean,
    onSubmit: (symptomId: String, symptomLabel: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedSubPart by remember { mutableStateOf<SubPart?>(null) }
    var selectedSymptom by remember { mutableStateOf<SymptomOption?>(null) }

    LaunchedEffect(regionData.regionId) {
        selectedSubPart = null
        selectedSymptom = null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        // Drag handle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFDDE1E7))
            )
        }

        AnimatedContent(
            targetState = selectedSubPart,
            transitionSpec = {
                if (targetState != null) {
                    (slideInHorizontally { it } + fadeIn()).togetherWith(
                        slideOutHorizontally { -it } + fadeOut()
                    )
                } else {
                    (slideInHorizontally { -it } + fadeIn()).togetherWith(
                        slideOutHorizontally { it } + fadeOut()
                    )
                }
            },
            label = "subpart_transition"
        ) { currentSubPart ->

            Column(modifier = Modifier.fillMaxWidth()) {

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 4.dp, top = 8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentSubPart != null) {
                        IconButton(
                            onClick = {
                                selectedSubPart = null
                                selectedSymptom = null
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = AppColors.darkBlue
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                    }

                    Text(
                        text = currentSubPart?.label ?: regionData.label,
                        style = AppTypography.poppinsSemiBold().copy(fontSize = 20.sp),
                        color = AppColors.darkBlue,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = AppColors.darkBlue
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFF0F2F5), thickness = 1.dp)

                val currentSymptoms = currentSubPart?.symptoms ?: regionData.symptoms
                val currentSubParts = if (currentSubPart == null) regionData.subParts else emptyList()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                ) {

                    items(currentSymptoms) { symptom ->
                        val isSelected = selectedSymptom?.id == symptom.id

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) AppColors.lightBlue.copy(alpha = 0.25f)
                                    else Color.Transparent
                                )
                                .clickable {
                                    selectedSymptom = if (isSelected) null else symptom
                                }
                                .padding(horizontal = 20.dp, vertical = 17.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = symptom.label,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    fontSize = 16.sp
                                ),
                                color = if (isSelected) AppColors.darkBlue else Color(0xFF2D3748),
                                modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AppColors.darkBlue)
                                )
                            }
                        }

                        HorizontalDivider(
                            color = Color(0xFFF0F2F5),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }

                    if (currentSubParts.isNotEmpty()) {
                        item {
                            Text(
                                text = "Specific areas",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.8.sp
                                ),
                                color = Color(0xFF9AA5B4),
                                modifier = Modifier.padding(
                                    start = 20.dp, end = 20.dp,
                                    top = 20.dp, bottom = 8.dp
                                )
                            )
                        }

                        items(currentSubParts) { subPart ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedSubPart = subPart
                                        selectedSymptom = null
                                    }
                                    .padding(horizontal = 20.dp, vertical = 17.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = subPart.label,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                    color = Color(0xFF2D3748),
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color(0xFFAEB9C8),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            HorizontalDivider(
                                color = Color(0xFFF0F2F5),
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }

                    item { Spacer(Modifier.height(8.dp)) }
                }

                // Animated submit button
                AnimatedVisibility(
                    visible = selectedSymptom != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Button(
                        onClick = {
                            selectedSymptom?.let { sym -> onSubmit(sym.id, sym.label) }
                        },
                        enabled = !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.darkBlue,
                            disabledContainerColor = AppColors.darkBlue.copy(alpha = 0.6f)
                        )
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(10.dp))
                        }
                        Text(
                            text = if (isSubmitting) "Submitting..." else "Confirm",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
