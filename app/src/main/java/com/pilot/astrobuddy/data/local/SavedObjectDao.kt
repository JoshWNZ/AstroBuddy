package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity

@Dao
interface SavedObjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObject(loc: AstroObjectEntity)

    @Query("DELETE FROM AstroObjectEntity WHERE (:delName) == Name")
    suspend fun deleteObject(delName: String)

    @Query("SELECT * FROM AstroObjectEntity WHERE (:getName) == Name")
    suspend fun getObject(getName: String) : AstroObjectEntity

    @Query("SELECT * FROM AstroObjectEntity")
    suspend fun getAllObjects() : List<AstroObjectEntity>
}