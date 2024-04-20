package com.pilot.astrobuddy.domain.model.openmeteo

import com.pilot.astrobuddy.data.local.entity.OMLocationEntity

data class OMLocation(
    val admin1: String?,
    val admin2: String?,
    val admin3: String?,
    val admin4: String?,
    val country: String,
    val country_code: String,
    val elevation: Double,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
){
    fun toLocationEntity(): OMLocationEntity{
        return OMLocationEntity(
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

    companion object Dummy{
        fun getDummyOMLocation(): OMLocation{
            return OMLocation(
                "",
                "",
                "",
                "",
                "",
                "",
                0.0,
                0,
                0.0,
                0.0,
                "",
            )
        }
    }
}
