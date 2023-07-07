package com.pilot.astrobuddy.presentation.location_search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.use_case.get_locations.GetLocationsUseCase
import com.pilot.astrobuddy.domain.use_case.get_locations.GetSavedLocUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase,
    private val getSavedLocUseCase: GetSavedLocUseCase
    //savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf(LocationSearchState())
    val state: State<LocationSearchState> = _state

    var savedLocs: List<OMLocation> = emptyList()

    private var searchJob: Job? = null

    fun onSearch(query: String){
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch{
            delay(50L)
            getLocationsUseCase(query).onEach{result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = LocationSearchState(locations = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _state.value = LocationSearchState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = LocationSearchState(isLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }

    init{
        viewModelScope.launch{
            getSavedLocUseCase.deleteUnsaved()
            savedLocs = getSavedLocUseCase.getAllLocations()
        }
    }

    fun saveLoc(loc: OMLocation){
        viewModelScope.launch{
            getSavedLocUseCase.insertLocation(loc)
            Log.i("locSaved", loc.id.toString())
        }
    }


}