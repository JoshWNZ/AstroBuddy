package com.pilot.astrobuddy.presentation.settings_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title={ Text("Preferences") },
                backgroundColor = Color.DarkGray,
                navigationIcon = {
                    //Button to navigate back
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                }
            )
        },
        content = {padding->
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(padding)
            ){
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.fillMaxSize()){
                    Text(
                        text = "Forecast Settings",
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Divider()
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text="Forecasted Days", style = MaterialTheme.typography.h6)
                        val days = state.forecastDays

                        Row(horizontalArrangement = Arrangement.SpaceEvenly){
                            Box(
                                modifier = Modifier
                                    .clickable {
                                        if (days > 1) {
                                            viewModel.updateDays(days - 1)
                                        }
                                    }
                                    .background(Color.Gray)
                                    .size(64.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text="-",
                                    style = MaterialTheme.typography.h3,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Box(
                                modifier = Modifier.size(64.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text=state.forecastDays.toString(),
                                    style = MaterialTheme.typography.h4
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clickable {
                                        if (days < 14) {
                                            viewModel.updateDays(days + 1)
                                        }
                                    }
                                    .background(Color.Gray)
                                    .size(64.dp),
                                contentAlignment = Alignment.Center

                            ){
                                Text(
                                    text="+",
                                    style = MaterialTheme.typography.h3
                                )
                            }
                        }

                    }
                    Divider()
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text="Units", style = MaterialTheme.typography.h6)
                        val unit = state.units

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.clickable{
                                viewModel.updateUnits()
                            }
                        ) {
                            val celsius = (unit=="C")
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (!celsius) {
                                            Color.Blue
                                        } else {
                                            Color.Gray
                                        }
                                    )
                                    .size(
                                        if (!celsius) {
                                            64.dp
                                        } else {
                                            60.dp
                                        }
                                    )
                                    .align(Alignment.CenterVertically)
                                    .border(
                                        color = Color.LightGray, width = if (!celsius) {
                                            2.dp
                                        } else {
                                            0.dp
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text="F",
                                    style = MaterialTheme.typography.h4
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (celsius) {
                                            Color.Blue
                                        } else {
                                            Color.Gray
                                        }
                                    )
                                    .size(
                                        if (celsius) {
                                            64.dp
                                        } else {
                                            60.dp
                                        }
                                    )
                                    .align(Alignment.CenterVertically)
                                    .border(
                                        color = Color.LightGray, width = if (celsius) {
                                            2.dp
                                        } else {
                                            0.dp
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text="C",
                                    style = MaterialTheme.typography.h4
                                )
                            }
                        }
                    }
                    Divider()
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text="Time Format", style = MaterialTheme.typography.h6)
                        val timeFormat = state.timeFormat

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.clickable{
                                viewModel.updateTimeFormat()
                            }
                        ) {
                            val military = (timeFormat=="24h")
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (!military) {
                                            Color.Blue
                                        } else {
                                            Color.Gray
                                        }
                                    )
                                    .size(
                                        if (!military) {
                                            64.dp
                                        } else {
                                            60.dp
                                        }
                                    )
                                    .align(Alignment.CenterVertically)
                                    .border(
                                        color = Color.LightGray, width = if (!military) {
                                            2.dp
                                        } else {
                                            0.dp
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text="12h",
                                    style = MaterialTheme.typography.h5
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (military) {
                                            Color.Blue
                                        } else {
                                            Color.Gray
                                        }
                                    )
                                    .size(
                                        if (military) {
                                            64.dp
                                        } else {
                                            60.dp
                                        }
                                    )
                                    .align(Alignment.CenterVertically)
                                    .border(
                                        color = Color.LightGray, width = if (military) {
                                            2.dp
                                        } else {
                                            0.dp
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text="24h",
                                    style = MaterialTheme.typography.h5
                                )
                            }
                        }
                    }
                    Divider()
                    Text(
                        text = "Object Settings",
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Divider()
                    Text(
                        text = "Equipment Settings",
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Divider()
                }
            }
        },
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    )
}