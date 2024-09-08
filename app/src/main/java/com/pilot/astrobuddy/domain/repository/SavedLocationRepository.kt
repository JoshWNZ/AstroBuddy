package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

/**
 * Repository interface for saved locations.
 */
interface SavedLocationRepository{
    /**
     * Fetch specific location.
     *
     * @param id of the location to fetch
     * @return location mapped to OMLocation object
     */
    suspend fun getLocation(id: Int): OMLocation

    /**
     * Fetch all location records.
     *
     * @return list of locations mapped to OMLocation objects
     */
    suspend fun getAllLocations(): List<OMLocation>

    /**
     * Map a location to OMLocationDTO and insert it into the database.
     *
     * @param loc OMLocation to insert
     */
    suspend fun insertLocation(loc: OMLocation)

    /**
     * Delete a specified location
     *
     * @param id id of the location to be deleted
     */
    suspend fun deleteLocation(id: Int)

    /**
     * Fetch all saved location IDs
     *
     * @return list of location Ids as Ints
     */
    suspend fun getAllSaved(): List<Int>

    /**
     * Delete all unsaved locations
     */
    suspend fun deleteUnsaved()

    /**
     * Save a location.
     *
     * @param id location id to save
     */
    suspend fun saveLocation(id: Int)

    /**
     * Unsave a location.
     *
     * @param id location id to remove
     */
    suspend fun unsaveLocation(id: Int)

    /**
     * Rename a location
     *
     * @param id id of the location to be renamed
     * @param name new name for the location
     */
    suspend fun renameLocation(id: Int, name: String)
}