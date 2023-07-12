package com.pilot.astrobuddy.presentation.forecast_display

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Grid4x4
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pilot.astrobuddy.domain.model.weatherapi.Astro
import com.pilot.astrobuddy.presentation.Screen
import com.pilot.astrobuddy.presentation.common.myBottomNavBar
import com.pilot.astrobuddy.presentation.forecast_display.components.ForecastCalendarItem
import com.pilot.astrobuddy.presentation.forecast_display.components.ForecastScrollerItem


@Composable
fun ForecastScreen(
    navController: NavController,
    viewModel: ForecastViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar ={
            TopAppBar(
                title={Text("Forecast")},
                backgroundColor = Color.DarkGray,
                actions = {
                    Row{
                        //menu button for decoration i suppose
                        Icon(
                            imageVector = if(state.calendar){Icons.Rounded.Grid4x4}else{Icons.Rounded.CalendarMonth},
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    viewModel.toggleCalendarView()
                                }
                                .size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                          },
                navigationIcon = {
                    //Button to navigate back
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                navController.navigate(Screen.LocationSearchScreen.route)
                            }
                    )
                }
            )
        },
        content = {padding->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(padding)
            ) {
                //show the forecast when it's available
                state.forecast?.let { fc ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Blue),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        var locName = ""
                        //if a real location and not a generated blank one (for raw coord entry)
                        if(viewModel.location.name.isNotEmpty() && viewModel.location.name!="_") {
                            locName = viewModel.location.name
                        }
                        Column(modifier = Modifier
                            .align(CenterVertically)
                            .padding(start = 5.dp)
                        ){
                            //Show name if available, and coordinates
                            if(locName.isNotEmpty()){
                                Text(
                                    text= locName,
                                    style = MaterialTheme.typography.body1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text= "${fc.latitude}, ${fc.longitude}",
                                    style = MaterialTheme.typography.body1,
                                )
                            }else{
                                Text(
                                    text= "${fc.latitude}, ${fc.longitude}",
                                    style = MaterialTheme.typography.body1,
                                )
                            }
                        }
                        //Button to save/bookmark location
                        Icon(
                            imageVector = Icons.Rounded.Bookmark,
                            tint = if(state.isSaved){Color.Yellow}else{Color.LightGray},
                            contentDescription = "SaveLoc",
                            modifier = Modifier
                                .padding(end = 32.dp)
                                .clickable {
                                    viewModel.toggleSaved(viewModel.location.id)
                                }
                                .align(CenterVertically)
                                .size(32.dp)
                        )
                        //Light pollution info
                        Column(modifier = Modifier
                            .align(CenterVertically)
                            .padding(end = 5.dp)
                        ){
                            val lightPollution = getLightPollution(fc.latitude.toString(),fc.longitude.toString())
                            Text(
                                text= "sqm: ${lightPollution.second}",
                                style = MaterialTheme.typography.body1
                            )
                            Text(
                                text= "bortle: ${lightPollution.first}",
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                    //Get the astronomical forecast
                    val curAstro: List<Astro> = state.astro.ifEmpty {
                        emptyList()
                    }
                    //pass forecast data into scrollable composable
                    if(state.calendar){
                        ForecastCalendarItem(fd = fc, astro = curAstro)
                    }else{
                        ForecastScrollerItem(fd = fc, astro = curAstro)
                    }
                }
                //display an error message if one is available
                if (state.error.isNotBlank()) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
                //display a spinning loading icon if the resource flow is still loading
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        },
        bottomBar = {
            myBottomNavBar(navController = navController)
        }
    )
}

/*
Helper function to get light pollution info from some magical method that i will write
sometime maybe
 */
private fun getLightPollution(lat: String, long: String): Pair<Int,Double>{
    val goawaywarning = lat+long+"a"
    goawaywarning+""
    return Pair(0,0.0)
}