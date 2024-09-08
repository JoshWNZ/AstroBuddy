package com.pilot.astrobuddy.data.repository

import com.pilot.astrobuddy.data.local.OMLocationDao
import com.pilot.astrobuddy.data.local.entity.OMLocationSavedEntity
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.repository.SavedLocationRepository
import javax.inject.Inject

/**
 * Implementation of SavedLocationRepository utilising OMLocationDao
 *
 * Dao is provided via hilt DI
 */
class SavedLocationRepositoryImpl @Inject constructor(
    private val locDao: OMLocationDao
):SavedLocationRepository {
    override suspend fun getLocation(id: Int): OMLocation {
        return locDao.getLocation(id).toLocation()
    }

    override suspend fun getAllLocations(): List<OMLocation> {
        return locDao.getAllLocations().map { l -> l.toLocation() }
    }

    override suspend fun insertLocation(loc: OMLocation) {
        locDao.insertLocation(loc.toLocationEntity())
    }

    override suspend fun deleteLocation(id: Int) {
        locDao.deleteLocation(id)
    }

    override suspend fun getAllSaved(): List<Int> {
        return locDao.getAllSaved().map{s->s.toId()}
    }

    override suspend fun deleteUnsaved(){
        locDao.deleteUnsaved()
    }

    override suspend fun saveLocation(id: Int) {
        locDao.saveLocation(OMLocationSavedEntity(id))
    }

    override suspend fun unsaveLocation(id: Int) {
        locDao.unsaveLocation(id)
    }

    override suspend fun renameLocation(id: Int, name: String) {
        locDao.renameLocation(id, name)
    }
}