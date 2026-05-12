package com.ironledger.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ironledger.app.data.local.entity.ChatHistoryEntity
import com.ironledger.app.ui.theme.*
import com.ironledger.app.ui.viewmodel.AICoachViewModel

@Composable
fun AICoachScreen(
    viewModel: AICoachViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Dark900)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "AI COACH",
                    style = MaterialTheme.typography.labelSmall,
                    color = TertiaryVibrant,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Iron Intelligence",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Chat Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(uiState.messages) { message ->
                ChatBubble(message)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Input Area
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Dark800,
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp)),
                    placeholder = { Text("Ask your coach...", color = Color.White.copy(alpha = 0.3f)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Dark700,
                        unfocusedContainerColor = Dark700,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    maxLines = 4
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TertiaryVibrant)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.Black)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatHistoryEntity) {
    val isUser = message.role == "user"
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val backgroundColor = if (isUser) Dark700 else Dark800
    val shape = if (isUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }
    val borderColor = if (isUser) Color.White.copy(alpha = 0.1f) else TertiaryVibrant.copy(alpha = 0.2f)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        if (!isUser) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = TertiaryVibrant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "IRON COACH",
                    style = MaterialTheme.typography.labelSmall,
                    color = TertiaryVibrant,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(shape)
                .background(backgroundColor)
                .border(1.dp, borderColor, shape)
                .padding(16.dp)
        ) {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}
