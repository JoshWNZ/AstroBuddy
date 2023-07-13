package com.pilot.astrobuddy.presentation.forecast_display.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.weatherapi.Astro

@Composable
fun ForecastCalendarItem(
    fd: OMForecast,
    astro: List<Astro>
) {
    Row(modifier = Modifier.fillMaxWidth()){
        LazyColumn(){
            items((fd.hourly.time.size/24)){
                Text(it.toString())
            }
        }
    }
}

private fun goodHour(fd: OMForecast, day: Int): Int{
    return 0
}