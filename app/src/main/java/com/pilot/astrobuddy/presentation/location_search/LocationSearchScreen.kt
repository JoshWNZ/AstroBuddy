package com.pilot.astrobuddy.presentation.location_search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.presentation.Screen
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar
import com.pilot.astrobuddy.presentation.location_search.components.LocDropDownMenu
import com.pilot.astrobuddy.presentation.location_search.components.LocationSearchItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationSearchScreen(
    navController: NavController,
    viewModel: LocationSearchViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val savedLocs = viewModel.savedLocs

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title={Text("Forecast")},
                backgroundColor = Color.DarkGray,
                actions = {
                    //Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
                    LocDropDownMenu(navController)
                }
            )
        },
        content = {padding->
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(padding)
            ){
                Column (
                    modifier = Modifier.fillMaxSize()
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        //Text box for search input
                        TextField(
                            value = viewModel.searchQuery.value,
                            onValueChange = viewModel::onSearch,
                            modifier = Modifier
                                .weight(0.85f),
                            placeholder = {
                                Text(text = "Enter a location or decimal coordinates")
                            }
                        )
                        //initialise states n scopes for location services
                        val locPermissionState = rememberPermissionState(
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        val scope = rememberCoroutineScope()
                        val context = LocalContext.current
                        val locClient = remember{
                            LocationServices.getFusedLocationProviderClient(context)
                        }

                        //Button to get current location
                        IconButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(CenterVertically)
                                .weight(0.15f)
                                .clip(CircleShape)
                                .background(Color.Blue),
                            content = {
                                Icon(imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = "Location pin")
                            },
                            onClick = {
                                if(locPermissionState.status.isGranted){
                                    //launch a coroutine to get current location
                                    scope.launch {
                                        //get the location from the google api
                                        val result = locClient.getCurrentLocation(
                                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                                            CancellationTokenSource().token,
                                        ).await()
                                        //generate a semi-unique id from the location info
                                        val id = ((result.latitude + result.longitude) * result.accuracy)
                                        //initialise a new location object with the fetched info
                                        val curLoc = OMLocation(
                                            "", "", "", "", "", "",
                                            result.altitude, id.toInt(), result.latitude, result.longitude, "User Location"
                                        )
                                        //save the location and navigate to the forecast screen as usual
                                        viewModel.saveLoc(curLoc)
                                        navController.navigate(Screen.ForecastScreen.route+"/${curLoc.id}")
                                    }
                                }else{
                                    //launch permission request
                                    locPermissionState.launchPermissionRequest()
                                }

                            }
                        )

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ){
                        //if the user is inputting coordinates, show them as an option
                        val query = viewModel.searchQuery.value
                        if(query.isNotBlank()){
                            if(query[0].isDigit() || query[0]=='-'){
                                if(query.contains(",")){
                                    val arr = query.split(",")
                                    Log.i("SPLIT", arr[0])
                                    Log.i("SPLIT", arr[1])
                                    //TODO make this work properly
                                    val coordLoc = OMLocation(
                                        admin1 = "",
                                        admin2 = "",
                                        admin3 = "",
                                        admin4 = "",
                                        country = "",
                                        country_code = "",
                                        elevation = -1.0,
                                        id = 215,
                                        latitude = 0.0,//arr[0].toDouble(),
                                        longitude = 0.0,//arr[1].toDouble(),
                                        name = ""
                                    )
                                    item {
                                        LocationSearchItem(
                                            location = coordLoc,
                                            onItemClick = {
                                                viewModel.saveLoc(coordLoc)
                                                navController.navigate(Screen.ForecastScreen.route+"/${coordLoc.id}")
                                            })
                                    }
                                }
                            }
                        }
                        //if the user is yet to input a query, and there are any bookmarked locations
                        if(query.isBlank() && savedLocs.isNotEmpty()){
                            //display all the bookmarked locations
                            items(savedLocs){loc->
                                LocationSearchItem(
                                    location = loc,
                                    onItemClick = {
                                        viewModel.saveLoc(loc)
                                        navController.navigate(Screen.ForecastScreen.route+"/${loc.id}")
                                    }
                                )
                            }
                        }
                        //show fetched locations from the user query
                        items(state.locations){loc->
                            LocationSearchItem(
                                location = loc,
                                onItemClick = {
                                    //save the location into the database
                                    viewModel.saveLoc(loc)
                                    //navigate to the forecast screen and pass the loc id
                                    navController.navigate(Screen.ForecastScreen.route+"/${loc.id}")
                                }
                            )
                        }

                    }
                    //display an error message if needed
                    if(state.error.isNotBlank()){
                        Text(
                            text=state.error,
                            color = MaterialTheme.colors.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        )
                    }
                    //display a loading icon when applicable
                    if(state.isLoading){
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                }
            }
        },
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    )
}

