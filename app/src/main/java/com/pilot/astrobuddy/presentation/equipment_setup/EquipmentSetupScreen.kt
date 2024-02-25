package com.pilot.astrobuddy.presentation.equipment_setup

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pilot.astrobuddy.presentation.Screen
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EquipmentSetupScreen(
    navController: NavController,
    viewModel: EquipmentSetupViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    //initialise states for form inputs
    var setupName by rememberSaveable { mutableStateOf("") }
    var scopeName by rememberSaveable { mutableStateOf("") }
    var focalLength by rememberSaveable { mutableStateOf("") }
    var aperture by rememberSaveable { mutableStateOf("") }
    var modifier by rememberSaveable { mutableStateOf("") }
    var focalRatio by rememberSaveable {mutableStateOf("") }
    var cameraName by rememberSaveable { mutableStateOf("") }
    var vertPixels by rememberSaveable { mutableStateOf("") }
    var horiPixels by rememberSaveable { mutableStateOf("") }
    var sensorWidth by rememberSaveable { mutableStateOf("") }
    var sensorHeight by rememberSaveable { mutableStateOf("") }
    var pixelScale by rememberSaveable { mutableStateOf("") }

    //variables for dropdown menu functionality
    val options = listOf("0.4","0.6","0.8","1.0","2.0","4.0","6.0")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[3]) }
    modifier = "1.0"

    focalRatio = "0.0"

    if(state.astroEquipment != null){
        val equip = state.astroEquipment

        setupName   = equip.setupName
        scopeName   = equip.scopeName
        focalLength = equip.focalLength.toString()
        aperture    = equip.aperture.toString()
        modifier    = equip.modifier.toString()
        cameraName  = equip.cameraName
        vertPixels  = equip.verticalPixels.toString()
        horiPixels  = equip.horizontalPixels.toString()
        sensorWidth = equip.sensorWidth.toString()
        sensorHeight = equip.sensorHeight.toString()
        pixelScale  = equip.pixelScale.toString()

        selectedOptionText = options[options.indexOf(equip.modifier.toString())]

        focalRatio = if(focalLength.toDouble() <= 0 || aperture.toDouble() <= 0){"0"}
        else{ ((focalLength.toDouble() / aperture.toDouble()) * modifier.toDouble()).toString() }
    }

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title={ Text("Edit your Setup") },
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
                },
                actions = {}
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
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ){
                        Text("Setup Name:")
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = setupName,
                            onValueChange = {setupName = it},
                            placeholder = {Text("e.g. My Setup")}
                            )

                        Divider(modifier = Modifier.padding(top=16.dp))

                        Text("Scope Name:")
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = scopeName,
                            onValueChange = {scopeName = it},
                            placeholder = {Text("e.g. Esprit 100ed")}
                        )

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column {
                                Text("Focal Length (mm)")
                                TextField(
                                    value = focalLength,
                                    onValueChange = {
                                        if(it.isEmpty()){
                                            focalLength = ""
                                        }else{
                                            try {
                                                if(it.toDouble() >= 0.0){
                                                    focalLength = it

                                                    focalRatio = if(focalLength.toDouble() > 0.0 && aperture.toDouble() > 0.0){
                                                        ((focalLength.toDouble() / aperture.toDouble()) * modifier.toDouble()).toString()
                                                    }else{
                                                        "0.0"
                                                    }
                                                }
                                            }catch(_: Exception){ }
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    placeholder = {Text("e.g. 600")},
                                    modifier = Modifier.width(128.dp)
                                )
                            }
                            Column {
                                Text("Aperture (mm)")
                                TextField(
                                    value = aperture,
                                    onValueChange = {
                                        if(it.isEmpty()){
                                            aperture = ""
                                        }else{
                                            try {
                                                if(it.toDouble() >= 0.0){
                                                    aperture = it

                                                    focalRatio = if(focalLength.toDouble() > 0.0 && aperture.toDouble() > 0.0){
                                                        ((focalLength.toDouble() / aperture.toDouble()) * modifier.toDouble()).toString()
                                                    }else{
                                                        "0.0"
                                                    }
                                                }
                                            }catch(_: Exception){ }
                                        }

                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    placeholder = {Text("e.g. 100")},
                                    modifier = Modifier.width(128.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            Column {
                                Text("Reducer/Barlow:")
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = {expanded = !expanded},
                                    modifier = Modifier.width(128.dp)
                                ) {
                                    TextField(
                                        readOnly = true,
                                        value = selectedOptionText,
                                        onValueChange = {},
                                        trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded
                                        )},
                                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                        modifier = Modifier.focusProperties {
                                            canFocus = false
                                            enter = {FocusRequester.Cancel}
                                        }
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        options.forEach{opt ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    selectedOptionText = opt
                                                    modifier = opt

                                                    focalRatio = if(focalLength.toDouble() > 0.0 && aperture.toDouble() > 0.0){
                                                        ((focalLength.toDouble() / aperture.toDouble()) * modifier.toDouble()).toString()
                                                    }else{
                                                        "0.0"
                                                    }

                                                    expanded = false
                                                }
                                            ) {
                                                Text(opt)
                                            }
                                        }
                                    }
                                }
                            }

                            Column {
                                Text("Focal Ratio:")
                                TextField(
                                    value = String.format("%.2f",focalRatio.toDouble()),
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .focusProperties {
                                            canFocus = false
                                            enter = { FocusRequester.Cancel }
                                        }
                                        .width(128.dp),
                                    leadingIcon = {
                                        Text(text="F/",style=MaterialTheme.typography.h4)
                                    }
                                )
                            }
                        }

                        Divider(modifier = Modifier.padding(top=16.dp))

                        Text("Camera Name:")
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = cameraName,
                            onValueChange = {cameraName = it},
                            placeholder = {Text("e.g. ZWO 533MC")}
                        )

                        Text("Resolution:")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            Column {
                                Text("Vertical:")
                                TextField(
                                    value = vertPixels,
                                    onValueChange = {
                                        if(it.isEmpty()){
                                            vertPixels = ""
                                        }else{
                                            try {
                                                if(it.toInt() >= 0 && it.toInt().toDouble() == it.toDouble()){
                                                    vertPixels = it.trim()
                                                }
                                            }catch(_: Exception){ }
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    placeholder = {Text("e.g. 4000")},
                                    modifier = Modifier.width(128.dp)
                                )
                            }
                            Column {
                                Text("Horizontal:")
                                TextField(
                                    value = horiPixels,
                                    onValueChange = {
                                        if(it.isEmpty()){
                                            horiPixels = ""
                                        }else{
                                            try {
                                                if(it.toInt() >= 0 && it.toInt().toDouble() == it.toDouble()){
                                                    horiPixels = it.trim()
                                                }
                                            }catch(_: Exception){ }
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    placeholder = {Text("e.g. 6000")},
                                    modifier = Modifier.width(128.dp)
                                )
                            }
                        }

                        Text("Sensor Size:")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ){
                            Column {
                                Text("Vertical (mm):")
                                TextField(
                                    value = sensorHeight,
                                    onValueChange = {
                                        if(it.isEmpty()){
                                            sensorHeight = ""
                                        }else{
                                            try {
                                                if(it.toDouble() >= 0.0){
                                                    sensorHeight = it.trim()
                                                }
                                            }catch(_: Exception){ }
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    placeholder = {Text("e.g. 14.9")},
                                    modifier = Modifier.width(128.dp)
                                )
                            }
                            Column {
                                Text("Horizontal (mm):")
                                TextField(
                                    value = sensorWidth,
                                    onValueChange = {
                                        if(it.isEmpty()){
                                            sensorWidth = ""
                                        }else{
                                            try {
                                                if(it.toDouble() >= 0.0){
                                                    sensorWidth = it.trim()
                                                }
                                            }catch(_: Exception){ }
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Decimal
                                    ),
                                    placeholder = {Text("e.g. 22.3")},
                                    modifier = Modifier.width(128.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))

                        IconButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .align(Alignment.CenterHorizontally)
                                .weight(0.15f)
                                .clip(CircleShape)
                                .background(Color.Blue),
                            content = {
                                Icon(imageVector = Icons.Rounded.Save,
                                    contentDescription = "Save")
                            },
                            onClick = {
                                try{
                                    val id = if(state.astroEquipment != null)
                                            {state.astroEquipment.id} else{0}

                                    viewModel.saveEquipment(
                                        id = id,
                                        setupName = setupName,
                                        scopeName = scopeName,
                                        focalLength = focalLength.toDouble(),
                                        aperture = focalLength.toDouble(),
                                        modifier = modifier.toDouble(),
                                        cameraName = cameraName,
                                        vertPixels = vertPixels.toInt(),
                                        horiPixels = horiPixels.toInt(),
                                        sensorWidth = sensorWidth.toDouble(),
                                        sensorHeight = sensorHeight.toDouble()
                                        )

                                    navController.navigate(Screen.EquipmentScreen.route)
                                }catch(e: Exception){
                                    viewModel.throwError()
                                }
                            }
                        )
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