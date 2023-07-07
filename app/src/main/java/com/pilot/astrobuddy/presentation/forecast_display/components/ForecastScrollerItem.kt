package com.pilot.astrobuddy.presentation.forecast_display.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.weatherapi.Astro
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable

fun ForecastScrollerItem(
    fd: OMForecast,
    astro: List<Astro>
){

    val dayRowState = rememberLazyListState()
    val hoursRowState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollableState{delta->
        scope.launch{
            dayRowState.scrollBy(-delta/1.465f)
            hoursRowState.scrollBy(-delta)
        }
        delta
    }

    val width = LocalConfiguration.current.screenWidthDp

    val curTime = LocalDateTime.now()
    val curHour = curTime.format(DateTimeFormatter.ofPattern("HH"))

    LaunchedEffect(Unit){
        scope.launch{
            //Log.i("SCROLLED",(curHour.toFloat()*(25.dp.value)).toString())
            scrollState.animateScrollBy(curHour.toFloat()*(25.dp.value))
            hoursRowState.animateScrollToItem(curHour.toInt())
        }
    }


    Column(
        modifier = Modifier.scrollable(
            scrollState,
            Orientation.Horizontal,
            flingBehavior = ScrollableDefaults.flingBehavior()
        )
    ){
        LazyRow(
            state = dayRowState,
            userScrollEnabled = false
        ){
            items(fd.hourly.time.size/24){d->
                    val date = LocalDate.parse(fd.hourly.time[d*24], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    val day = date.format(DateTimeFormatter.ofPattern("EEE"))
                    val dayMon = date.format(DateTimeFormatter.ofPattern("dd - MMM"))
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray)
                            .height(44.dp)
                            .width((width*0.15).dp),
                        contentAlignment = Center
                    ){
                        Column(modifier = Modifier.padding(start = 2.dp)){
                            Text(
                                text=day,
                                modifier = Modifier.align(CenterHorizontally),
                                style = MaterialTheme.typography.body1.copy(color= MaterialTheme.colors.onSecondary))
                            Text(
                                text=dayMon,
                                modifier = Modifier.align(CenterHorizontally),
                                style = MaterialTheme.typography.body1.copy(color= MaterialTheme.colors.onSecondary)
                            )
                        }
                    }
                    val curAstro = astro.elementAtOrElse(d){Astro("","","","","","")}
                    Row(
                        modifier = Modifier
                            .background(Color.Yellow)
                            .height(44.dp)
                            .width((width*0.35).dp)
                    ){
                        Column(modifier = Modifier.align(CenterVertically)){
                            Icon(
                                imageVector = Icons.Rounded.WbSunny,
                                contentDescription = "Sun",
                                tint = Color.Black
                            )
                        }
                        Column {
                            Text(
                                text= "rise: ${curAstro.sunrise}",
                                style = MaterialTheme.typography.body1.copy(color= MaterialTheme.colors.onSecondary)
                            )
                            Text(
                                text= "set: ${curAstro.sunset}",
                                style = MaterialTheme.typography.body1.copy(color= MaterialTheme.colors.onSecondary)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .background(Color.Gray)
                            .height(44.dp)
                            .width((width*0.50).dp)
                    ){
                        Icon(
                            imageVector = Icons.Rounded.DarkMode,
                            contentDescription = "Moon"
                        )
                        Column {
                            Text(
                                text= "${curAstro.moon_phase}, ${curAstro.moon_illumination}%",
                                style = MaterialTheme.typography.body1
                            )
                            Text(
                                text= "rise: ${curAstro.moonrise}, set: ${curAstro.moonset}",
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
        }
        Row(modifier = Modifier.height(300.dp)){
            Column{
                val labels = listOf(
                    "time","cloudTot","cloudHi","cloudMed","cloudLow","rainprob","windspd","winddir","temp","feels","humidity","dewpoint"
                )
                Column(
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .width(63.dp)
                ){
                    labels.forEach {
                        Box(
                            modifier = Modifier
                                .height(24.dp)
                                .align(CenterHorizontally),
                            contentAlignment = Center
                        ){
                            Text(
                                text=it,
                                style = MaterialTheme.typography.body2,
                                textAlign = TextAlign.Center
                            )
                        }
                        Divider()
                    }

                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            LazyRow(
                state = hoursRowState,
                userScrollEnabled = false
            ){
                items(fd.hourly.time.size){i->
                    val date = LocalDate.parse(fd.hourly.time[i], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    val dayMon = date.format(DateTimeFormatter.ofPattern("dd - MMM"))

                    val curDay = (dayMon == curTime.format(DateTimeFormatter.ofPattern("dd - MMM")))

                    if(curDay){
                        ForecastHourItem(forecastHour = fd.hourly, i=i, curHour = curHour)
                    }else{
                        ForecastHourItem(forecastHour = fd.hourly, i=i, curHour=null)
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )

                }
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

