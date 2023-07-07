package com.pilot.astrobuddy.data.remote.dto.openmeteo_dto

data class HourlyUnitsDto(
    val apparent_temperature: String,
    val cloudcover: String,
    val cloudcover_high: String,
    val cloudcover_low: String,
    val cloudcover_mid: String,
    val dewpoint_2m: String,
    val is_day: String,
    val precipitation_probability: String,
    val relativehumidity_2m: String,
    val temperature_2m: String,
    val time: String,
    val visibility: String,
    val winddirection_10m: String,
    val windspeed_10m: String
)