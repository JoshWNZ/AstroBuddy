package com.pilot.astrobuddy.domain.model.astro_equipment

import com.pilot.astrobuddy.data.local.entity.AstroEquipmentEntity

/**
 * Record to represent an astro equipment setup
 *
 * @param id equipment id
 * @param setupName name of the whole setup
 * @param scopeName name of the telescope
 * @param focalLength focal length of the telescope (mm)
 * @param aperture aperture of the telescope (mm)
 * @param modifier FL modifier value (from barlows/reducers)
 * @param cameraName name of the camera
 * @param verticalPixels vertical sensor resolution
 * @param horizontalPixels horizontal sensor resolution
 * @param sensorWidth width of the camera sensor (mm)
 * @param sensorHeight height of the camera sensor (mm)
 */
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
    /**
     * Map the object to an Entity for database storage
     *
     * @return AstroEquipmentEntity object
     */
    fun toAstroEquipmentEntity(): AstroEquipmentEntity{
        return AstroEquipmentEntity(id,setupName, scopeName, focalLength, aperture, modifier, cameraName, verticalPixels, horizontalPixels, sensorWidth, sensorHeight, pixelScale)
    }

    /**
     * Companion object to provide a dummy/empty AstroEquipment object
     */
    companion object Dummy {
        /**
         * Get a dummy AstroEquipment object
         *
         * @return AstroEquipment with all parameters set to zero or empty strings
         */
        fun getDummyAstroEquipment(): AstroEquipment {
            return AstroEquipment(
                0,
                "",
                "",
                0.0,
                0.0,
                0.0,
                "",
                0,
                0,
                0.0,
                0.0,
                0.0,
            )
        }
    }
}