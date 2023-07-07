package com.pilot.astrobuddy.data.remote

import com.pilot.astrobuddy.data.remote.dto.openmeteo_dto.OMLocationResultDto

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoSearchApi {
    @GET("/v1/search")
    suspend fun getLocations(
        @Query("name") query: String,
        @Query("count") count: String = "10",
        @Query("language") lang: String = "en",
        @Query("format") format: String = "json"
    ) : OMLocationResultDto
}