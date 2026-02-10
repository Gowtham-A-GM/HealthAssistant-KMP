//package com.example.healthassistant.presentation.assessment
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.ui.graphics.Color
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MicOff
//import androidx.compose.material.icons.filled.Mic
//import androidx.compose.material.icons.filled.VolumeUp
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.style.TextAlign
//
//
//@Composable
//fun AssessmentScreen(
//    onMyselfClick: () -> Unit,
//    onSomeoneElseClick: () -> Unit,
//    onExit: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .statusBarsPadding()
//            .background(MaterialTheme.colorScheme.background),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        // ───── Top Bar ─────
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 12.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "New assessment",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold
//            )
//
//            Text(
//                text = "step 1",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.primary
//            )
//        }
//
//        Spacer(modifier = Modifier.height(40.dp))
//
//        // ───── Illustration Placeholder ─────
//        Box(
//            contentAlignment = Alignment.Center
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(200.dp)
//                    .background(
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                        CircleShape
//                    )
//            )
//            Box(
//                modifier = Modifier
//                    .size(140.dp)
//                    .background(
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
//                        CircleShape
//                    )
//            )
//        }
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // ───── Question ─────
//        Text(
//            text = "For who are you taking this\nassessment ?",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // ───── Buttons ─────
//        Button(
//            onClick = onMyselfClick,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 40.dp)
//                .height(52.dp),
//            shape = RoundedCornerShape(26.dp)
//        ) {
//            Text("Myself")
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        OutlinedButton(
//            onClick = onSomeoneElseClick,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 40.dp)
//                .height(52.dp),
//            shape = RoundedCornerShape(26.dp)
//        ) {
//            Text("Someone else")
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Text(
//            text = "For me only I am taking this.",
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // ───── Bottom Controls ─────
//        BottomAssessmentControls(
//            onExit = onExit
//        )
//    }
//}
//
//@Composable
//fun BottomAssessmentControls(
//    onExit: () -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        shape = RoundedCornerShape(24.dp),
//        color = MaterialTheme.colorScheme.surface
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp, vertical = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Row(
//                modifier = Modifier.weight(1f),
//                horizontalArrangement = Arrangement.spacedBy(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                ActionIcon(Icons.Default.MicOff)
//                ActionIcon(Icons.Default.Mic)
//                ActionIcon(Icons.Default.VolumeUp)
//                ActionIcon(Icons.Default.MoreVert)
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            // Exit button
//            Box(
//                modifier = Modifier
//                    .size(52.dp)
//                    .background(Color.Red, CircleShape)
//                    .clickable { onExit() },
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Close,
//                    contentDescription = "Exit",
//                    tint = Color.White
//                )
//            }
//        }
//    }
//}
//
//
//
//@Composable
//private fun ActionIcon(icon: ImageVector) {
//    Box(
//        modifier = Modifier
//            .size(44.dp)
//            .background(
//                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
//                CircleShape
//            )
//            .clickable { },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//        )
//    }
//}
//
//
