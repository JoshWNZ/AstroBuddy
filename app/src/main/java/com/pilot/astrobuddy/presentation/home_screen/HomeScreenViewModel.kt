package com.pilot.astrobuddy.presentation.home_screen

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
import com.pilot.astrobuddy.setings_store.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAstroEquipmentUseCase: GetAstroEquipmentUseCase,
    private val getSavedObjectUseCase: GetSavedObjectUseCase,
    private val getSavedLocUseCase: GetSavedLocUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val settingsStore: SettingsStore
): ViewModel() {
    //initialise a blank state
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    init{
        //cursed hack to pre-load the dataStore to avoid 500ms first query
        viewModelScope.launch{
            settingsStore.getDaysFromDataStore()
        }
    }

    fun fetchInformation(){
        //initialise lists to save data
        var astroEquipmentList: List<AstroEquipment> = emptyList()
        var savedLocationList: List<OMLocation> = emptyList()
        var savedObjectList: List<AstroObject> = emptyList()

        //fetch astro equipment
        viewModelScope.launch{
            val equipmentDefer = async{
                getAstroEquipmentUseCase.getAllAstroEquipment().first{ result ->
                    when(result){
                        is Resource.Success -> {
                            astroEquipmentList = result.data?:emptyList()
                            true
                        }
                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                error = result.message ?: "An unexpected error occurred"
                            )
                            false
                        }
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                            false
                        }
                    }
                }
            }

            val locationDefer = async{
                savedLocationList = getSavedLocUseCase.getAllLocations()
            }

            val objectsDefer = async{
                savedObjectList = getSavedObjectUseCase.getAllObjects()
            }

            awaitAll(equipmentDefer,locationDefer,objectsDefer)

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