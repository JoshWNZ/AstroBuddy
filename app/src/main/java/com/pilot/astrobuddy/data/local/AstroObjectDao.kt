package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity

/**
 * Room DB DAO for interacting with AstroObject table.
 */
@Dao
interface AstroObjectDao {
    /**
     * Select all AstroObject records
     *
     * @return all AstroObjectEntities as a List
     */
    @Query("SELECT * FROM AstroObjectEntity")
    suspend fun getAllAstroObjects(): List<AstroObjectEntity>

    /**
     * Select a particular AstroObject record by name
     *
     * @param name name of the object
     * @return corresponding AstroObjectEntity record wrapped in a list
     */
    @Query("SELECT * FROM AstroObjectEntity " +
            "WHERE Name == (:name)"
    )
    suspend fun getAstroObject(name: String): List<AstroObjectEntity>

    /**
     * Search for AstroObjects by name or common names
     *
     * @param query search query
     * @return AstroObjectEntities as a List
     */
    @Query("SELECT * FROM AstroObjectEntity " +
            "WHERE Name LIKE '%'||(:query)||'%' " +
            "OR CommonNames LIKE '%'||(:query)||'%'"
    )
    suspend fun searchAstroObjects(query: String): List<AstroObjectEntity>
}
