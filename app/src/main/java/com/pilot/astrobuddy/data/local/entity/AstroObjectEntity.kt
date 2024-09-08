package com.pilot.astrobuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject

/**
 * RoomDB entity (table) for storing astro objects
 */
@Entity
data class AstroObjectEntity (
    @PrimaryKey
    val Name: String,
    val Type: String,
    val RA: String?="00:00:00.00",
    val Dec: String?="+00:00:00.00",
    val Const: String?="",
    val MajAx: String?="",
    val MinAx: String?="",
    val PosAng: String?="",
    val BMag: String?="",
    val VMag: String?="",
    val JMag: String?="",
    val HMag: String?="",
    val KMag: String?="",
    val SurfBr: String?="",
    val Hubble: String?="",
    val Pax: String?="",
    val PmRA: String?="",
    val PMDec: String?="",
    val RadVel: String?="",
    val Redshift: String?="",
    val CstarUMag: String?="",
    val CstarBMag: String?="",
    val CstarVMag: String?="",
    val M: String?="",
    val NGC: String?="",
    val IC: String?="",
    val CstarNames: String?="",
    val Identifiers: String?="",
    val CommonNames: String?=""
){
    /**
     * Convert db record to AstroObject
     *
     * @return AstroObject object
     */
    fun toAstroObject(): AstroObject{
        return AstroObject(Name, Type, RA, Dec, Const, MajAx, MinAx, PosAng, BMag, VMag, JMag,
            HMag, KMag, SurfBr, Hubble, Pax, PmRA, PMDec, RadVel, Redshift, CstarUMag, CstarBMag,
            CstarVMag, M, NGC, IC, CstarNames, Identifiers, CommonNames)
    }
}