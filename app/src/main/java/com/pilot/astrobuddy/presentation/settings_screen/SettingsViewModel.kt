package com.pilot.astrobuddy.presentation.settings_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.domain.model.warning.WarningSeverity
import com.pilot.astrobuddy.domain.model.warning.WarningType
import com.pilot.astrobuddy.setings_store.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStore: SettingsStore
    //savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    init{
        viewModelScope.launch{
            val startTime = System.nanoTime()
            _state.value = SettingsState(
                forecastDays = settingsStore.getDaysFromDataStore(),
                units = settingsStore.getUnitsFromDataStore(),
                timeFormat = settingsStore.getTimeFormatFromDataStore(),
                dewThres = Triple(
                    settingsStore.getThresFromDataStore(WarningType.DEW,WarningSeverity.LOW),
                    settingsStore.getThresFromDataStore(WarningType.DEW,WarningSeverity.MED),
                    settingsStore.getThresFromDataStore(WarningType.DEW,WarningSeverity.HIGH)
                ),
                windThres = Triple(
                    settingsStore.getThresFromDataStore(WarningType.WIND,WarningSeverity.LOW),
                    settingsStore.getThresFromDataStore(WarningType.WIND,WarningSeverity.MED),
                    settingsStore.getThresFromDataStore(WarningType.WIND,WarningSeverity.HIGH)
                ),
                rainThres = Triple(
                    settingsStore.getThresFromDataStore(WarningType.RAIN,WarningSeverity.LOW),
                    settingsStore.getThresFromDataStore(WarningType.RAIN,WarningSeverity.MED),
                    settingsStore.getThresFromDataStore(WarningType.RAIN,WarningSeverity.HIGH)
                )
            )
            val timeTaken = ((System.nanoTime()-startTime)/1000000)
            Log.i("SETTINGSSCREEN", "Loaded in "+timeTaken+"ms")
        }
    }

    fun updateDays(days: Int){
        viewModelScope.launch {
            settingsStore.saveDaysToDataStore(days)
            _state.value = _state.value.copy(forecastDays = settingsStore.getDaysFromDataStore())
        }
    }

    fun updateUnits(){
        viewModelScope.launch{
            settingsStore.toggleUnits()
            _state.value = _state.value.copy(units = settingsStore.getUnitsFromDataStore())
        }
    }

    fun updateTimeFormat(){
        viewModelScope.launch{
            settingsStore.toggleTimeFormat()
            _state.value = _state.value.copy(timeFormat = settingsStore.getTimeFormatFromDataStore())
        }
    }

    fun updateWarningThreshold(warningType: WarningType, warningSeverity: WarningSeverity, value: Int) {
        viewModelScope.launch {
            settingsStore.saveThresToDataStore(warningType, warningSeverity, value)
            when (warningType) {
                WarningType.DEW -> {
                    val thres = _state.value.dewThres
                    when (warningSeverity) {
                        WarningSeverity.LOW -> {
                            _state.value = _state.value.copy(dewThres = thres.copy(first = value))
                        }
                        WarningSeverity.MED -> {
                            _state.value = _state.value.copy(dewThres = thres.copy(second = value))
                        }
                        WarningSeverity.HIGH -> {
                            _state.value = _state.value.copy(dewThres = thres.copy(third = value))
                        }
                    }
                }

                WarningType.RAIN -> {
                    val thres = _state.value.dewThres
                    when (warningSeverity) {
                        WarningSeverity.LOW -> {
                            _state.value = _state.value.copy(rainThres = thres.copy(first = value))
                        }
                        WarningSeverity.MED -> {
                            _state.value = _state.value.copy(rainThres = thres.copy(second = value))
                        }
                        WarningSeverity.HIGH -> {
                            _state.value = _state.value.copy(rainThres = thres.copy(third = value))
                        }
                    }
                }

                WarningType.WIND -> {
                    val thres = _state.value.dewThres
                    when (warningSeverity) {
                        WarningSeverity.LOW -> {
                            _state.value = _state.value.copy(windThres = thres.copy(first = value))
                        }
                        WarningSeverity.MED -> {
                            _state.value = _state.value.copy(windThres = thres.copy(second = value))
                        }
                        WarningSeverity.HIGH -> {
                            _state.value = _state.value.copy(windThres = thres.copy(third = value))
                        }
                    }
                }
            }
        }
    }
}