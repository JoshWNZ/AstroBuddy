package com.pilot.astrobuddy.presentation.object_search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.use_case.get_objects.GetAstroObjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObjectSearchViewModel @Inject constructor(
    private val getAstroObjectUseCase: GetAstroObjectUseCase
) : ViewModel() {

    //initialise a mutable variable to store the current search query
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    //initialise a blank state
    private val _state = mutableStateOf(ObjectSearchState())
    val state: State<ObjectSearchState> = _state

    private var searchJob: Job? = null

    /**
    Function called whenever the user updates the search box
     */
    fun onSearch(query: String){
        //update the query var, cancel the current job, launch a new one
        _searchQuery.value = query
        searchJob?.cancel()

        if(query.isEmpty()){
            _state.value = ObjectSearchState(objects = emptyList())
            return
        }

        searchJob = viewModelScope.launch{
            //delay to avoid polling the api for every single text box update
            delay(100L)
            getAstroObjectUseCase.searchAstroObjects(query).onEach{result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = ObjectSearchState(objects = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _state.value = ObjectSearchState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = ObjectSearchState(isLoading = true)
                    }
                }
            }.launchIn(this)
        }
    }

}