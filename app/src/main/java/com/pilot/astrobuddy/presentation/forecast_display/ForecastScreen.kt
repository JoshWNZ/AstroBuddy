package com.pilot.astrobuddy.presentation.forecast_display

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Grid4x4
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pilot.astrobuddy.domain.model.astro_forecast.Astro
import com.pilot.astrobuddy.domain.model.warning.WarningSeverity
import com.pilot.astrobuddy.domain.model.warning.WarningType
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar
import com.pilot.astrobuddy.presentation.forecast_display.components.ForecastScrollerItem
import kotlinx.coroutines.launch
import kotlin.math.round

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ForecastScreen(
    navController: NavController,
    viewModel: ForecastViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val interactionSource = remember { MutableInteractionSource() }

    var curName by remember { mutableStateOf(viewModel.location.name)}
    var editing by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var showDialog by remember { mutableStateOf(false) }

    var locName = ""
    //if a real location and not a generated blank one (for raw coord entry)
    if(viewModel.location.name.isNotEmpty() && viewModel.location.name!="_") {
        locName = viewModel.location.name
    }
    curName = locName

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                    } },
                navigationIcon = {
                    //Button to navigate back
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
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
                            .background(Color.Blue)
                            .height(44.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,

                    ){
                        val elevationString: String = if(viewModel.getUnits()=="C"){
                            "${fc.elevation}m"
                        }else{
                            "${round(fc.elevation*3.28084)}ft"
                        }

                        Column(modifier = Modifier
                            .align(CenterVertically)
                            .padding(start = 5.dp)
                        ){
                            //Show name if available, and coordinates
                            if(locName.isNotEmpty()){
                                Row(){
                                    if(!editing){
                                        Text(
                                            text= curName,
                                            style = MaterialTheme.typography.body1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        if(viewModel.location.country.isEmpty() && viewModel.location.admin1.isNullOrEmpty() && state.isSaved){
                                            IconButton(
                                                onClick = { editing = true },
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .padding(start = 8.dp)
                                            ) {
                                                Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit Custom Name")
                                            }
                                        }
                                    }else{
                                        TextField(
                                            value = curName,
                                            onValueChange = {curName = it},
                                            modifier = Modifier.height(44.dp).focusRequester(focusRequester = focusRequester),
                                            textStyle = MaterialTheme.typography.body1,
                                            singleLine = true
                                        )
                                        IconButton(onClick = {
                                            viewModel.renameLocation(curName)
                                            navController.previousBackStackEntry?.savedStateHandle?.set("updatedLoc",true)
                                            editing = false
                                        }
                                        ) {
                                            Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = "Confirm new name")
                                        }
                                        IconButton(onClick = {
                                            curName = locName
                                            editing = false
                                        }
                                        ) {
                                            Icon(imageVector = Icons.Rounded.Cancel, contentDescription = "Cancel new name")
                                        }
                                    }
                                }
                                Text(
                                    text= "(${fc.latitude}, ${fc.longitude}), $elevationString",
                                    style = MaterialTheme.typography.body2,
                                )

                            }else{
                                Text(
                                    text= "(${fc.latitude}, ${fc.longitude})",
                                    style = MaterialTheme.typography.body1,
                                )
                                Text(
                                    text= elevationString,
                                    style = MaterialTheme.typography.body2,
                                )
                            }
                        }
                        //Button to save/bookmark location
                        Icon(
                            imageVector = Icons.Rounded.Bookmark,
                            tint = if(state.isSaved){Color.Yellow}else{Color.LightGray},
                            contentDescription = "SaveLoc",
                            modifier = Modifier
                                .padding(end = 106.dp)
                                .clickable {
                                    if (state.isSaved) {
                                        if (viewModel.location.country.isEmpty() && viewModel.location.admin1.isNullOrEmpty()) {
                                            showDialog = true
                                        } else {
                                            viewModel.toggleSaved(viewModel.location.id)
                                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                                "updatedLoc", true
                                            )
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Removed from favourites")
                                            }
                                        }
                                    } else {
                                        viewModel.toggleSaved(viewModel.location.id)
                                        navController.previousBackStackEntry?.savedStateHandle?.set(
                                            "updatedLoc", true
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Added to favourites")
                                        }
                                    }
                                }
                                .align(CenterVertically)
                                .size(36.dp)
                        )
                        //Light pollution info
                        Column(modifier = Modifier
                            .align(CenterVertically)
                            .padding(end = 5.dp)
                        ){
                            val sqm = state.sqm
                            val bortle = state.bortle
                            Text(
                                text= "sqm: ${"%.2f".format((sqm.first+sqm.second)/2)}",
                                style = MaterialTheme.typography.body1
                            )
                            Text(
                                text= "bortle: $bortle",
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                    //Get the astronomical forecast
                    val curAstro: List<Astro> = state.astro.ifEmpty {
                        Log.i("Empty", "No astro today :(")
                        emptyList()
                    }

                    //pass forecast data into scrollable composable
                    ForecastScrollerItem(
                        fd = fc,
                        astro = curAstro,
                        timeFormat = viewModel.getTimeFormat(),
                        units = viewModel.getUnits(),
                        dewThres = Triple(
                            viewModel.getWarningThreshold(WarningType.DEW, WarningSeverity.LOW),
                            viewModel.getWarningThreshold(WarningType.DEW, WarningSeverity.MED),
                            viewModel.getWarningThreshold(WarningType.DEW, WarningSeverity.HIGH)
                        ),
                        windThres = Triple(
                            viewModel.getWarningThreshold(WarningType.WIND, WarningSeverity.LOW),
                            viewModel.getWarningThreshold(WarningType.WIND, WarningSeverity.MED),
                            viewModel.getWarningThreshold(WarningType.WIND, WarningSeverity.HIGH)
                        ),
                        rainThres = Triple(
                            viewModel.getWarningThreshold(WarningType.RAIN, WarningSeverity.LOW),
                            viewModel.getWarningThreshold(WarningType.RAIN, WarningSeverity.MED),
                            viewModel.getWarningThreshold(WarningType.RAIN, WarningSeverity.HIGH)
                        )
                    )
//                    if(state.calendar){
//                        ForecastCalendarItem(fd = fc, astro = curAstro)
//                    }else{
//
//                    }
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

                if(showDialog){
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Removing Custom Location") },
                        text = { Text("Are you sure you want to remove this custom location?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.toggleSaved(viewModel.location.id)
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "updatedLoc", true
                                    )
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Removed from favourites")
                                    }
                                    showDialog = false
                                }
                            ){
                                Text("Remove")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDialog = false }
                            ){
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        },
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    )
}