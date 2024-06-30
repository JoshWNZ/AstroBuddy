package com.pilot.astrobuddy.presentation.attribution_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar


@Composable
fun AttributionScreen(
    navController: NavController
) {
    //TODO
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
                    Text("Astronomical database derived from Mattia Verga's OpenNGC project."+
                            "\nhttps://github.com/mattiaverga/OpenNGC"
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text("Weather data provided via the OpenMeteo API." +
                                "\nhttps://open-meteo.com/"
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text("Astronomical calculation library by Cosinekitty." +
                        "\nhttps://github.com/cosinekitty/astronomy"
                    )
                }
            }
        },
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    )
}