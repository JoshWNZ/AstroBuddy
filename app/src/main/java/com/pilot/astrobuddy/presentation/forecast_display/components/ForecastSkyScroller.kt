package com.pilot.astrobuddy.presentation.forecast_display.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.domain.model.astro_forecast.Astro
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast

@Composable
fun ForecastSkyScroller(
    fd: OMForecast,
    astros: List<Astro>,
    listState: LazyListState,
    scrollState: ScrollableState
) {
    val scope = rememberCoroutineScope()

    //Colours for times of day
    val skyMidDay = Color.hsv(235f,0.7f,0.9f)
    val skyCivilDark = Color.hsv(235f,1f,0.22f)
    val skyNauticalDark = Color.hsv(235f,1f,0.13f)
    val skyAstroDark = Color.hsv(235f,1f,0f)
    val skySunRiseSet = Color.hsv(27f,0.94f,0.9f)

    LazyRow(
        state = listState,
        userScrollEnabled = false,
        modifier = Modifier
            .scrollable(
                state = scrollState,
                orientation = Orientation.Horizontal,
                flingBehavior = ScrollableDefaults.flingBehavior()
            ).fillMaxHeight(fraction=0.995f)
    ){
        val hours = fd.hourly.time.size
        val days = hours / 24
        items(days){i->
            val curAstro = astros[i]
            val sunrise = curAstro.sunRiseSet.first

            Box(modifier = Modifier
                .width(720.dp)
                .fillMaxHeight()
                //.border(width = 1.dp, color = Color.Green)
            ){
                //androidx.compose.ui.geometry.Size()
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val horiSect = width / 96

                    //val sunRise =

                    //val dayOffset = Offset(x=,y=)

                    drawRect(color = skyAstroDark, size = size)
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp),
                color = Color.Blue
            )
        }
    }
}

/*
Calculate and return which section a given time belongs to
 */
fun getPosition(hour: Int, min: Int):Int{

    return 0
}