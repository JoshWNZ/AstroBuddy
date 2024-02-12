package com.pilot.astrobuddy.presentation.equipment_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.pilot.astrobuddy.presentation.equipment_screen.components.EquipmentCreateItem
import com.pilot.astrobuddy.presentation.equipment_screen.components.EquipmentListItem

@Composable
fun EquipmentScreen(
    navController: NavController,
    viewModel: EquipmentScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title={ Text("Equipment") },
                backgroundColor = Color.DarkGray,
                actions = {
                    IconButton(
                        onClick = {viewModel.toggleEdit()},
                        content = {
                            Text(
                                text="Edit",
                                color = if(state.editing){Color.Red}
                                        else{Color.LightGray}
                            )
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
                Column (
                    modifier = Modifier.fillMaxSize()
                ){
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ){
                        //show fetched equipment
                        if(state.objects.isNotEmpty()) {
                            items(state.objects) { obj ->
                                EquipmentListItem(
                                    equipment = obj,
                                    onItemClick = {
                                        navController.navigate(Screen.EquipmentSetupScreen.route + "/${obj.id}")
                                    },
                                    editing = state.editing,
                                    delAction = {
                                        viewModel.deleteEquip(obj.id)
                                    }
                                )
                            }
                        }
                        item{
                            EquipmentCreateItem(
                                onItemClick = {navController.navigate(Screen.EquipmentSetupScreen.route+"/-1")}
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
        floatingActionButton = {},
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    )
}
