package com.pilot.astrobuddy.data.remote

import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API definition for OpenMeteo forecasts.
 */
interface OpenMeteoApi {
    /**
     * Function to get a forecast from the openmeteo api.
     *
     * @param lat latitude
     * @param long longitude
     * @param hourly forecast fields (should not be overridden)
     * @param timezone timezone (should not be overridden)
     * @param days number of days to forecast
     * @param tempUnit temperature units (F or C)
     * @param speedUnit speed units (mph or kph)
     * @return Forecast result mapped to OMForecastDto object.
     */
    @GET("/v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: String,
        @Query("longitude") long : String,
        @Query("hourly") hourly: String =
                "temperature_2m," +
                "relativehumidity_2m," +
                "dewpoint_2m," +
                "apparent_temperature," +
                "precipitation_probability," +
                "cloudcover," +
                "cloudcover_low," +
                "cloudcover_mid," +
                "cloudcover_high," +
                "visibility," +
                "windspeed_10m," +
                "winddirection_10m," +
                "is_day",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") days: String,
        @Query("temperature_unit") tempUnit: String,
        @Query("windspeed_unit") speedUnit: String
    ) : OMForecastDto

}