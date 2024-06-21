package com.pilot.astrobuddy.presentation.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.presentation.Screen
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar
import com.pilot.astrobuddy.presentation.home_screen.components.AstroRowItem

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(0){
        viewModel.fetchInformation()
    }

    //variables for dropdown menu functionality
    val equipList = state.savedEquip
    val locList = state.savedLocs
    val objList = state.savedObjects

    var equipEmpty = equipList.isEmpty()
    var locEmpty = locList.isEmpty()
    var objEmpty = objList.isEmpty()

    var equipExpanded by remember { mutableStateOf(false) }
    var locExpanded by remember { mutableStateOf(false) }

    var selectedEquipOption by remember {
        if(equipEmpty){
            mutableStateOf(AstroEquipment.getDummyAstroEquipment())
        }else{
            mutableStateOf(equipList[0])
        }
    }
    var selectedLocOption by remember {
        if(locEmpty){
            mutableStateOf(OMLocation.getDummyOMLocation())
        }else {
            mutableStateOf(locList[0])
        }
    }

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title={ Text("AstroBuddy Home") },
                backgroundColor = Color.DarkGray,
                actions = {

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
                    //CONTENT

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Column(){
                           Text("Active Equipment")
                           //Equipment selection
                           ExposedDropdownMenuBox(
                               expanded = equipExpanded,
                               onExpandedChange = { equipExpanded = !equipExpanded },
                               modifier = Modifier.width(192.dp)
                           ) {
                               TextField(
                                   readOnly = true,
                                   value = if(!equipEmpty){selectedEquipOption.setupName}
                                   else{"No Setups Found"},
                                   onValueChange = {},
                                   trailingIcon = {
                                       ExposedDropdownMenuDefaults.TrailingIcon(
                                           expanded = equipExpanded
                                       )},
                                   colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                   modifier = Modifier.focusProperties {
                                       canFocus = false
                                       enter = { FocusRequester.Cancel}
                                   }
                               )
                               ExposedDropdownMenu(
                                   expanded = equipExpanded,
                                   onDismissRequest = { equipExpanded = false }
                               ) {
                                   if(!equipEmpty) {
                                       equipList.forEach { opt ->
                                           DropdownMenuItem(
                                               onClick = {
                                                   selectedEquipOption = opt
                                                   //make the actual change (settingstore?)
                                                   //modifier = opt

                                                   equipExpanded = false
                                               }
                                           ) {
                                               Text(opt.setupName)
                                           }
                                       }
                                   }
                               }
                           }
                        }
                        Column(){
                            Text("Active Location")
                            //Location selection
                            ExposedDropdownMenuBox(
                                expanded = locExpanded,
                                onExpandedChange = { locExpanded = !locExpanded},
                                modifier = Modifier.width(192.dp)
                            ) {
                                TextField(
                                    readOnly = true,
                                    //TODO check empty and show coords instead
                                    value = if(!locEmpty){selectedLocOption.name}
                                    else{"No Saved Locations"},
                                    onValueChange = {},
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = locExpanded
                                        )},
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                    modifier = Modifier.focusProperties {
                                        canFocus = false
                                        enter = { FocusRequester.Cancel}
                                    }
                                )
                                ExposedDropdownMenu(
                                    expanded = locExpanded,
                                    onDismissRequest = { locExpanded = false }
                                ) {
                                    locList.forEach{opt ->
                                        DropdownMenuItem(
                                            onClick = {
                                                selectedLocOption = opt
                                                //make the actual change (settingstore?)
                                                //modifier = opt

                                                locExpanded = false
                                            }
                                        ) {
                                            Text(opt.name)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Divider()

                    Text("Object Wishlist:")

                    //display object list
                    LazyRow(modifier = Modifier.fillMaxWidth()){
                        objList.forEach{obj ->
                            item{
                                AstroRowItem(
                                    obj = obj,
                                    onItemClick = {
                                        navController.navigate(Screen.ObjectDisplayScreen.route+"/${obj.Name}")
                                    },
                                )
                            }
                        }
                    }

                    Divider()


                }
            }
        },
        floatingActionButton = {},
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    )
}
