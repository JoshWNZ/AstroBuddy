package com.pilot.astrobuddy.presentation.object_display

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Constants
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
class ObjectDisplayViewModel @Inject constructor(
    private val getAstroObjectUseCase: GetAstroObjectUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    //initialise a mutable state
    private val _state = mutableStateOf(ObjectDisplayState())
    val state: State<ObjectDisplayState> = _state

    private var searchJob: Job? = null

    init {
        //get object name from the nav parameters
        val name = savedStateHandle.get<String>(Constants.PARAM_OBJ_NAME)

        if(name.isNullOrEmpty()){
            _state.value = ObjectDisplayState(
                error = "Ruh Roh; Object not passed"
            )
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch{
            //delay to avoid polling the api for every single text box update
            delay(100L)
            if (name != null) {
                getAstroObjectUseCase.getAstroObject(name).onEach{result ->
                    when(result){
                        is Resource.Success -> {
                            _state.value = ObjectDisplayState(astroObject= result.data?.get(0))
                        }

                        is Resource.Error -> {
                            _state.value = ObjectDisplayState(
                                error = result.message ?: "An unexpected error occurred"
                            )
                        }

                        is Resource.Loading -> {
                            _state.value = ObjectDisplayState(isLoading = true)
                        }
                    }
                }.launchIn(this)
            }
        }
    }

}

