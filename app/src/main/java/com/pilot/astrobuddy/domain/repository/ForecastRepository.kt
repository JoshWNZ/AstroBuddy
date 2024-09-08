package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMForecastDto
import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMLocationResultDto

/**
 * Repository interface for forecasts
 */
interface ForecastRepository {
    /**
     * Fetch a forecast from the API
     *
     * @param lat latitude
     * @param long longitude
     * @param days number of days to forecast
     * @return OMForecastDto object
     */
    suspend fun getOMForecast(lat: String, long: String, days: Int): OMForecastDto

    /**
     * Fetch location search results
     *
     * @param query search query
     * @return OMLocationResultDto object containing results
     */
    suspend fun getOMLocations(query: String): OMLocationResultDto
}