package com.example.healthassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidApp()
        }
    }
}
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun AppPreview() {
    AndroidApp()
}
