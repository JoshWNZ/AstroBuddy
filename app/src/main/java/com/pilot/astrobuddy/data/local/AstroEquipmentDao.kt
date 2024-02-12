package com.pilot.astrobuddy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pilot.astrobuddy.data.local.entity.AstroEquipmentEntity

@Dao
interface AstroEquipmentDao {
    @Query("SELECT * FROM AstroEquipmentEntity")
    suspend fun getAllAstroEquipment(): List<AstroEquipmentEntity>

    @Query("SELECT * FROM AstroEquipmentEntity WHERE ID == (:id)")
    suspend fun getAstroEquipment(id: Int): AstroEquipmentEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAstroEquipment(equip: AstroEquipmentEntity)

    @Query("DELETE FROM AstroEquipmentEntity WHERE id == (:id)")
    suspend fun unsaveAstroEquipment(id: Int)
}