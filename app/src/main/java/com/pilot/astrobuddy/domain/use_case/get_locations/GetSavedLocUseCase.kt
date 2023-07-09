package com.pilot.astrobuddy.domain.use_case.get_locations

import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.repository.SavedLocationRepository
import javax.inject.Inject
/*
Use-case to define suspend functions to access location database
 */
class GetSavedLocUseCase @Inject constructor(
    private val repository: SavedLocationRepository
){
    suspend fun getLocation(id: Int): OMLocation{
        return repository.getLocation(id)
    }
    suspend fun getAllLocations(): List<OMLocation>{
        return repository.getAllLocations()
    }
    suspend fun insertLocation(loc: OMLocation){
        repository.insertLocation(loc)
    }
    suspend fun deleteLocation(id: Int){
        repository.deleteLocation(id)
    }
    suspend fun getAllSaved(): List<Int>{
        return repository.getAllSaved()
    }
    suspend fun deleteUnsaved(){
        repository.deleteUnsaved()
    }
    suspend fun saveLocation(id: Int){
        repository.saveLocation(id)
    }
    suspend fun unsaveLocation(id: Int){
        repository.unsaveLocation(id)
    }
}