package com.pilot.astrobuddy.presentation.forecast_display

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Constants
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.astro_forecast.Astro
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.model.warning.WarningSeverity
import com.pilot.astrobuddy.domain.model.warning.WarningType
import com.pilot.astrobuddy.domain.use_case.calculate_astro.CalculateSunMoonUseCase
import com.pilot.astrobuddy.domain.use_case.calculate_lightpollution.CalculateLightPollUseCase
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
    private val getSavedLocUseCase: GetSavedLocUseCase,
    private val getLightPollUseCase: CalculateLightPollUseCase,
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
            //get forecasts
            getForecast(lat, long, location.elevation)
        }

    }

    /*
    Function to get forecast and update the state accordingly
     */
    private fun getForecast(latitude: String, longitude: String, elevation: Double){
        var days: Int
        viewModelScope.launch{
            days = settingsStore.getDaysFromDataStore()
            getForecastUseCase(latitude,longitude,days).onEach{result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = _state.value.copy(forecast = result.data, isLoading = false, error = "")
                        result.data?.let {
                            calculateAstro(latitude, longitude, elevation, it)
                            getLightPol(latitude, longitude)
                        }
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

    fun toggleCalendarView(){
        viewModelScope.launch{
            _state.value = _state.value.copy(calendar = !_state.value.calendar)
        }
    }

    private fun calculateAstro(latitude: String, longitude: String, elevation: Double, forecast: OMForecast){
        val days = forecast.hourly.time.size / 24

        val astros = ArrayList<Astro>()
        for(i in 0 until days){
            val sunRiseSet = CalculateSunMoonUseCase.calculateSunRiseSet(
                latitude = latitude,
                longitude = longitude,
                elevation = elevation,
                time = forecast.hourly.time[i*24]
            )

            val moonRiseSet = CalculateSunMoonUseCase.calculateMoonRiseSet(
                latitude = latitude,
                longitude = longitude,
                elevation = elevation,
                time = forecast.hourly.time[i*24]
            )

            val moonIllumPhase = CalculateSunMoonUseCase.calculateMoonIllumPhase(
                time = forecast.hourly.time[i*24]
            )

            val civilDark = CalculateSunMoonUseCase.calcCivilDark(
                latitude = latitude,
                longitude = longitude,
                elevation = elevation,
                time = forecast.hourly.time[i*24]
            )

            val nauticalDark = CalculateSunMoonUseCase.calcNauticalDark(
                latitude = latitude,
                longitude = longitude,
                elevation = elevation,
                time = forecast.hourly.time[i*24]
            )

            val astroDark = CalculateSunMoonUseCase.calcAstroDark(
                latitude = latitude,
                longitude = longitude,
                elevation = elevation,
                time = forecast.hourly.time[i*24]
            )

            val curAstro = Astro(
                moonIllumPhase.first,
                moonIllumPhase.second,
                moonRiseSet,
                sunRiseSet,
                civilDark,
                nauticalDark,
                astroDark
            )
            astros.add(curAstro)
        }

        _state.value = _state.value.copy(astro = astros)
    }

    fun getTimeFormat(): String{
        var result = ""
        viewModelScope.launch{
            result = settingsStore.getTimeFormatFromDataStore()
        }
        return result
    }

    fun getUnits(): String{
        var result = ""
        viewModelScope.launch{
            result = settingsStore.getUnitsFromDataStore()
        }
        return result
    }

    fun getWarningThreshold(warningType: WarningType,warningSeverity: WarningSeverity):Int{
        var result = 0
        viewModelScope.launch{
            result = settingsStore.getThresFromDataStore(warningType,warningSeverity)
        }
        return result
    }

    private fun getLightPol(lat: String, long: String){
        var sqm: Pair<Double,Double> = Pair(0.0,0.0)
        viewModelScope.launch{
            sqm = getLightPollUseCase.calcLightPol(lat.toDouble(),long.toDouble())
        }
        Log.i("VIEWMODEL",sqm.toString())
        val avg = (sqm.first + sqm.second)/2
        val bortle =  getLightPollUseCase.calcBortleFromSQM(avg)
        _state.value = _state.value.copy(sqm=sqm,bortle=bortle)
        //return (sqm.first+sqm.second) / 2
    }

    fun renameLocation(newName: String){
        viewModelScope.launch{
            getSavedLocUseCase.renameLocation(id = location.id, name = newName)
        }
    }
}