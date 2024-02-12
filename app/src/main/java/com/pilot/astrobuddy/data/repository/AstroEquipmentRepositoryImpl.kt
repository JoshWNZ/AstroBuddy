package com.pilot.astrobuddy.data.repository

import com.pilot.astrobuddy.data.local.AstroEquipmentDao
import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment
import com.pilot.astrobuddy.domain.repository.AstroEquipmentRepository
import javax.inject.Inject

class AstroEquipmentRepositoryImpl @Inject constructor(
    private val equipDao: AstroEquipmentDao
): AstroEquipmentRepository {
    override suspend fun getAllAstroEquipment(): List<AstroEquipment> {
        return equipDao.getAllAstroEquipment().map{e-> e.toAstroEquipment()}
    }

    override suspend fun getAstroEquipment(id: Int): AstroEquipment {
        return equipDao.getAstroEquipment(id).toAstroEquipment()
    }

    override suspend fun saveAstroEquipment(equip: AstroEquipment) {
        equipDao.saveAstroEquipment(equip.toAstroEquipmentEntity())
    }

    override suspend fun unsaveAstroEquipment(id: Int) {
        equipDao.unsaveAstroEquipment(id)
    }

}