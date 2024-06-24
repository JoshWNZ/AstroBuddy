package com.pilot.astrobuddy.presentation.object_search

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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pilot.astrobuddy.presentation.Screen
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar
import com.pilot.astrobuddy.presentation.object_search.components.ObjDropDownMenu
import com.pilot.astrobuddy.presentation.object_search.components.ObjSearchItem

@Composable
fun ObjectSearchScreen(
    navController: NavController,
    viewModel: ObjectSearchViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title={ Text("Objects") },
                backgroundColor = Color.DarkGray,
                actions = {
                    ObjDropDownMenu(navController)
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
                                Text(text = "Enter object name")
                            }
                        )

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ){
                        //show fetched objects from the user query
                        items(state.objects){obj->
                            Log.i("OBJECT","object NGC:"+obj.NGC+" IC:"+obj.IC+" M:"+obj.M)
                            Log.i("OBJECT", obj.toString())
                            ObjSearchItem(
                                astroObject = obj,
                                onItemClick = {
                                    navController.navigate(Screen.ObjectDisplayScreen.route+"/${obj.Name}")
                                },
                                recommended = false
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
        floatingActionButton = {
        },
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    )
}