package com.pilot.astrobuddy.presentation.object_display

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Constants
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.use_case.get_objects.GetAstroObjectUseCase
import com.pilot.astrobuddy.domain.use_case.get_objects.GetSavedObjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObjectDisplayViewModel @Inject constructor(
    private val getAstroObjectUseCase: GetAstroObjectUseCase,
    private val getSavedObjectUseCase: GetSavedObjectUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    //initialise a mutable state
    private val _state = mutableStateOf(ObjectDisplayState())
    val state: State<ObjectDisplayState> = _state

    init {
        //get object name from the nav parameters
        val name = savedStateHandle.get<String>(Constants.PARAM_OBJ_NAME)

        if(name.isNullOrEmpty()){
            _state.value = _state.value.copy(
                error = "Ruh Roh; Object not passed"
            )
        }

        viewModelScope.launch{
            if (name != null) {
                getAstroObjectUseCase.getAstroObject(name).onEach{result ->
                    when(result){
                        is Resource.Success -> {
                            val obj = result.data?.get(0)
                            _state.value = _state.value.copy(astroObject= obj)
                            if(getSavedObjectUseCase.getAllObjects().contains(obj)){
                                _state.value = _state.value.copy(isSaved = true)
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
                }.launchIn(this)
            }
        }
    }

    fun toggleSave(){
        viewModelScope.launch{
            if(_state.value.isSaved){
                _state.value = _state.value.copy(isSaved = false)
                getSavedObjectUseCase.deleteObject(_state.value.astroObject?.Name ?:"")
            }else{
                _state.value = _state.value.copy(isSaved = true)
                _state.value.astroObject?.let { getSavedObjectUseCase.insertObject(it) }
            }
        }
    }

}

