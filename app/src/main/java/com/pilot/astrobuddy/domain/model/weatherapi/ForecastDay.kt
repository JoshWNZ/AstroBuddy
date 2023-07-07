package com.pilot.astrobuddy.domain.model.weatherapi

data class ForecastDay(
    val astro: Astro,
    val date: String,
    val date_epoch: Int,
    val hour: List<ForecastHour>
)
