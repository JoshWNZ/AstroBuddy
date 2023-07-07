package com.pilot.astrobuddy.presentation.forecast_display

import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.model.weatherapi.Astro

data class ForecastState(
    val isLoading: Boolean = false,
    val isAstroLoading: Boolean = false,
    val forecast: OMForecast? = null,
    val astro: List<Astro> = emptyList(),
    val error: String = "",
    val astroError: String = "",
    val isSaved: Boolean = false
)
