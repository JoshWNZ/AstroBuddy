package com.pilot.astrobuddy.presentation.forecast_display

import android.util.Log
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ForecastState())
    val state: State<ForecastState> = _state

    var location =  OMLocation("","","","","","",0.0,0,0.0,0.0,"")

    init {
        val id = savedStateHandle.get<String>(Constants.PARAM_ID_NAME)
        Log.i("locLoaded",id.toString())
        viewModelScope.launch {
            if (id != null) {
                location = getSavedLocUseCase.getLocation(id.toInt())
            }
            if (getSavedLocUseCase.getAllSaved().contains(location.id)) {
                _state.value = _state.value.copy(isSaved = true)
            }
            val lat = location.latitude.toString()
            val long = location.longitude.toString()

            getForecast(lat, long)
            getAstro(lat, long)
        }

    }

    private fun getForecast(latitude: String, longitude: String){
        getForecastUseCase(latitude,longitude).onEach{result ->
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
                    Log.i("bork",result.message?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isAstroLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

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