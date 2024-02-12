package com.pilot.astrobuddy.presentation.equipment_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.use_case.get_equipment.GetAstroEquipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipmentScreenViewModel @Inject constructor(
    private val getAstroEquipmentUseCase: GetAstroEquipmentUseCase
): ViewModel() {
    //initialise a blank state
    private val _state = mutableStateOf(EquipmentScreenState())
    val state: State<EquipmentScreenState> = _state

    private fun onRefresh(){
        viewModelScope.launch{
            //delay to avoid polling the api for every single text box update
            delay(100L)
            getAstroEquipmentUseCase.getAllAstroEquipment().onEach{result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = _state.value.copy(objects = result.data ?: emptyList())
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

    init{
        onRefresh()
    }

    fun toggleEdit(){
        _state.value = _state.value.copy(
            editing = !_state.value.editing
        )
    }

    fun deleteEquip(id: Int){
        viewModelScope.launch{
            getAstroEquipmentUseCase.unsaveAstroEquipment(id)
        }
        onRefresh()
    }
}