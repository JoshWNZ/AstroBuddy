package com.pilot.astrobuddy.presentation.settings_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            _state.value = SettingsState(settingsStore.getDaysFromDataStore()?: 5)
        }
    }

    fun updateDays(days: Int){
        viewModelScope.launch {
            settingsStore.saveDaysToDataStore(days)
            _state.value = SettingsState(settingsStore.getDaysFromDataStore())
        }
    }
}