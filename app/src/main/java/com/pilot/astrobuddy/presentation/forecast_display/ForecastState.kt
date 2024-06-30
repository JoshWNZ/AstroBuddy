package com.pilot.astrobuddy.presentation.forecast_display

import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.weatherapi.Astro

/*
State record to hold up-to-date information to update the ui
 */
data class ForecastState(
    val isLoading: Boolean = false,
    val isAstroLoading: Boolean = false,
    val forecast: OMForecast? = null,
    val error: String = "",
    val astroError: String = "",
    val isSaved: Boolean = false,
    val calendar: Boolean = false,
    val astro: List<Astro> = emptyList()
)
