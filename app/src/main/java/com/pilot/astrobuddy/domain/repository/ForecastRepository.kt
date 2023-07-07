package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMForecastDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMLocationResultDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.ResultDto
import com.pilot.astrobuddy.data.remote.dto.weatherapi_dto.ForecastResultDto
import com.pilot.astrobuddy.data.remote.dto.weatherapi_dto.LocationResultDto

interface ForecastRepository {

    suspend fun getWAForecast(location: String): ForecastResultDto

    suspend fun getWALocations(query: String): List<LocationResultDto>

    suspend fun getOMForecast(lat: String, long: String): OMForecastDto

    suspend fun getOMLocations(query: String): OMLocationResultDto
}