package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMForecastDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMLocationResultDto

interface ForecastRepository {
    suspend fun getOMForecast(lat: String, long: String, days: Int): OMForecastDto
    suspend fun getOMLocations(query: String): OMLocationResultDto
}