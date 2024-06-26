package com.pilot.astrobuddy.presentation.location_search

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

    //initialise a mutable variable to store the current search query
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    //initialise a blank state
    private val _state = mutableStateOf(LocationSearchState())
    val state: State<LocationSearchState> = _state

    private var searchJob: Job? = null

    /**
    Function called whenever the user updates the search box
     */
    fun onSearch(query: String){
        //update the query var, cancel the current job, launch a new one
        _searchQuery.value = query

        if(query.isEmpty()){
            //_state.value = ObjectSearchState(objects = emptyList())
            getBookmarked()
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch{
            //delay to avoid polling the api for every single text box update
            delay(100L)
            getLocationsUseCase(query).onEach{result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = _state.value.copy(locations = result.data ?: emptyList())
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

    /**
     * Retrieve list of bookmarked locations
     */
    private fun getBookmarked(){
        searchJob?.cancel()

        viewModelScope.launch{
            getSavedLocUseCase.deleteUnsaved()
            _state.value = _state.value.copy(savedLocs = getSavedLocUseCase.getAllLocations())
        }
    }

    /**
    Save the provided location into the database, to be fetched by the location screen
     */
    fun saveLoc(loc: OMLocation){
        viewModelScope.launch{
            getSavedLocUseCase.insertLocation(loc)
        }
    }

    /**
    On initialisation, delete cached locations from the database which aren't bookmarked
    and get the remaining bookmarked locations
     */
    init{
        getBookmarked()
    }


}