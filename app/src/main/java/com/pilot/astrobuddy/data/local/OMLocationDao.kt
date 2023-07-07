package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.OMLocationEntity
import com.pilot.astrobuddy.data.local.entity.OMLocationSavedEntity

@Dao
interface OMLocationDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(loc: OMLocationEntity)

    @Query("DELETE FROM OMLocationEntity WHERE (:delId) == id")
    suspend fun deleteLocation(delId: Int)

    @Query("SELECT * FROM OMLocationEntity WHERE (:getId) == id")
    suspend fun getLocation(getId: Int) : OMLocationEntity

    @Query("SELECT * FROM OMLocationEntity")
    suspend fun getAllLocations() : List<OMLocationEntity>

    @Query("SELECT * FROM OMLocationSavedEntity")
    suspend fun getAllSaved(): List<OMLocationSavedEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveLocation(loc: OMLocationSavedEntity)

    @Query("DELETE FROM OMLocationSavedEntity WHERE (:delId)==id")
    suspend fun unsaveLocation(delId: Int)

    @Query("DELETE FROM OMLocationEntity WHERE id NOT IN(SELECT id FROM OMLocationSavedEntity)")
    suspend fun deleteUnsaved()
}