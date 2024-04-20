package com.pilot.astrobuddy.presentation.home_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.use_case.get_equipment.GetAstroEquipmentUseCase
import com.pilot.astrobuddy.domain.use_case.get_forecast.GetAstroUseCase
import com.pilot.astrobuddy.domain.use_case.get_forecast.GetForecastUseCase
import com.pilot.astrobuddy.domain.use_case.get_locations.GetSavedLocUseCase
import com.pilot.astrobuddy.domain.use_case.get_objects.GetSavedObjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAstroEquipmentUseCase: GetAstroEquipmentUseCase,
    private val getSavedObjectUseCase: GetSavedObjectUseCase,
    private val getSavedLocUseCase: GetSavedLocUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val getAstroUseCase: GetAstroUseCase
): ViewModel() {
    //initialise a blank state
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    fun fetchSavedEquipment() /*:List<AstroEquipment>*/{
        viewModelScope.launch{
            getAstroEquipmentUseCase.getAllAstroEquipment().onEach{ result ->
                when(result){
                    is Resource.Success -> {
                        result.data ?: emptyList()
                        _state.value = _state.value.copy(savedEquip = result.data ?: emptyList())
                        Log.i("HOMELOAD","equipment fetched")
                        Log.i("HOMELOAD",_state.value.savedEquip[0].setupName)
                        _state.value = _state.value.copy(isLoading = false)
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }

    fun fetchSavedLocations(){
        viewModelScope.launch{
            _state.value = _state.value.copy(savedLocs = getSavedLocUseCase.getAllLocations())
            Log.i("HOMELOAD","locations fetched")
            Log.i("HOMELOAD",_state.value.savedLocs[0].name)
        }
    }

    fun fetchSavedObjects(){
        viewModelScope.launch{
            _state.value = _state.value.copy(savedObjects = getSavedObjectUseCase.getAllObjects())
            Log.i("HOMELOAD","objects fetched")
            Log.i("HOMELOAD",_state.value.savedObjects[0].Name)
        }
    }

    fun fetchForecast(loc: OMLocation){}
}