package com.pilot.astrobuddy.presentation

sealed class Screen(val route: String){
    object LocationSearchScreen: Screen("location_search_screen")
    object ForecastScreen: Screen("forecast_screen")
    object SettingsScreen : Screen("settings_screen")
}
