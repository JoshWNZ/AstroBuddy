package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.OMLocationEntity
import com.pilot.astrobuddy.data.local.entity.OMLocationSavedEntity

/*
Dao to access and update the location and saved-location tables in the database
 */
@Dao
interface OMLocationDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(loc: OMLocationEntity)

    @Query("DELETE FROM OMLocationEntity WHERE (:delId) == id")
    suspend fun deleteLocation(delId: Int)

    @Query("SELECT * FROM OMLocationEntity WHERE (:getId) == id")
    suspend fun getLocation(getId: Int) : OMLocationEntity

    @Query("SELECT * FROM OMLocationEntity")
    suspend fun getAllLocations() : List<OMLocationEntity>

    @Query("SELECT * FROM OMLocationSavedEntity")
    suspend fun getAllSaved(): List<OMLocationSavedEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveLocation(loc: OMLocationSavedEntity)

    @Query("DELETE FROM OMLocationSavedEntity WHERE (:delId) == id")
    suspend fun unsaveLocation(delId: Int)

    //Query method to delete all locations which haven't been saved/bookmarked by the user
    @Query("DELETE FROM OMLocationEntity WHERE id NOT IN(SELECT id FROM OMLocationSavedEntity)")
    suspend fun deleteUnsaved()

    @Query("UPDATE OMLocationEntity SET name = (:name) WHERE (:id) == id")
    suspend fun renameLocation(id: Int, name: String)
}