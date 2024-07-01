package com.pilot.astrobuddy.presentation.forecast_display.components

import androidx.compose.foundation.background
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.astro_forecast.Astro
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ForecastDayItem(
    fd: OMForecast,
    d: Int,
    astro: Astro
){
    val date = LocalDate.parse(fd.hourly.time[d*24], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val day = date.format(DateTimeFormatter.ofPattern("EEE"))
    val dayMon = date.format(DateTimeFormatter.ofPattern("dd - MMM"))
    Row(
        modifier = Modifier.fillMaxWidth()
    ){

        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .height(44.dp)
                .width(64.dp)
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
        Row(
            modifier = Modifier
                .background(Color.Yellow)
                .height(44.dp)
                .weight(0.75f)
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
                    text= "rise: ${astro.sunrise}",
                    style = MaterialTheme.typography.body1.copy(color= MaterialTheme.colors.onSecondary)
                )
                Text(
                    text= "set: ${astro.sunset}",
                    style = MaterialTheme.typography.body1.copy(color= MaterialTheme.colors.onSecondary)
                )
            }
        }

        Row(
            modifier = Modifier
                .background(Color.Gray)
                .height(44.dp)
                .weight(1.25f)
        ){
            Icon(
                imageVector = Icons.Rounded.DarkMode,
                contentDescription = "Moon"
            )
            Column {
                Text(
                    text= "${astro.moon_phase}, ${astro.moon_illumination}%",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text= "rise: ${astro.moonrise}, set: ${astro.moonset}",
                    style = MaterialTheme.typography.body2
                )
            }
        }

    }
    Row {
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
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        val curTime = LocalDateTime.now()
        val curHour = curTime.format(DateTimeFormatter.ofPattern("HH"))
        val curDay = (dayMon == curTime.format(DateTimeFormatter.ofPattern("dd - MMM")))

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit){
            coroutineScope.launch{
                listState.animateScrollToItem(curHour.toInt())
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxSize(),
            state = if(curDay){listState}else{LazyListState()}
        ){
            item{
                //for(i in 0 until fd.hourly.time.size){
                for(i in 0 until 24){
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


    }
}

