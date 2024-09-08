package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment

/**
 * Repository Interface for Astro Equipment
 */
interface AstroEquipmentRepository {
    /**
     * Returns all AstroEquipmentEntity records mapped to AstroEquipment objects.
     *
     * @return all AstroEquipment records as a list
     */
    suspend fun getAllAstroEquipment(): List<AstroEquipment>

    /**
     * Fetch a specific AstroEquipmentEntity record by ID
     *
     * @return astro equipment mapped to AstroEquipment object
     */
    suspend fun getAstroEquipment(id: Int): AstroEquipment

    /**
     * Converts and saves an AstroEquipment object
     */
    suspend fun saveAstroEquipment(equip: AstroEquipment)

    /**
     * Removes an AstroEquipment record from the DB by ID
     *
     * @param id the id of the equipment to be removed
     */
    suspend fun unsaveAstroEquipment(id: Int)
}