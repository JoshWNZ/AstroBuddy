package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.data.local.entity.OMLocationSavedEntity
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

interface SavedLocationRepository{
    suspend fun getLocation(id: Int): OMLocation
    suspend fun getAllLocations(): List<OMLocation>
    suspend fun insertLocation(loc: OMLocation)
    suspend fun deleteLocation(id: Int)
    suspend fun getAllSaved(): List<Int>
    suspend fun deleteUnsaved()
    suspend fun saveLocation(id: Int)
    suspend fun unsaveLocation(id: Int)
}