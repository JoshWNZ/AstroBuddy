package com.pilot.astrobuddy.presentation.location_search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.pilot.astrobuddy.presentation.Screen

@Composable
fun LocDropDownMenu(navController: NavController){
    val opened = remember{ mutableStateOf(false) }
    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ){
        IconButton(onClick = {
            opened.value = true
        }) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "More Options"
            )
        }
    }
    DropdownMenu(
        expanded = opened.value,
        onDismissRequest = { opened.value = false }
    ) {
        DropdownMenuItem(onClick = { navController.navigate(Screen.SettingsScreen.route) }) {
            Text("Preferences")
        }
        DropdownMenuItem(onClick = { navController.navigate(Screen.AttributionScreen.route) }) {
            Text("Attribution")
        }
    }
}