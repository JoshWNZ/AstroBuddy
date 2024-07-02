package com.pilot.astrobuddy.presentation.forecast_display.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pilot.astrobuddy.domain.model.astro_forecast.Astro
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ForecastScrollerItem(
    fd: OMForecast,
    astro: List<Astro>,
    timeFormat: String
){
    val scope = rememberCoroutineScope()

    //store the current pixel width of the screen
    val width = LocalConfiguration.current.screenWidthDp
    val scrollSyncRatio = (width/722.0).toFloat()

    //fetch the current datetime from the system, format it to an hour value
    val curTime = LocalDateTime.now()
    val curHour = curTime.format(DateTimeFormatter.ofPattern("HH"))

    //remember the states of the day and hour rows
    val dayRowState = rememberLazyListState()
    val hoursRowState = rememberLazyListState()
    val skyScrollerState = rememberLazyListState()

    //keep track of whether any scrolling has been done
    var firstScroll = true

    //update the lazyrows at the correct ratios
    //correct the day-info row on the first scroll only
    val scrollState = rememberScrollableState{delta->
        if(firstScroll) {
            scope.launch {
                val hoursElapsed = curHour.toInt() - 1
                dayRowState.scrollBy((((hoursElapsed*30f)-(width*0.15f))/scrollSyncRatio))
                firstScroll = firstScroll.not()
            }
        }
        scope.launch{
            dayRowState.scrollBy(-delta*scrollSyncRatio)
            hoursRowState.scrollBy(-delta)
            skyScrollerState.scrollBy(-delta)
        }
        delta
    }

    val skyScrollBy = with(LocalDensity.current){((curHour.toInt()*30)+1).dp.toPx()}

    //automatically scroll to the current hour
    LaunchedEffect(Unit){
        scope.launch{
            firstScroll = true
            hoursRowState.animateScrollToItem(curHour.toInt())

            skyScrollerState.animateScrollBy(skyScrollBy)
        }
    }

    Divider(
        color = Color.Gray
    )
    Column(
        modifier = Modifier.scrollable(
            scrollState,
            Orientation.Horizontal,
            flingBehavior = ScrollableDefaults.flingBehavior()
        )
    ){
        //day row cannot be scrolled by the user, can only be updated
        // by the scrollstate of the hour row
        LazyRow(
            state = dayRowState,
            userScrollEnabled = false
        ){
            //give each day an item in the row
            items(fd.hourly.time.size/24){d->
                //format the first datetime from each day
                val date = LocalDate.parse(fd.hourly.time[d*24], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                val day = date.format(DateTimeFormatter.ofPattern("EEE"))
                val dayMon = date.format(DateTimeFormatter.ofPattern("dd MMM"))
                //display the date as dd-mm and the day as a word
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .height(44.dp)
                        .width((width * 0.15).dp),
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
                            style = MaterialTheme.typography.body2.copy(color= MaterialTheme.colors.onSecondary)
                        )
                    }
                }
                //get the current astronomical forecast or default to an empty one (to be fixed)
                val curAstro = astro[d]

                val timeFormatter = if(timeFormat == "12h"){
                    DateTimeFormatter.ofPattern("hh:mm a")}
                else{
                    DateTimeFormatter.ofPattern("HH:mm")}

                // Sunrise/set
                Row(
                    modifier = Modifier
                        .background(Color.Yellow)
                        .height(44.dp)
                        .width((width * 0.35).dp)
                ){
                    Icon(
                        imageVector = Icons.Rounded.WbSunny,
                        contentDescription = "Sun",
                        tint = Color.Black,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 6.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            text= "rise: ${timeFormatter.format(curAstro.sunRiseSet.first)}",
                            style = MaterialTheme.typography.body2.copy(color= MaterialTheme.colors.onSecondary)
                        )
                        Text(
                            text= "set:  ${timeFormatter.format(curAstro.sunRiseSet.second)}",
                            style = MaterialTheme.typography.body2.copy(color= MaterialTheme.colors.onSecondary)
                        )
                    }
                }

                //Moon phase, rise/set
                Column(
                    modifier = Modifier
                        .background(Color.Gray)
                        .height(44.dp)
                        .width((width * 0.5).dp)
                        .offset(x = 6.dp)
                ){
                    Row{
                        Icon(
                            imageVector = Icons.Rounded.DarkMode,
                            contentDescription = "Moon"
                        )
                        Text(
                            text= "${curAstro.moon_phase}, ${curAstro.moon_illumination}%",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .align(CenterVertically)
                        )
                    }
                    Text(
                        text= "rise: ${timeFormatter.format(curAstro.moonRiseSet.first)}" +
                                ", set: ${timeFormatter.format(curAstro.moonRiseSet.second)}",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
        Divider(
            color = Color.Gray
        )
        Row(/*modifier = Modifier.height(380.dp)*/){
            Column{
                //display a column of labels for each element of the forecast
                val labels = listOf(
                    "Time","Total Cloud","High Cloud","Mid Cloud","Low Cloud","Visibility","Rain prob.","Wind Spd.","Wind Dir.","Temp","Feels Like","Humidity","Dew Point"
                )
                Column(
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .width((width*0.15).dp)
                ){
                    labels.forEach {
                        Box(
                            modifier = Modifier
                                .height(28.dp)
                                .align(CenterHorizontally),
                            contentAlignment = Center
                        ){
                            Text(
                                text=it,
                                style = MaterialTheme.typography.body2,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Right,
                                modifier = Modifier
                                    .padding(end = 2.dp)
                                    .fillMaxWidth()
                            )
                        }
                        Divider(
                            color = Color.Gray
                        )
                    }
                    Box(modifier = Modifier.fillMaxHeight(fraction =0.995f)){}
                    Divider(color = Color.Gray)
                }
            }
            //Vertical divider
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
            )
            //scrollable lazyrow containing all forecast values for each hour
            Column(){
                LazyRow(
                    state = hoursRowState,
                    userScrollEnabled = false
                ){
                    items(fd.hourly.time.size){i->
                        val date = LocalDate.parse(fd.hourly.time[i], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        val dayMon = date.format(DateTimeFormatter.ofPattern("dd - MMM"))

                        val curDay = (dayMon == curTime.format(DateTimeFormatter.ofPattern("dd - MMM")))

                        val isNewDay = (i+1)%24==0 && i+1!=fd.hourly.time.size

                        //pass the current hour into hours from the current day
                        if(curDay){
                            ForecastHourItem(forecastHour = fd.hourly, i=i, curHour = curHour)
                        }else{
                            ForecastHourItem(forecastHour = fd.hourly, i=i, curHour=null)
                        }
                        Divider(
                            color = if(isNewDay){Color.Blue}else{MaterialTheme.colors.onSurface.copy(alpha=0.01f)},
                            modifier = Modifier
                                .height(380.dp)
                                .width(2.dp)
                        )

                    }
                }
                Divider(
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
                ForecastSkyScroller(fd = fd, astros = astro, listState = skyScrollerState, scrollState= scrollState)
                Divider(color = Color.Gray)
            }
        }

    }
}

