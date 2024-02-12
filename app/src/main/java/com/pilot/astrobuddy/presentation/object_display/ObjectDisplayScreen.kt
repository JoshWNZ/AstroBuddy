package com.pilot.astrobuddy.presentation.object_display

import android.util.Log
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
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistRemove
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.pilot.astrobuddy.domain.model.astro_objects.ObjDefinitions
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar
import kotlinx.coroutines.launch

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
                                        else{Icons.Rounded.PlaylistAdd},
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
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(MaterialTheme.colors.background)
            ) {
                //show the object info when it's available
                state.astroObject?.let { obj ->
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

                            ObjectImageFromHiPS(fov = fov, obj = obj.Name)
                            if(obj.CommonNames!=null){
                                Text(obj.CommonNames)
                            }
                            Text(obj.Name)
                            Text("RA:"+obj.RA)
                            Text("Dec:"+obj.Dec)
                            Text("Mag:"+obj.getMagnitude())
                            Text("Size:"+obj.getSize())
                            Text("Type: "+ ObjDefinitions.objectTypes[obj.Type])
                            Text("Constellation: "+ ObjDefinitions.constellations[obj.Const])
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
fun ObjectImageFromHiPS(
    fov: String,
    obj: String,
){
    var url = "https://alasky.cds.unistra.fr/hips-image-services/hips2fits?" +
            "hips=CDS%2FP%2FDSS2%2Fcolor" +
            "&width=720&height=720" +
            "&fov=$fov" +
            "&projection=TAN" +
            "&coordsys=icrs" +
            "&rotation_angle=0.0" +
            "&object=$obj" +
            "&format=jpg"

    Log.i("IMAGE", "$obj $fov")

    Box(modifier = Modifier.size(imageScale.dp), contentAlignment = Alignment.Center){
        SubcomposeAsyncImage(
            model = url,
            contentDescription = "image",
            modifier = Modifier.size(imageScale.dp),
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.High,
            loading = {
                Box(
                    modifier = Modifier.size((imageScale/1.5).dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            onError = {
                Log.i("IMAGE_ERROR", "Image borked")
            }
        )
    }
}

@Composable
fun CoordImageFromHiPS(
    fov: String,
    ra: String,
    dec: String,
){
    val url = "https://alasky.cds.unistra.fr/hips-image-services/hips2fits?" +
            "hips=CDS%2FP%2FDSS2%2Fcolor" +
            "&width=720&height=720" +
            "&fov=$fov" +
            "&projection=TAN" +
            "&coordsys=icrs" +
            "&rotation_angle=0.0" +
            "&ra=$ra" +
            "&dec=$dec" +
            "&format=jpg"

    Box(modifier = Modifier.size(imageScale.dp), contentAlignment = Alignment.Center){
        SubcomposeAsyncImage(
            model = url,
            contentDescription = "image",
            modifier = Modifier.size(imageScale.dp),
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.High,
            loading = {
                Box(
                    modifier = Modifier.size((imageScale/1.5).dp),
                    contentAlignment = Alignment.Center)
                {
                    CircularProgressIndicator()
                }
            }
        )
    }
}

@Composable
fun ImageError(){
    Box(modifier = Modifier.size(imageScale.dp), contentAlignment = Alignment.Center){
        Text(text="Image Service Unavailable")
    }
}
