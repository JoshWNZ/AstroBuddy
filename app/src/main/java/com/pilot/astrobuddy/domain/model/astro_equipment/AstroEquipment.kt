package com.pilot.astrobuddy.domain.model.astro_equipment

import com.pilot.astrobuddy.data.local.entity.AstroEquipmentEntity

data class AstroEquipment (
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
    fun toAstroEquipmentEntity(): AstroEquipmentEntity{
        return AstroEquipmentEntity(id,setupName, scopeName, focalLength, aperture, modifier, cameraName, verticalPixels, horizontalPixels, sensorWidth, sensorHeight, pixelScale)
    }
}