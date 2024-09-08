package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity

/**
 * Room DB DAO for interacting with the savedObject database.
 */
@Dao
interface SavedObjectDao {

    /**
     * Save a new object.
     * On conflict, replaces the old entry.
     *
     * @param loc the AstroObjectEntity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObject(loc: AstroObjectEntity)

    /**
     * Remove an object from the db.
     *
     * @param delName the name of the object to be deleted
     */
    @Query("DELETE FROM AstroObjectEntity WHERE (:delName) == Name")
    suspend fun deleteObject(delName: String)

    /**
     * Fetch a saved object by name
     *
     * @param getName the name of the object to fetch
     * @return the corresponding AstroObjectEntity
     */
    @Query("SELECT * FROM AstroObjectEntity WHERE (:getName) == Name")
    suspend fun getObject(getName: String) : AstroObjectEntity

    /**
     * Fetch all saved AstroObjectEntities
     *
     * @return all AstroObjectEntity records in a list.
     */
    @Query("SELECT * FROM AstroObjectEntity")
    suspend fun getAllObjects() : List<AstroObjectEntity>
}