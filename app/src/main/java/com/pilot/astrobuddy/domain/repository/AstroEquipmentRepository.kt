package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment

interface AstroEquipmentRepository {
    suspend fun getAllAstroEquipment(): List<AstroEquipment>

    suspend fun getAstroEquipment(id: Int): AstroEquipment

    suspend fun saveAstroEquipment(equip: AstroEquipment)

    suspend fun unsaveAstroEquipment(id: Int)
}