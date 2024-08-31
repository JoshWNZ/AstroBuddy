package com.pilot.astrobuddy.presentation.object_display

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pilot.astrobuddy.common.Constants
import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.use_case.calculate_astro.CalculateDsoUseCase
import com.pilot.astrobuddy.domain.use_case.get_locations.GetSavedLocUseCase
import com.pilot.astrobuddy.domain.use_case.get_objects.GetAstroObjectUseCase
import com.pilot.astrobuddy.domain.use_case.get_objects.GetSavedObjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ObjectDisplayViewModel @Inject constructor(
    private val getAstroObjectUseCase: GetAstroObjectUseCase,
    private val getSavedObjectUseCase: GetSavedObjectUseCase,
    private val getSavedLocUseCase: GetSavedLocUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    //initialise a mutable state
    private val _state = mutableStateOf(ObjectDisplayState())
    val state: State<ObjectDisplayState> = _state

    //private val timer = Timer()

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

    fun getObjPosNow(){
        viewModelScope.launch{
            //Log.i("OBJPOS","get")
            //TODO make this based on location selected on home screen
            val tempLoc = getSavedLocUseCase.getAllSaved()[0]
            val loc = getSavedLocUseCase.getLocation(tempLoc)

            val obj = _state.value.astroObject

            if(obj!=null){
                val body = CalculateDsoUseCase.getCustomBody(
                    obj.RA.toString(),
                    obj.Dec.toString()
                )

                val curPos = CalculateDsoUseCase.calcObjPosition(
                    time = LocalDateTime.now().toString(),
                    latitude = loc.latitude.toString(),
                    longitude = loc.longitude.toString(),
                    elevation = loc.elevation,
                    body = body
                )

                _state.value = _state.value.copy(apparentPos = curPos)
            }

        }
        // timer.schedule(object : TimerTask() { override fun run(){} }, 0, 1000L)
    }

    fun getObjRiseSet(){
        viewModelScope.launch{
            Log.i("OBJPOS","get")
            //TODO make this based on location selected on home screen
            val tempLoc = getSavedLocUseCase.getAllSaved()[0]
            val loc = getSavedLocUseCase.getLocation(tempLoc)

            val obj = _state.value.astroObject

            if(obj!=null){
                val body = CalculateDsoUseCase.getCustomBody(
                    obj.RA.toString(),
                    obj.Dec.toString()
                )

                val riseSet = CalculateDsoUseCase.calcObjRiseSet(
                    time = LocalDateTime.now().toString(),
                    latitude = loc.latitude.toString(),
                    longitude = loc.longitude.toString(),
                    elevation = loc.elevation,
                    body = body
                )

                _state.value = _state.value.copy(riseSet = riseSet)
            }

        }
    }

    fun ceaseLoading() {
        _state.value = _state.value.copy(isLoading = false)
    }

}

