package com.pilot.astrobuddy.domain.use_case.get_locations

import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.repository.SavedLocationRepository
import javax.inject.Inject

/**
 * Use-case to define suspend functions to access location database
 */
class GetSavedLocUseCase @Inject constructor(
    private val repository: SavedLocationRepository
){
    /**
     * Fetch a location by Id
     *
     * @param id location id
     * @return corresponding OMLocation object
     */
    suspend fun getLocation(id: Int): OMLocation{
        return repository.getLocation(id)
    }

    /**
     * Fetch all locations
     *
     * @return List of all locations as OMLocation
     */
    suspend fun getAllLocations(): List<OMLocation>{
        return repository.getAllLocations()
    }

    /**
     * Insert a location into the database
     *
     * @param loc OMLocation to insert
     */
    suspend fun insertLocation(loc: OMLocation){
        repository.insertLocation(loc)
    }

    /**
     * Delete a specified location from the database
     *
     * @param id id of location to delete
     */
    suspend fun deleteLocation(id: Int){
        repository.deleteLocation(id)
    }

    /**
     * Fetch all saved location IDs
     *
     * @return List of Location IDs as Int
     */
    suspend fun getAllSaved(): List<Int>{
        return repository.getAllSaved()
    }

    /**
     * Delete all unsaved locations.
     */
    suspend fun deleteUnsaved(){
        repository.deleteUnsaved()
    }

    /**
     * Save a location ID
     *
     * @param id location ID to save
     */
    suspend fun saveLocation(id: Int){
        repository.saveLocation(id)
    }

    /**
     * Unsave a location ID
     *
     * @param id location id to unsave
     */
    suspend fun unsaveLocation(id: Int){
        repository.unsaveLocation(id)
    }

    /**
     * Rename a location
     *
     * @param id id of location to be renamed
     * @param name new name for the location
     */
    suspend fun renameLocation(id: Int, name: String){
        repository.renameLocation(id, name)
    }
}