package com.pilot.astrobuddy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pilot.astrobuddy.presentation.forecast_display.ForecastScreen
import com.pilot.astrobuddy.presentation.location_search.LocationSearchScreen
import com.pilot.astrobuddy.presentation.settings_screen.SettingsScreen
import com.pilot.astrobuddy.presentation.ui.theme.AstroBuddyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AstroBuddyTheme {
                Surface(color = MaterialTheme.colors.background){
                    //initialise navcontroller and navhost
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.LocationSearchScreen.route
                    ){
                        composable(
                            route = Screen.LocationSearchScreen.route
                        ){
                            LocationSearchScreen(navController)
                        }
                        composable(
                            route = Screen.ForecastScreen.route + "/{id}"
                        ){
                            ForecastScreen(navController)
                        }
                        composable(
                            route = Screen.SettingsScreen.route
                        ){
                            SettingsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

