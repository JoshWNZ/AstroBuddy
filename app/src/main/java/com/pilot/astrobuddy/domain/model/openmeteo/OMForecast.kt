package com.pilot.astrobuddy.domain.model.openmeteo

import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.HourlyDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.HourlyUnitsDto

data class OMForecast(
    val elevation: Double,
    val hourly: HourlyDto,
    val hourly_units: HourlyUnitsDto,
    val latitude: Double,
    val longitude: Double,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
    )
