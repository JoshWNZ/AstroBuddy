package com.pilot.astrobuddy.presentation.forecast_display

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Constants
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.use_case.get_forecast.GetAstroUseCase
import com.pilot.astrobuddy.domain.use_case.get_forecast.GetForecastUseCase
import com.pilot.astrobuddy.domain.use_case.get_locations.GetSavedLocUseCase
import com.pilot.astrobuddy.setings_store.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val getAstroUseCase: GetAstroUseCase,
    private val getSavedLocUseCase: GetSavedLocUseCase,
    savedStateHandle: SavedStateHandle,
    private val settingsStore: SettingsStore
) : ViewModel() {
    //initialise a mutable forecast
    private val _state = mutableStateOf(ForecastState())
    val state: State<ForecastState> = _state

    //initialise an empty location for if the api call fails
    var location =  OMLocation("","","","","","",0.0,0,0.0,0.0,"")

    /*
    initialise the state information
     */
    init {
        //get location id from the nav parameters
        val id = savedStateHandle.get<String>(Constants.PARAM_ID_NAME)

        //launch a coroutine
        viewModelScope.launch {
            //fetch location from database
            if (id != null) {
                location = getSavedLocUseCase.getLocation(id.toInt())
            }
            //check if the location is already bookmarked
            if (getSavedLocUseCase.getAllSaved().contains(location.id)) {
                _state.value = _state.value.copy(isSaved = true)
            }
            //fetch lat/long from location
            val lat = location.latitude.toString()
            val long = location.longitude.toString()
            //launch coroutines to get weather and astro forecasts
            launch{
                getForecast(lat, long)
            }
            launch{
                getAstro(lat, long)
            }
        }

    }

    /*
    Function to get forecast and update the state accordingly
     */
    private fun getForecast(latitude: String, longitude: String){
        var days = 5
        viewModelScope.launch{
            days = settingsStore.getDaysFromDataStore()
            getForecastUseCase(latitude,longitude,days).onEach{result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = _state.value.copy(forecast = result.data, isLoading = false, error = "")
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
            }.launchIn(viewModelScope)
        }


    }

    /*
    Function to get astronomical forecast and update state accordingly
     */
    private fun getAstro(latitude: String, longitude: String){
        getAstroUseCase(latitude,longitude).onEach{result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(astro = result.data?: emptyList(), isAstroLoading = false, astroError = "")
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        astroError = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isAstroLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    /*
    Save or unsave a location
     */
    fun toggleSaved(id: Int){
        viewModelScope.launch{
            if(_state.value.isSaved){
                getSavedLocUseCase.unsaveLocation(id)
                _state.value = _state.value.copy(isSaved = false)
            }else{
                getSavedLocUseCase.saveLocation(id)
                _state.value = _state.value.copy(isSaved = true)
            }
        }
    }
}