package com.pilot.astrobuddy.presentation.settings_screen

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.DataStoreManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


class SettingsViewModel @Inject constructor(
    //savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(SettingsState())
    val state: State<SettingsState> = _state

    private val dataStore = DataStoreManager(Application())

    init{
        viewModelScope.launch{
            _state.value = SettingsState(dataStore.getDaysFromDataStore()?: 5)
        }
    }

    fun updateDays(days: Int){
        viewModelScope.launch {
            dataStore.saveDaysToDataStore(days)
            _state.value = SettingsState(dataStore.getDaysFromDataStore()?:5)
        }
    }
}