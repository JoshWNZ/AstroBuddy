package com.pilot.astrobuddy.presentation.object_search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.use_case.GetAstroObjectUseCase
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
        Log.i("SEARCH", "initiate object search")
        //update the query var, cancel the current job, launch a new one
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch{
            //delay to avoid polling the api for every single text box update
            delay(100L)
            getAstroObjectUseCase.searchAstroObjects(query).onEach{result ->
                Log.i("SEARCH","its happening poggers")
                when(result){
                    is Resource.Success -> {
                        _state.value = ObjectSearchState(objects = result.data ?: emptyList())
                        Log.i("SEARCH","result:"+result.data?.map{e->e.Name})
                    }
                    is Resource.Error -> {
                        _state.value = ObjectSearchState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                        Log.i("SEARCH","error:"+result.message)
                    }
                    is Resource.Loading -> {
                        _state.value = ObjectSearchState(isLoading = true)
                        Log.i("SEARCH","Loading")
                    }
                }
            }.launchIn(this)
        }
    }

}