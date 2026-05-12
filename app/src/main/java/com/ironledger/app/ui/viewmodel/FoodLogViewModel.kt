package com.ironledger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.local.entity.MealEntryEntity
import com.ironledger.app.data.repository.IronLedgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class FoodLogUiState(
    val selectedDate: String,
    val meals: List<MealEntryEntity> = emptyList(),
    val totalCalories: Int = 0,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFat: Float = 0f,
    val isLoading: Boolean = true
)

@HiltViewModel
class FoodLogViewModel @Inject constructor(
    private val repository: IronLedgerRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    val uiState: StateFlow<FoodLogUiState> = _selectedDate.flatMapLatest { date ->
        repository.getMealsByDateFlow(date).map { meals ->
            FoodLogUiState(
                selectedDate = date,
                meals = meals,
                totalCalories = meals.sumOf { it.calories },
                totalProtein = meals.sumOf { it.protein.toDouble() }.toFloat(),
                totalCarbs = meals.sumOf { it.carbs.toDouble() }.toFloat(),
                totalFat = meals.sumOf { it.fat.toDouble() }.toFloat(),
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FoodLogUiState(selectedDate = _selectedDate.value)
    )

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }
}
