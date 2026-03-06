//package com.example.healthassistant.presentation.assessment.components
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import healthassistant.composeapp.generated.resources.Res
//import healthassistant.composeapp.generated.resources.img_user_avatar
//import org.jetbrains.compose.resources.painterResource
//
//@Composable
//fun BodySelector(
//    onBodyPartClick: (String) -> Unit
//) {
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//
//        // 🔹 BODY IMAGE (base layer)
//        Image(
//            painter = painterResource(Res.drawable.),
//            contentDescription = "Body Model",
//            modifier = Modifier
//                .width(596.dp)
//                .height(1137.dp)
//        )
//
//        // 🔹 HEAD REGION
//        Image(
//            painter = painterResource("drawable/head.xml"),
//            contentDescription = "Head Region",
//            modifier = Modifier
//                .width(120.dp)
//                .height(146.dp)
//                .offset(y = (-470).dp)
//                .clickable {
//                    onBodyPartClick("head")
//                }
//        )
//
//        // 🔹 NECK REGION
//        Image(
//            painter = painterResource("drawable/neck.xml"),
//            contentDescription = "Neck Region",
//            modifier = Modifier
//                .width(90.dp)
//                .height(80.dp)
//                .offset(y = (-410).dp)
//                .clickable {
//                    onBodyPartClick("neck")
//                }
//        )
//
//        // 🔹 CHEST REGION
//        Image(
//            painter = painterResource("drawable/chest.xml"),
//            contentDescription = "Chest Region",
//            modifier = Modifier
//                .width(260.dp)
//                .height(180.dp)
//                .offset(y = (-300).dp)
//                .clickable {
//                    onBodyPartClick("chest")
//                }
//        )
//    }
//}