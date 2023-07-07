package com.pilot.astrobuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

@Entity
data class OMLocationEntity(
    val admin1: String?,
    val admin2: String?,
    val admin3: String?,
    val admin4: String?,
    val country: String,
    val country_code: String,
    val elevation: Double,
    @PrimaryKey val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
){
    fun toLocation(): OMLocation{
        return OMLocation(admin1, admin2, admin3, admin4, country, country_code, elevation, id, latitude, longitude, name)
    }
}