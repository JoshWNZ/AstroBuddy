package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.OMLocationEntity
import com.pilot.astrobuddy.data.local.entity.OMLocationSavedEntity

/**
 * Room DB DAO for interacting with OMLocation and OMSavedLocation tables
 */
@Dao
interface OMLocationDao{

    /**
     * Insert a new location record.
     *
     * @param loc location object to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(loc: OMLocationEntity)

    /**
     * Delete a location record by ID.
     *
     * @param delId ID of location to delete
     */
    @Query("DELETE FROM OMLocationEntity WHERE (:delId) == id")
    suspend fun deleteLocation(delId: Int)

    /**
     * Fetch a location by ID
     *
     * @param getId the location Id to fetch
     * @return the specified OMLocationEntity
     */
    @Query("SELECT * FROM OMLocationEntity WHERE (:getId) == id")
    suspend fun getLocation(getId: Int) : OMLocationEntity

    /**
     * Fetch all locations currently stored in the database
     *
     * @return all OMLocationEntities in a list.
     */
    @Query("SELECT * FROM OMLocationEntity")
    suspend fun getAllLocations() : List<OMLocationEntity>

    /**
     * Fetch all saved locations.
     *
     * @return all OMLocationSavedEntities in the saved loc table
     */
    @Query("SELECT * FROM OMLocationSavedEntity")
    suspend fun getAllSaved(): List<OMLocationSavedEntity>

    /**
     * Add a location to the saved loc table.
     *
     * @param loc the OMLocationSavedEntity (int wrapper) to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveLocation(loc: OMLocationSavedEntity)

    /**
     * Remove a location from the saved loc table.
     *
     * @param delId Location ID to remove
     */
    @Query("DELETE FROM OMLocationSavedEntity WHERE (:delId) == id")
    suspend fun unsaveLocation(delId: Int)

    /**
     * Query to delete all locations which haven't been saved/bookmarked by the user.
     * i.e any locations without a corresponding ID entry in the saved loc db will be deleted.
     */
    @Query("DELETE FROM OMLocationEntity WHERE id NOT IN(SELECT id FROM OMLocationSavedEntity)")
    suspend fun deleteUnsaved()

    /**
     * Rename a given location.
     *
     * @param id the ID of the location being renamed
     * @param name the new name to update
     */
    @Query("UPDATE OMLocationEntity SET name = (:name) WHERE (:id) == id")
    suspend fun renameLocation(id: Int, name: String)
}