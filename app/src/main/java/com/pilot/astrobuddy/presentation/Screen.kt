package com.pilot.astrobuddy.presentation

sealed class Screen(val route: String){
    object LocationSearchScreen: Screen("location_search_screen")
    object ForecastScreen: Screen("forecast_screen")
    object SettingsScreen : Screen("settings_screen")
    object AttributionScreen : Screen("attribution_screen")
    object ObjectSearchScreen : Screen("object_search_screen")
    object ObjectDisplayScreen : Screen("object_display_screen")
    object EquipmentScreen : Screen("equipment_screen")
    object EquipmentSetupScreen : Screen("equipment_setup_screen")
    object HomeScreen : Screen("home_screen")

}
