package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity

@Dao
interface AstroObjectDao {
    @Query("SELECT * FROM AstroObjectEntity")
    suspend fun getAllAstroObjects(): List<AstroObjectEntity>

    @Query("SELECT * FROM AstroObjectEntity " +
            "WHERE Name == (:name)"
    )
    suspend fun getAstroObject(name: String): List<AstroObjectEntity>

    @Query("SELECT * FROM AstroObjectEntity " +
            "WHERE Name LIKE '%'||(:query)||'%' " +
            "OR CommonNames LIKE '%'||(:query)||'%'"
    )
    suspend fun searchAstroObjects(query: String): List<AstroObjectEntity>
}
