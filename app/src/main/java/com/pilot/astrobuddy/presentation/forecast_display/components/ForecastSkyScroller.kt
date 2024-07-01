package com.pilot.astrobuddy.presentation.forecast_display.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.domain.model.astro_forecast.Astro
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun ForecastSkyScroller(
    fd: OMForecast,
    astros: List<Astro>,
    listState: LazyListState
) {
    val scope = rememberCoroutineScope()

    LazyRow(
        state = listState,
        userScrollEnabled = false //TODO fix this, pass scroll back
    ){
        val hours = fd.hourly.time.size
        val days = hours / 24
        items(hours){i->
            val curDay = floor(i / 24.0).roundToInt()
            val curAstro = astros[curDay]
            Box(modifier = Modifier
                .width(30.dp)
                .fillMaxHeight()
                //.border(width = 1.dp, color = Color.Blue )
            ){

            }
        }
    }
}