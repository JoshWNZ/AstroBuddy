package com.pilot.astrobuddy.presentation.home_screen

data class HomeScreenState (
    val isLoading: Boolean = false,
    val error: String = "",
    val editing: Boolean = false
)