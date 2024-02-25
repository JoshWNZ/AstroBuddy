package com.pilot.astrobuddy.presentation.home_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    //private val getAstroEquipmentUseCase: GetAstroEquipmentUseCase
): ViewModel() {
    //initialise a blank state
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    init{

    }
}