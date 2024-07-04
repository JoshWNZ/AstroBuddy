package com.pilot.astrobuddy.presentation.settings_screen

data class SettingsState (
    var forecastDays: Int = 0,
    var units: String = "",
    var timeFormat: String = "",
    var dewThres: Triple<Int,Int,Int> = Triple(0,0,0),
    var windThres: Triple<Int,Int,Int> = Triple(0,0,0),
    var rainThres: Triple<Int,Int,Int> = Triple(0,0,0)
)