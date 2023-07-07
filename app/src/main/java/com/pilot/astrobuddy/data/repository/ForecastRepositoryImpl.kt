package com.pilot.astrobuddy.data.repository

import com.pilot.astrobuddy.data.remote.OpenMeteoApi
import com.pilot.astrobuddy.data.remote.OpenMeteoSearchApi
import com.pilot.astrobuddy.data.remote.WeatherApi
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMForecastDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMLocationResultDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.ResultDto
import com.pilot.astrobuddy.data.remote.dto.weatherapi_dto.ForecastResultDto
import com.pilot.astrobuddy.data.remote.dto.weatherapi_dto.LocationResultDto
import com.pilot.astrobuddy.domain.repository.ForecastRepository
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val apiWA: WeatherApi,
    private val apiOM: OpenMeteoApi,
    private val apiSOM: OpenMeteoSearchApi
) : ForecastRepository {
    override suspend fun getWAForecast(location: String): ForecastResultDto {
        return apiWA.getForecast(location = location)
    }
    override suspend fun getWALocations(query: String): List<LocationResultDto> {
        return apiWA.getLocations(query = query)
    }

    override suspend fun getOMForecast(lat: String, long: String): OMForecastDto{
        return apiOM.getForecast(lat=lat,long=long)
    }

    override suspend fun getOMLocations(query: String): OMLocationResultDto{
        return apiSOM.getLocations(query=query)
    }


}