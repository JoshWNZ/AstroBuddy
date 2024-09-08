package com.pilot.astrobuddy.data.remote.dto.openmeteo_dto

import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

/**
 * DTO for OpenMeteo Locations.
 */
data class ResultDto(
    val admin1: String?,
    val admin1_id: Int?,
    val admin2: String?,
    val admin2_id: Int?,
    val admin3: String?,
    val admin3_id: Int?,
    val admin4: String?,
    val admin4_id: String?,
    val country: String,
    val country_code: String,
    val country_id: Int,
    val elevation: Double,
    val feature_code: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val population: Int?,
    val postcodes: List<String>?,
    val timezone: String
){
    /**
     * Convert DTO to OMLocation object
     *
     * @return OMLocation object
     */
    fun toLocation(): OMLocation{
        return OMLocation(
            admin1,
            admin2,
            admin3,
            admin4,
            country,
            country_code,
            elevation,
            id,
            latitude,
            longitude,
            name
        )
    }
}