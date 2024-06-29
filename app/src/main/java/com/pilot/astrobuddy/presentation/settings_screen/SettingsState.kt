package com.pilot.astrobuddy.presentation.settings_screen

data class SettingsState (
    var forecastDays: Int = 0,
    var units: String = "",
    var timeFormat: String = ""
)