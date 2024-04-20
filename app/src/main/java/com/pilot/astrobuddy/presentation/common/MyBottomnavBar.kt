package com.pilot.astrobuddy.presentation.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pilot.astrobuddy.R
import com.pilot.astrobuddy.presentation.Screen

@Composable
fun MyBottomNavBar(navController: NavController){
    val items = listOf(
        Screen.LocationSearchScreen,
        Screen.ObjectSearchScreen,
        Screen.HomeScreen,
        Screen.EquipmentScreen,
        Screen.SettingsScreen
    )


    BottomNavigation(backgroundColor = Color.DarkGray) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    when(items.indexOf(screen)){
                        0 -> Icon(Icons.Rounded.Cloud,contentDescription = null)
                        1 -> Icon(Icons.Rounded.Star,contentDescription = null)
                        2 -> Icon(Icons.Rounded.Home,contentDescription = null,modifier = Modifier.size(40.dp))
                        3 -> Icon(
                            painter = painterResource(id = R.drawable.scope),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        4 -> Icon(Icons.Rounded.Settings,contentDescription = null)
                    }
                       },
                label = {
                    when(items.indexOf(screen)){
                        0 -> Text("Weather")
                        1 -> Text("Objects")
                        2 -> Text("Home")
                        3 -> Text("Setups")
                        4 -> Text("Settings")
                    }
                },
                alwaysShowLabel = false,
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}