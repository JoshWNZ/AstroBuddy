package com.pilot.astrobuddy.data.remote

import com.pilot.astrobuddy.data.remote.dto.weatherapi_dto.ForecastResultDto
import com.pilot.astrobuddy.data.remote.dto.weatherapi_dto.LocationResultDto

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/forecast.json")
    suspend fun getForecast(
        @Query("key") key: String = "2ebd6854d896429cba071311232506",
        @Query("q") location: String,
        @Query("days") days : String = "3",
        @Query("aqi") aqi : String = "no",
        @Query("alerts") alerts : String = "no"
    ) : ForecastResultDto

    @GET("/v1/search.json")
    suspend fun getLocations(
        @Query("key") key: String = "2ebd6854d896429cba071311232506",
        @Query("q") query : String
    ) : List<LocationResultDto>
}