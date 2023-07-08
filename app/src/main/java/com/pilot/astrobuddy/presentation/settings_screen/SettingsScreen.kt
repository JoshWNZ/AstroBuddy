package com.pilot.astrobuddy.presentation.settings_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


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
                backgroundColor = Color.DarkGray
            )
        },
        content = {padding->
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(padding)
            ){
                Column(modifier = Modifier.fillMaxSize()){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text="Forecasted Days")
                        val days = state.forecastDays

                        Row(horizontalArrangement = Arrangement.SpaceEvenly){
                            Box(
                                modifier = Modifier.clickable{
                                    if(days>0){
                                        viewModel.updateDays(days-1)
                                    }
                                }.background(Color.Gray)
                            ){
                                Text("-",style = MaterialTheme.typography.h2)
                            }
                            Box{
                                Text(text=state.forecastDays.toString())
                            }
                            Box(
                                modifier = Modifier.clickable{
                                    if(days<12){
                                        viewModel.updateDays(days+1)
                                    }
                                }.background(Color.Gray)
                            ){
                                Text("+",style = MaterialTheme.typography.h2)
                            }
                        }

                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(backgroundColor = Color.DarkGray) {
                Text("Example Nav Bar", textAlign = TextAlign.Center)
            }
        }

    )
}