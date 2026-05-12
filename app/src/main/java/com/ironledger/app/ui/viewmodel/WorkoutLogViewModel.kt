package com.ironledger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.local.entity.WorkoutEntity
import com.ironledger.app.data.repository.IronLedgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class WorkoutLogUiState(
    val selectedDate: String,
    val workouts: List<WorkoutEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class WorkoutLogViewModel @Inject constructor(
    private val repository: IronLedgerRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    val uiState: StateFlow<WorkoutLogUiState> = _selectedDate.flatMapLatest { date ->
        repository.getWorkoutsByDateFlow(date).map { workouts ->
            WorkoutLogUiState(
                selectedDate = date,
                workouts = workouts,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WorkoutLogUiState(selectedDate = _selectedDate.value)
    )

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }
}
