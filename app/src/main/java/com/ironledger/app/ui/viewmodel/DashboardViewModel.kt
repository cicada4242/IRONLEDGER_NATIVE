package com.ironledger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.local.entity.HabitTrackerEntity
import com.ironledger.app.data.local.entity.MealEntryEntity
import com.ironledger.app.data.local.entity.ProfileEntity
import com.ironledger.app.data.local.entity.WorkoutEntity
import com.ironledger.app.data.repository.IronLedgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class DashboardUiState(
    val profile: ProfileEntity? = null,
    val todaysMeals: List<MealEntryEntity> = emptyList(),
    val todaysWorkouts: List<WorkoutEntity> = emptyList(),
    val habitTracker: HabitTrackerEntity? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: IronLedgerRepository
) : ViewModel() {

    private val todayDateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.profileFlow,
        repository.getMealsByDateFlow(todayDateString),
        repository.getWorkoutsByDateFlow(todayDateString),
        repository.habitTrackerFlow
    ) { profile, meals, workouts, habitTracker ->
        DashboardUiState(
            profile = profile,
            todaysMeals = meals,
            todaysWorkouts = workouts,
            habitTracker = habitTracker,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )
}
