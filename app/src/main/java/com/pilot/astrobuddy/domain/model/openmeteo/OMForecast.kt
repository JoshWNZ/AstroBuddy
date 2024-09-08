package com.pilot.astrobuddy.domain.model.openmeteo

import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.HourlyDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.HourlyUnitsDto

/**
 * Record to represent an OpenMeteo Forecast
 *
 * @param elevation altitude of observation
 * @param hourly hourly forecast info  (contains lists of data)
 * @param hourly_units units of measurement used in hourly forecast
 * @param latitude latitude of observation
 * @param longitude longitude of observation
 * @param timezone_abbreviation timezone of forecast times
 * @param utc_offset_seconds utc offset defined by timezone
 */
data class OMForecast(
    val elevation: Double,
    val hourly: HourlyDto,
    val hourly_units: HourlyUnitsDto,
    val latitude: Double,
    val longitude: Double,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
    )
