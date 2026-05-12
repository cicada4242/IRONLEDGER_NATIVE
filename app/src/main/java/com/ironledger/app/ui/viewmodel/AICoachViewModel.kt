package com.ironledger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.local.entity.ChatHistoryEntity
import com.ironledger.app.data.repository.IronLedgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AICoachUiState(
    val messages: List<ChatHistoryEntity> = emptyList(),
    val isTyping: Boolean = false
)

@HiltViewModel
class AICoachViewModel @Inject constructor(
    private val repository: IronLedgerRepository
) : ViewModel() {

    val uiState: StateFlow<AICoachUiState> = repository.chatHistoryFlow
        .map { AICoachUiState(messages = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AICoachUiState()
        )

    fun sendMessage(content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            // Add user message
            repository.insertChatMessage(ChatHistoryEntity(role = "user", content = content, timestamp = System.currentTimeMillis()))
            
            // TODO: Call actual AI API (Gemini/OpenAI)
            // For now, simulated response
            simulateAIResponse()
        }
    }

    private fun simulateAIResponse() {
        viewModelScope.launch {
            // isTyping = true logic would go here if we had a local state
            repository.insertChatMessage(ChatHistoryEntity(role = "assistant", content = "I'm your IronLedger AI Coach. I can help you track your macros, suggest workouts, and keep you motivated! (Simulated response)", timestamp = System.currentTimeMillis()))
        }
    }
}
