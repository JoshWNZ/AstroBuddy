package com.pilot.astrobuddy.presentation.forecast_display.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.weatherapi.Astro

@Composable
fun ForecastCalendarItem(
    fd: OMForecast,
    astro: List<Astro>
) {
    Text(text="placeholder")
}