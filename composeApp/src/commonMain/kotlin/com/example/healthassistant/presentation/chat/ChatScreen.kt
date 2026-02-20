package com.example.healthassistant.presentation.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthassistant.core.platform.PlatformBackHandler
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    currentReportId: String?,
    onBack: () -> Unit
) {

    val state by viewModel.state.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    PlatformBackHandler {
        showExitDialog = true
    }


    // 🔥 Auto-scroll when new message arrives
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(state.messages.lastIndex)
            }
        }
    }

    // 🔥 Start chat when screen opens
    LaunchedEffect(Unit) {
        if (state.messages.isEmpty()) {
            viewModel.startChat(currentReportId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = {
                    showExitDialog = false
                },
                title = { Text("End Chat Session") },
                text = { Text("Warning: This will end the chat session.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            viewModel.endChat()
                            onBack()   // go to Home
                        }
                    ) {
                        Text("Yes, End")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Top bar
        TopAppBar(
            title = { Text("Remy Chatbot") },
            actions = {
                TextButton(
                    onClick = {
                        showExitDialog = true
                    }
                ) {
                    Text("Exit")
                }
            }
        )

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {

            items(state.messages) { message ->
                ChatBubble(message)
            }

            // Typing indicator
            if (state.isLoading) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "Remy is typing...",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

        }

        Divider()

        // Input bar
        ChatInputBar(
            typedMessage = state.typedMessage,
            isLoading = state.isLoading,
            onMessageChange = {
                viewModel.onEvent(ChatEvent.MessageChanged(it))
            },
            onSendClick = {
                viewModel.onEvent(ChatEvent.SendMessage)
            }
        )
    }
}
