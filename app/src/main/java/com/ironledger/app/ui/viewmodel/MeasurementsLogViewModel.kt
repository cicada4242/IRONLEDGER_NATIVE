package com.ironledger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.local.entity.MeasurementLogEntity
import com.ironledger.app.data.repository.IronLedgerRepository
import com.ironledger.app.utils.Calculations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class MeasurementTypeInfo(
    val type: String,
    val latestValue: Float,
    val firstValue: Float,
    val percentChange: Float,
    val history: List<MeasurementLogEntity>
)

data class MeasurementsUiState(
    val typeInfos: Map<String, MeasurementTypeInfo> = emptyMap(),
    val bodyFatPercentage: Float = 0f,
    val selectedType: String = "Waist",
    val isLoading: Boolean = true
)

@HiltViewModel
class MeasurementsLogViewModel @Inject constructor(
    private val repository: IronLedgerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeasurementsUiState())
    val uiState: StateFlow<MeasurementsUiState> = _uiState.asStateFlow()

    init {
        observeMeasurements()
    }

    private fun observeMeasurements() {
        combine(
            repository.allMeasurementEntriesFlow,
            repository.profileFlow
        ) { entries, profile ->
            if (entries.isEmpty()) {
                _uiState.update { it.copy(isLoading = false) }
                return@combine
            }

            // Group by type
            val grouped = entries.groupBy { it.type }
            val typeInfos = grouped.mapValues { (type, history) ->
                val sorted = history.sortedBy { it.date }
                val first = sorted.first().value
                val latest = sorted.last().value
                MeasurementTypeInfo(
                    type = type,
                    latestValue = latest,
                    firstValue = first,
                    percentChange = if (first == 0f) 0f else (latest - first) / first * 100f,
                    history = sorted
                )
            }

            // Calculate Navy Body Fat
            val latestWaist = typeInfos["Waist"]?.latestValue ?: 0f
            val latestNeck = typeInfos["Neck"]?.latestValue ?: 0f
            val latestHips = typeInfos["Hips"]?.latestValue ?: 0f
            val height = profile?.height ?: 0f
            val sex = profile?.sex ?: "male"

            val bodyFat = if (latestWaist > 0f && latestNeck > 0f && height > 0f) {
                Calculations.calculateNavyBodyFat(
                    neck = latestNeck,
                    waist = latestWaist,
                    hips = latestHips,
                    height = height,
                    sex = sex
                )
            } else 0f

            _uiState.update { it.copy(
                typeInfos = typeInfos,
                bodyFatPercentage = bodyFat,
                isLoading = false
            ) }
        }.launchIn(viewModelScope)
    }

    fun selectType(type: String) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun saveMeasurements(
        measurements: Map<String, String> // Type -> Value string
    ) {
        viewModelScope.launch {
            val date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            measurements.forEach { (type, valueStr) ->
                val value = valueStr.toFloatOrNull()
                if (value != null && value > 0f) {
                    val entity = MeasurementLogEntity(
                        date = date,
                        type = type,
                        value = value
                    )
                    repository.insertMeasurementEntry(entity)
                }
            }
        }
    }
}
