package com.pilot.astrobuddy.presentation.object_display

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistRemove
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pilot.astrobuddy.domain.model.astro_objects.ObjDefinitions
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar
import com.pilot.astrobuddy.presentation.object_display.components.ObjectImageFromHiPS
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

const val imageScale = 200

@Composable
fun ObjectDisplayScreen(
    navController: NavController,
    viewModel: ObjectDisplayViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val scrollState = rememberScrollState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        topBar ={
            TopAppBar(
                title={ state.astroObject?.let { Text(it.Name) } },
                backgroundColor = Color.DarkGray,
                actions = {
                    Icon(
                        imageVector = if(state.isSaved){Icons.Rounded.PlaylistRemove}
                                        else{ Icons.AutoMirrored.Rounded.PlaylistAdd },
                        contentDescription = null,
                        tint = if(state.isSaved){Color.Yellow}
                                else{Color.LightGray},
                        modifier = Modifier
                            .clickable {
                                viewModel.toggleSave()

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (state.isSaved) {
                                            "Removed from watchlist"
                                        } else {
                                            "Added to watchlist"
                                        }
                                    )
                                }
                            }
                            .size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                },
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
                    .padding(padding)
                    .background(MaterialTheme.colors.background)
                    .verticalScroll(scrollState)
            ) {
                //show the object info when it's available
                state.astroObject?.let { obj ->
                    viewModel.ceaseLoading()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ){
                            val maxSize = obj.MajAx?:"60"
                            val fov = ((maxSize.toDouble()/60)*1.5).toString()
                            val bgFov = ((maxSize.toDouble()/60)*5).toString()

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((imageScale * 1.2).dp),
                                contentAlignment = Alignment.Center
                            ){
                                //Image
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .blur(5.dp, 5.dp)
                                ){
                                    ObjectImageFromHiPS(fov = bgFov, obj = obj.Name, bg = true)
                                    Box(modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color.Black.copy(alpha = 0.25f))
                                    )
                                }

                                //Image
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(imageScale.dp)
                                        .border(width = 3.dp, color = Color.Black)
                                ){
                                    ObjectImageFromHiPS(fov = fov, obj = obj.Name)
                                }
                            }


                            Divider()

                            //Common Name
                            if(!obj.CommonNames.isNullOrEmpty()){
                                val name = obj.CommonNames.replace(",", ", ")
                                Box(
                                    modifier = Modifier.padding(all = 8.dp),
                                    contentAlignment = Alignment.CenterStart
                                ){
                                    Text(name, style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                            //Divider()

                            //IDs
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Center)
                                ){
                                    if(!obj.M.isNullOrEmpty()){
                                        Text("M${obj.M.trimStart('0')}    ")
                                    }
                                    if(!obj.NGC.isNullOrEmpty()){
                                        Text("NGC${obj.NGC}    ")
                                    }
                                    if(!obj.IC.isNullOrEmpty()){
                                        Text("IC${obj.IC}    ")
                                    }
                                }
                            }
                            Divider()
                            //Object Type
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                if(!ObjDefinitions.objectTypes[obj.Type].isNullOrEmpty()){
                                    Text(ObjDefinitions.objectTypes[obj.Type].toString())
                                }
                            }
                            Divider()

                            //RA origin
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                if(!obj.RA.isNullOrEmpty()){
                                    val raSplit = obj.RA.split(':')
                                    val raHour = "${raSplit[0].trimStart('0').replace("-0","-")}h${raSplit[1]}m${raSplit[2]}s"
                                    Text("RA (J2000):   $raHour")
                                }
                            }
                            Divider()

                            //DEC origin
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                if(!obj.Dec.isNullOrEmpty()){
                                    val decSplit = obj.Dec.split(':')
                                    val decCorrected =
                                        "${decSplit[0].trimStart('0').replace("-0","-")}:${decSplit[1]}:${decSplit[2]}"
                                    Text("Dec (J2000):  $decCorrected")
                                }

                            }
                            Divider()

                            viewModel.getObjPosNow()

                            val curPos = state.apparentPos?:Pair("","")

                            //RA apparent now
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                //TODO make this h:m:s (may need to change usecase)
                                val raApparent = curPos.first
                                Text("RA (of now):   $raApparent")
                            }
                            Divider()

                            //DEC apparent now
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                val decApparent = curPos.second
                                Text("Dec (of now):  $decApparent")
                            }
                            Divider()

                            viewModel.getObjRiseSet()

                            val riseSet = state.riseSet?:Pair(LocalDateTime.MIN, LocalDateTime.MIN)

                            //Rise
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                val rise = riseSet.first
                                if(rise==LocalDateTime.MIN){
                                    Text("Rises:   N/A")
                                }else{
                                    Text("Rises:   $rise")
                                }
                            }
                            Divider()

                            //Set
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                val set = riseSet.second
                                if(set==LocalDateTime.MIN){
                                    Text("Sets:   N/A")
                                }else{
                                    Text("Sets:   $set")
                                }

                            }
                            Divider()

                            //Time up
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                val upForTotalSeconds = Duration.between(riseSet.second,riseSet.first).toSeconds().absoluteValue
                                val upHours = (upForTotalSeconds / 3600).toInt()
                                val upMinutes = ((upForTotalSeconds % 3600)/60).toInt()
                                val upSeconds = (upForTotalSeconds % 60).toInt()

                                if(upForTotalSeconds.toInt() ==0){
                                    Text("Time up:   N/A (will not rise tonight)")
                                }else{
                                    if(upHours > 0){
                                        Text("Time up:   ${upHours}h ${upMinutes}m")
                                    }else{
                                        Text("Time up:   ${upMinutes}m ${upSeconds}s")
                                    }
                                }
                            }
                            Divider()

                            //Apparent magnitude
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                val mag = obj.getMagnitude()?:0.0
                                val rndMag = (mag * 100).roundToInt() / 100.0
                                Text("App. Mag: $rndMag")
                            }
                            Divider()

                            //Angular size
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                Text("Size :  "+obj.getSize().first+"\', "+obj.getSize().second+"\'")
                            }
                            Divider()

                            //Constellation
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ){
                                Text("Constellation: "+ ObjDefinitions.constellations[obj.Const])
                            }
                            Divider()
                        }
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
            MyBottomNavBar(navController = navController)
        }
    )
}

@Composable
fun ImageError(){
    Box(modifier = Modifier.size(imageScale.dp), contentAlignment = Alignment.Center){
        Text(text="Image Service Unavailable")
    }
}
