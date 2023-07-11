package com.pilot.astrobuddy.data.repository

import com.pilot.astrobuddy.data.remote.OpenMeteoApi
import com.pilot.astrobuddy.data.remote.OpenMeteoSearchApi
import com.pilot.astrobuddy.data.remote.WeatherApi
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMForecastDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMLocationResultDto
import com.pilot.astrobuddy.data.remote.dto.weatherapi_dto.ForecastResultDto
import com.pilot.astrobuddy.data.remote.dto.weatherapi_dto.LocationResultDto
import com.pilot.astrobuddy.domain.repository.ForecastRepository
import com.pilot.astrobuddy.setings_store.SettingsStore
import javax.inject.Inject
/*
Implementation of the forecastrepository, which takes the various apis and applies the relevant methods
 */
class ForecastRepositoryImpl @Inject constructor(
    private val apiWA: WeatherApi,
    private val apiOM: OpenMeteoApi,
    private val apiSOM: OpenMeteoSearchApi,
    private val settingsStore: SettingsStore
) : ForecastRepository {
    override suspend fun getWAForecast(location: String): ForecastResultDto {
        return apiWA.getForecast(location = location)
    }
    override suspend fun getWALocations(query: String): List<LocationResultDto> {
        return apiWA.getLocations(query = query)
    }

    override suspend fun getOMForecast(lat: String, long: String, days: Int): OMForecastDto{
        val unit = settingsStore.getUnitsFromDataStore()
        val speedUnit = if(unit=="C"){"kmh"}else{"mph"}
        val tempUnit = if(unit=="C"){"celsius"}else{"fahrenheit"}
        return apiOM.getForecast(lat=lat,long=long,days = days.toString(), speedUnit = speedUnit, tempUnit = tempUnit)
    }

    override suspend fun getOMLocations(query: String): OMLocationResultDto{
        return apiSOM.getLocations(query=query)
    }


}