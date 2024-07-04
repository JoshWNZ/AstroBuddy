package com.pilot.astrobuddy.presentation.settings_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pilot.astrobuddy.domain.model.warning.WarningSeverity
import com.pilot.astrobuddy.domain.model.warning.WarningType
import com.pilot.astrobuddy.presentation.common.MyBottomNavBar

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    var tabVisible by rememberSaveable {
        mutableStateOf("")
    }

    var daysPosition by remember { mutableFloatStateOf(state.forecastDays.toFloat())}
    var unitPosition by remember {mutableStateOf(state.units=="C")}
    var timePosition by remember {mutableStateOf(state.timeFormat=="24h")}
    var dewLowPosition by remember {mutableIntStateOf(state.dewThres.first)}
    var dewMedPosition by remember {mutableIntStateOf(state.dewThres.second)}
    var dewHighPosition by remember {mutableIntStateOf(state.dewThres.third)}
    var windLowPosition by remember {mutableIntStateOf(state.windThres.first)}
    var windMedPosition by remember {mutableIntStateOf(state.windThres.second)}
    var windHighPosition by remember {mutableIntStateOf(state.windThres.third)}
    var rainLowPosition by remember {mutableIntStateOf(state.rainThres.first)}
    var rainMedPosition by remember {mutableIntStateOf(state.rainThres.second)}
    var rainHighPosition by remember {mutableIntStateOf(state.rainThres.third)}

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title={ Text("Preferences") },
                backgroundColor = Color.DarkGray,
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
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(padding)
            ) {
                if (false) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                } else{
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tabVisible = if (tabVisible == "forecast") {
                                        ""
                                    } else {
                                        "forecast"
                                    }
                                }
                                .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "Forecast Settings",
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .width(300.dp)
                                )
                                val arrow = if (tabVisible == "forecast") {
                                    Icons.Rounded.ArrowDropUp
                                } else {
                                    Icons.Rounded.ArrowDropDown
                                }
                                Icon(
                                    imageVector = arrow,
                                    contentDescription = "DropDown",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .scale(3f)
                                )
                            }

                            Divider()
                            AnimatedVisibility(
                                visible = tabVisible == "forecast",
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column(modifier = Modifier.background(color = Color.DarkGray.copy(alpha = 0.25f))) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Days", style = MaterialTheme.typography.h6)

                                        Slider(
                                            value = daysPosition,
                                            onValueChange = { daysPosition = it },
                                            onValueChangeFinished = { viewModel.updateDays(daysPosition.toInt()) },
                                            colors = SliderDefaults.colors(),
                                            steps = 14,
                                            valueRange = 1f..14f,
                                            modifier = Modifier.width(256.dp)
                                        )

                                        Box(modifier = Modifier.width(32.dp)) {
                                            Text(
                                                text = daysPosition.toInt().toString(),
                                                style = MaterialTheme.typography.h5
                                            )
                                        }

                                    }
                                    Divider()

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Units", style = MaterialTheme.typography.h6)
                                        val unit = state.units

                                        Column() {
                                            Text(text = "Imperial", style = MaterialTheme.typography.h5)
                                            Text(
                                                text = "(°F, ft, mph)",
                                                style = MaterialTheme.typography.body1
                                            )
                                        }
                                        Switch(
                                            checked = unitPosition,
                                            onCheckedChange = {
                                                unitPosition = it
                                                viewModel.updateUnits()
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = MaterialTheme.colors.secondary,
                                                checkedTrackColor = MaterialTheme.colors.secondaryVariant,
                                                uncheckedThumbColor = MaterialTheme.colors.primary,
                                                uncheckedTrackColor = MaterialTheme.colors.primaryVariant
                                            ),
                                            modifier = Modifier.scale(1.2f)
                                        )
                                        Column() {
                                            Text(text = "Metric", style = MaterialTheme.typography.h5)
                                            Text(
                                                text = "(°C, m, kph)",
                                                style = MaterialTheme.typography.body1
                                            )
                                        }
                                    }
                                    Divider()

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Time Format", style = MaterialTheme.typography.h6)
                                        val timeFormat = state.timeFormat

                                        Column() {
                                            Text(text = "12h", style = MaterialTheme.typography.h5)
                                            Text(
                                                text = "(6:00 AM/",
                                                style = MaterialTheme.typography.body1
                                            )
                                            Text(
                                                text = "6:00 PM)",
                                                style = MaterialTheme.typography.body1
                                            )
                                        }
                                        Switch(
                                            checked = timePosition,
                                            onCheckedChange = {
                                                timePosition = it
                                                viewModel.updateTimeFormat()
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = MaterialTheme.colors.secondary,
                                                checkedTrackColor = MaterialTheme.colors.secondaryVariant,
                                                uncheckedThumbColor = MaterialTheme.colors.primary,
                                                uncheckedTrackColor = MaterialTheme.colors.primaryVariant
                                            ),
                                            modifier = Modifier.scale(1.2f)
                                        )
                                        Column() {
                                            Text(text = "24h", style = MaterialTheme.typography.h5)
                                            Text(
                                                text = "(06:00/",
                                                style = MaterialTheme.typography.body1
                                            )
                                            Text(
                                                text = "18:00)",
                                                style = MaterialTheme.typography.body1
                                            )
                                        }
                                    }

                                    Divider()

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Warning Thresholds",
                                            style = MaterialTheme.typography.h4
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Wind\nSpeed",
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier.width(64.dp)
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 8.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth(0.8f)
                                            ) {
                                                Text("Level", style = MaterialTheme.typography.h6)
                                                Text("Threshold", style = MaterialTheme.typography.h6)
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Low",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = windLowPosition.toFloat(),
                                                    onValueChange = { windLowPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.WIND,
                                                            WarningSeverity.LOW,
                                                            windLowPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 51,
                                                    valueRange = 0f..50f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = windLowPosition.toString()
                                                )
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Med",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = windMedPosition.toFloat(),
                                                    onValueChange = { windMedPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.WIND,
                                                            WarningSeverity.MED,
                                                            windMedPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 51,
                                                    valueRange = 0f..50f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = windMedPosition.toString()
                                                )
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "High",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = windHighPosition.toFloat(),
                                                    onValueChange = { windHighPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.WIND,
                                                            WarningSeverity.HIGH,
                                                            windHighPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 51,
                                                    valueRange = 0f..50f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = windHighPosition.toString()
                                                )
                                            }
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Rain\nProb\n(%)",
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier.width(64.dp)
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 8.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth(0.8f)
                                            ) {
                                                Text("Level", style = MaterialTheme.typography.h6)
                                                Text("Threshold", style = MaterialTheme.typography.h6)
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Low",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = rainLowPosition.toFloat(),
                                                    onValueChange = { rainLowPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.RAIN,
                                                            WarningSeverity.LOW,
                                                            rainLowPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 101,
                                                    valueRange = 0f..100f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = rainLowPosition.toString()
                                                )
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Med",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = rainMedPosition.toFloat(),
                                                    onValueChange = { rainMedPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.RAIN,
                                                            WarningSeverity.MED,
                                                            rainMedPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 101,
                                                    valueRange = 0f..100f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = rainMedPosition.toString()
                                                )
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "High",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = rainHighPosition.toFloat(),
                                                    onValueChange = { rainHighPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.RAIN,
                                                            WarningSeverity.HIGH,
                                                            rainHighPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 101,
                                                    valueRange = 0f..100f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = rainHighPosition.toString()
                                                )
                                            }
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Dew\nDelta\n(ºC)",
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier.width(64.dp)
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 8.dp)
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth(0.8f)
                                            ) {
                                                Text("Level", style = MaterialTheme.typography.h6)
                                                Text("Threshold", style = MaterialTheme.typography.h6)
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Low",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = dewLowPosition.toFloat(),
                                                    onValueChange = { dewLowPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.DEW,
                                                            WarningSeverity.LOW,
                                                            dewLowPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 16,
                                                    valueRange = 0f..15f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = dewLowPosition.toString()
                                                )
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Med",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = dewMedPosition.toFloat(),
                                                    onValueChange = { dewMedPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.DEW,
                                                            WarningSeverity.MED,
                                                            dewMedPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 31,
                                                    valueRange = 0f..30f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = dewMedPosition.toString()
                                                )
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "High",
                                                    modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .width(48.dp),
                                                    textAlign = TextAlign.End
                                                )
                                                Slider(
                                                    value = dewHighPosition.toFloat(),
                                                    onValueChange = { dewHighPosition = it.toInt() },
                                                    onValueChangeFinished = {
                                                        viewModel.updateWarningThreshold(
                                                            WarningType.DEW,
                                                            WarningSeverity.HIGH,
                                                            dewHighPosition
                                                        )
                                                    },
                                                    colors = SliderDefaults.colors(
                                                        activeTickColor = Color.Transparent,
                                                        inactiveTickColor = Color.Transparent
                                                    ),
                                                    steps = 31,
                                                    valueRange = 0f..30f,
                                                    modifier = Modifier.width(224.dp)
                                                )
                                                Text(
                                                    text = dewHighPosition.toString()
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Divider()
                        }
                        item {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tabVisible = if (tabVisible == "object") {
                                        ""
                                    } else {
                                        "object"
                                    }
                                }
                                .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "Object Settings",
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .width(300.dp)
                                )
                                val arrow = if (tabVisible == "object") {
                                    Icons.Rounded.ArrowDropUp
                                } else {
                                    Icons.Rounded.ArrowDropDown
                                }
                                Icon(
                                    imageVector = arrow,
                                    contentDescription = "DropDown",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .scale(3f)
                                )
                            }
                            Divider()
                            AnimatedVisibility(
                                visible = tabVisible == "object",
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column(modifier = Modifier.background(color = Color.DarkGray.copy(alpha = 0.25f))) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("WIP")
                                        Text("Clear image cache, etc")
                                    }
                                }
                            }
                            Divider()
                        }
                        item {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tabVisible = if (tabVisible == "equipment") {
                                        ""
                                    } else {
                                        "equipment"
                                    }
                                }
                                .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "Equipment Settings",
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .width(300.dp)
                                )
                                val arrow = if (tabVisible == "equipment") {
                                    Icons.Rounded.ArrowDropUp
                                } else {
                                    Icons.Rounded.ArrowDropDown
                                }
                                Icon(
                                    imageVector = arrow,
                                    contentDescription = "DropDown",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .scale(3f)
                                )
                            }
                            Divider()
                            AnimatedVisibility(
                                visible = tabVisible == "equipment",
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column(modifier = Modifier.background(color = Color.DarkGray.copy(alpha = 0.25f))) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("WIP")
                                    }
                                }
                            }
                            Divider()
                        }
                        item {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tabVisible = if (tabVisible == "general") {
                                        ""
                                    } else {
                                        "general"
                                    }
                                }
                                .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "General Settings",
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .width(300.dp)
                                )
                                val arrow = if (tabVisible == "general") {
                                    Icons.Rounded.ArrowDropUp
                                } else {
                                    Icons.Rounded.ArrowDropDown
                                }
                                Icon(
                                    imageVector = arrow,
                                    contentDescription = "DropDown",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .scale(3f)
                                )
                            }
                            Divider()
                            AnimatedVisibility(
                                visible = tabVisible == "general",
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column(modifier = Modifier.background(color = Color.DarkGray.copy(alpha = 0.25f))) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("WIP")
                                        Text("Colour theme, notifs, etc")
                                    }
                                }
                            }
                            Divider()
                        }
                    }
                }
            }
        },
        bottomBar = {
            MyBottomNavBar(navController = navController)
        }
    )
}