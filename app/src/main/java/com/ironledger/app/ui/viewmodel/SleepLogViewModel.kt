package com.ironledger.app.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.local.entity.SleepLogEntity
import com.ironledger.app.data.repository.IronLedgerRepository
import com.ironledger.app.utils.Calculations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class SleepEntryUi(
    val entity: SleepLogEntity,
    val displayBedtime: String,
    val displayWakeTime: String,
    val durationHours: Float,
    val quality: Calculations.SleepQuality,
    val qualityColor: Color
)

data class SleepLogUiState(
    val entries: List<SleepEntryUi> = emptyList(),
    val sevenDayAverage: Float = 0f,
    val isLoading: Boolean = true
)

@HiltViewModel
class SleepLogViewModel @Inject constructor(
    private val repository: IronLedgerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SleepLogUiState())
    val uiState: StateFlow<SleepLogUiState> = _uiState.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    init {
        observeSleepLogs()
    }

    private fun observeSleepLogs() {
        repository.allSleepEntriesFlow
            .onEach { _uiState.update { it.copy(isLoading = true) } }
            .map { entries ->
                entries.reversed().map { entity -> // DESC order for UI
                    val duration = Calculations.calculateSleepDuration(entity.bedtime, entity.wakeTime)
                    val quality = Calculations.getSleepQuality(duration)
                    
                    // Convert timestamps back to readable time for UI
                    val bedtimeStr = java.time.Instant.ofEpochMilli(entity.bedtime)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalTime()
                        .format(timeFormatter)
                        
                    val wakeTimeStr = java.time.Instant.ofEpochMilli(entity.wakeTime)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalTime()
                        .format(timeFormatter)

                    SleepEntryUi(
                        entity = entity,
                        displayBedtime = bedtimeStr,
                        displayWakeTime = wakeTimeStr,
                        durationHours = duration,
                        quality = quality,
                        qualityColor = when (quality) {
                            Calculations.SleepQuality.GREAT -> Color(0xFF2DD4BF) // Teal
                            Calculations.SleepQuality.FAIR -> Color(0xFFFBBF24)  // Amber
                            Calculations.SleepQuality.POOR -> Color(0xFFF87171)  // Red
                        }
                    )
                }
            }
            .onEach { uiEntries ->
                val avg = if (uiEntries.isEmpty()) 0f else {
                    uiEntries.take(7).map { it.durationHours }.average().toFloat()
                }
                _uiState.update { it.copy(
                    entries = uiEntries,
                    sevenDayAverage = avg,
                    isLoading = false
                ) }
            }
            .launchIn(viewModelScope)
    }

    fun saveSleepEntry(
        bedtime: String, // "HH:mm"
        wakeTime: String, // "HH:mm"
        note: String
    ) {
        viewModelScope.launch {
            val today = java.time.LocalDate.now()
            val wakeTimeLocal = java.time.LocalTime.parse(wakeTime)
            val bedTimeLocal = java.time.LocalTime.parse(bedtime)
            
            // Wake time is today
            val wakeDateTime = java.time.LocalDateTime.of(today, wakeTimeLocal)
            val wakeMillis = wakeDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
            
            // Bedtime logic: if bedtime is after wakeTime, it must have been yesterday
            var bedDateTime = java.time.LocalDateTime.of(today, bedTimeLocal)
            if (bedDateTime.isAfter(wakeDateTime)) {
                bedDateTime = bedDateTime.minusDays(1)
            }
            val bedMillis = bedDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
            
            val wakeDateStr = today.format(dateFormatter)
            val existing = repository.getSleepByDate(wakeDateStr)
            
            val entry = SleepLogEntity(
                id = existing?.id ?: 0,
                date = wakeDateStr,
                bedtime = bedMillis,
                wakeTime = wakeMillis,
                note = note
            )
            
            if (existing != null) {
                repository.updateSleepEntry(entry)
            } else {
                repository.insertSleepEntry(entry)
            }
        }
    }
}
