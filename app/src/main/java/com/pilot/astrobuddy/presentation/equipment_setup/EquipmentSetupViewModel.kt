package com.pilot.astrobuddy.presentation.equipment_setup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Constants
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment
import com.pilot.astrobuddy.domain.use_case.get_equipment.GetAstroEquipmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipmentSetupViewModel @Inject constructor(
    private val getAstroEquipmentUseCase: GetAstroEquipmentUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    //initialise a mutable state
    private val _state = mutableStateOf(EquipmentSetupState())
    val state: State<EquipmentSetupState> = _state

    init {
        //get object name from the nav parameters
        val id = savedStateHandle.get<String>(Constants.PARAM_ID_NAME)?.toInt()

        if (id != null) {
            if(id >= 0) {
                viewModelScope.launch {
                    getAstroEquipmentUseCase.getAstroEquipment(id).onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                val equip = result.data?.get(0)
                                _state.value = _state.value.copy(astroEquipment = equip,isLoading = false)
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
    }

    fun saveEquipment(
        id: Int?,
        setupName: String,
        scopeName: String,
        focalLength: Double,
        aperture: Double,
        modifier: Double,
        cameraName: String,
        vertPixels: Int,
        horiPixels: Int,
        sensorWidth: Double,
        sensorHeight: Double
    ){
        val pixelScale = calcPixelScale(vertPixels,horiPixels,sensorWidth,sensorHeight,focalLength)

        viewModelScope.launch{
            getAstroEquipmentUseCase.saveAstroEquipment(
                AstroEquipment(
                    id ?: 0,
                    setupName,
                    scopeName,
                    focalLength,
                    aperture,
                    modifier,
                    cameraName,
                    vertPixels,
                    horiPixels,
                    sensorWidth,
                    sensorHeight,
                    pixelScale
                )
            )
        }
    }

    private fun calcPixelScale(
        vertPixels: Int,
        horiPixels: Int,
        sensorWidth: Double,
        sensorHeight: Double,
        focalLength: Double
    ): Double{
        //calculate pixel size in microns
        val vertPixSize = (sensorHeight / vertPixels) * 1000
        val horiPixSize = (sensorWidth / horiPixels) * 1000

        //calculate pixel scale in arcSeconds
        val vertScale = 206 * vertPixSize / focalLength
        val horiScale = 206 * horiPixSize / focalLength

        return minOf(vertScale, horiScale)
    }

    fun throwError(){
        _state.value = _state.value.copy(
            error = "Invalid Inputs"
        )
    }

    fun clearError(){
        _state.value = _state.value.copy(
            error = ""
        )
    }

}