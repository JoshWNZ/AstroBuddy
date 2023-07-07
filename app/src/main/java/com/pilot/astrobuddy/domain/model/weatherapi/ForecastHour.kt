package com.pilot.astrobuddy.domain.model.weatherapi


data class ForecastHour(
    val chance_of_rain: Int,
    val cloud: Int,
    val dewpoint_c: Double,
    val feelslike_c: Double,
    val humidity: Int,
    val precip_mm: Double,
    val temp_c: Double,
    val time: String,
    val vis_km: Double,
    val wind_degree: Int,
    val wind_kph: Double
    )
