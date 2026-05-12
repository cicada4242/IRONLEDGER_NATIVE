package com.ironledger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.local.entity.WeightLogEntity
import com.ironledger.app.data.repository.IronLedgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WeightLogViewModel @Inject constructor(
    private val repository: IronLedgerRepository
) : ViewModel() {

    val weightEntries: StateFlow<List<WeightLogEntity>> = repository.allWeightEntriesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveWeight(weight: Float) {
        viewModelScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val existing = repository.getWeightByDate(today)
            if (existing != null) {
                repository.updateWeightEntry(existing.copy(weight = weight))
            } else {
                repository.insertWeightEntry(WeightLogEntity(date = today, weight = weight))
            }
        }
    }
}
