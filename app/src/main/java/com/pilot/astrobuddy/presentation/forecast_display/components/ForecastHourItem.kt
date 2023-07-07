package com.pilot.astrobuddy.presentation.forecast_display.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.HourlyDto
import com.pilot.astrobuddy.domain.model.weatherapi.ForecastHour
import kotlin.math.roundToInt


@Composable
fun ForecastHourItem(
    forecastHour: HourlyDto,
    i: Int,
    curHour: String?
){
    val hour = "${forecastHour.time[i][11]}${forecastHour.time[i][12]}"
    Column {
        val curCol= if(curHour!=null && curHour==hour){Color.Magenta}else{Color.DarkGray}
        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=hour,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.cloudcover[i].toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.cloudcover_high[i].toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.cloudcover_mid[i].toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.cloudcover_low[i].toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.precipitation_probability[i].toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.windspeed_10m[i].roundToInt().toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            val dir = degToDir(forecastHour.winddirection_10m[i])
            Text(
                text=dir,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.temperature_2m[i].roundToInt().toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.apparent_temperature[i].roundToInt().toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.relativehumidity_2m[i].toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()

        Box(modifier = Modifier.size(24.dp).background(curCol)){
            Text(
                text=forecastHour.dewpoint_2m[i].roundToInt().toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Divider()
    }
}

private fun degToDir(angle: Int): String{
    //val directions2 = listOf("↑N", "↗NE", "→E", "↘SE", "↓S", "↙SW", "←W", "↖NW")
    val directions = listOf("↑", "↗", "→", "↘", "↓", "↙", "←", "↖")
    val index = (angle / 45.0).roundToInt() % 8
    return directions[index]
}