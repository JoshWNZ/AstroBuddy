package com.pilot.astrobuddy.data.repository

import com.pilot.astrobuddy.data.local.OMLocationDao
import com.pilot.astrobuddy.data.local.entity.OMLocationSavedEntity
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.repository.SavedLocationRepository
import javax.inject.Inject

class SavedLocationRepositoryImpl @Inject constructor(
    private val locDao: OMLocationDao
):SavedLocationRepository {
    override suspend fun getLocation(id: Int): OMLocation {
        val loc = locDao.getLocation(id).toLocation()
        return loc
    }

    override suspend fun getAllLocations(): List<OMLocation> {
        val locs = locDao.getAllLocations().map{l-> l.toLocation()}
        return locs
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

    override suspend fun saveLocation(loc: Int) {
        locDao.saveLocation(OMLocationSavedEntity(loc))
    }

    override suspend fun unsaveLocation(id: Int) {
        locDao.unsaveLocation(id)
    }
}