package com.pilot.astrobuddy.data.remote.dto.openmeteo_dto

import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast

data class OMForecastDto(
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly: HourlyDto,
    val hourly_units: HourlyUnitsDto,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
){
    fun toForecast(): OMForecast{
        return OMForecast(
            elevation,
            hourly,
            hourly_units,
            latitude,
            longitude,
            timezone_abbreviation,
            utc_offset_seconds
        )
    }
}