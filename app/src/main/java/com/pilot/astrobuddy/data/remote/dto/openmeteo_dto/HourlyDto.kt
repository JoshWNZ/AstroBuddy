package com.pilot.astrobuddy.data.remote.dto.openmeteo_dto

data class HourlyDto(
    val apparent_temperature: List<Double>,
    val cloudcover: List<Int>,
    val cloudcover_high: List<Int>,
    val cloudcover_low: List<Int>,
    val cloudcover_mid: List<Int>,
    val dewpoint_2m: List<Double>,
    val is_day: List<Int>,
    val precipitation_probability: List<Int?>,
    val relativehumidity_2m: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val visibility: List<Double>,
    val winddirection_10m: List<Int>,
    val windspeed_10m: List<Double>
)