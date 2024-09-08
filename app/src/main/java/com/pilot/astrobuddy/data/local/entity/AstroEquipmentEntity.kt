package com.pilot.astrobuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment

/**
 * RoomDB entity (table) for storing astro equipment
 */
@Entity
data class AstroEquipmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val setupName: String,
    val scopeName: String,
    val focalLength: Double,
    val aperture: Double,
    val modifier: Double,
    val cameraName: String,
    val verticalPixels: Int,
    val horizontalPixels: Int,
    val sensorWidth: Double,
    val sensorHeight: Double,
    val pixelScale: Double
){
    /**
     * Convert db record to a proper object
     *
     * @return AstroEquipment object
     */
    fun toAstroEquipment(): AstroEquipment{
        return AstroEquipment(id, setupName, scopeName, focalLength, aperture, modifier, cameraName, verticalPixels, horizontalPixels, sensorWidth, sensorHeight, pixelScale)
    }
}