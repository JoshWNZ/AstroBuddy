package com.pilot.astrobuddy.presentation.home_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.use_case.get_equipment.GetAstroEquipmentUseCase
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
    private val getForecastUseCase: GetForecastUseCase
): ViewModel() {
    //initialise a blank state
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state


    fun fetchInformation(){
        //initialise lists to save data
        var astroEquipmentList: List<AstroEquipment> = emptyList()
        var savedLocationList: List<OMLocation> = emptyList()
        var savedObjectList: List<AstroObject> = emptyList()

        //fetch astro equipment
        viewModelScope.launch{
            getAstroEquipmentUseCase.getAllAstroEquipment().onEach{ result ->
                when(result){
                    is Resource.Success -> {
                        astroEquipmentList = result.data?:emptyList()
                        Log.i("HOMELOAD","equipment fetched")
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

            savedLocationList = getSavedLocUseCase.getAllLocations()
            Log.i("HOMELOAD","locations fetched")

            savedObjectList = getSavedObjectUseCase.getAllObjects()
            Log.i("HOMELOAD","objects fetched")

            _state.value = _state.value.copy(
                isLoading = false,
                savedEquip = astroEquipmentList,
                savedLocs = savedLocationList,
                savedObjects = savedObjectList
                )
        }
    }

    fun fetchForecast(loc: OMLocation){

    }
}