package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.AstroEquipmentEntity

/**
 * Room DB DAO for interacting with AstroEquipment table.
 */
@Dao
interface AstroEquipmentDao {
    /**
     * Select all AstroEquipment records
     *
     * @return all AstroEquipmentEntities as a List
     */
    @Query("SELECT * FROM AstroEquipmentEntity")
    suspend fun getAllAstroEquipment(): List<AstroEquipmentEntity>

    /**
     * Select a particular AstroEquipment record by ID
     *
     * @param id the id to fetch
     * @return corresponding AstroEquipmentEntity record
     */
    @Query("SELECT * FROM AstroEquipmentEntity WHERE ID == (:id)")
    suspend fun getAstroEquipment(id: Int): AstroEquipmentEntity

    /**
     * Insert a new AstroEquipment record.
     * Will replace on conflicting ID.
     *
     * @param equip the AstroEquipmentEntity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAstroEquipment(equip: AstroEquipmentEntity)

    /**
     * Delete an AstroEquipment record with the given ID
     *
     * @param id the id of the record to be deleted
     */
    @Query("DELETE FROM AstroEquipmentEntity WHERE id == (:id)")
    suspend fun unsaveAstroEquipment(id: Int)
}