package com.pilot.astrobuddy.domain.model.astro_objects

import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity
import kotlin.math.log10

data class AstroObject (
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
    //returns the apparent magnitude as an average of BMag and VMag
    fun getMagnitude(): Double?{

        val vMag = VMag?.toDouble()
        val bMag = BMag?.toDouble()

        var appVMag: Double? = null
        var appBMag: Double? = null

        if(vMag != null){
            appVMag = -2.5 * log10(vMag)
        }
        if(bMag != null){
            appBMag = -2.5 * log10(bMag)
        }

        if(appVMag == null && appBMag == null){
            return null
        }

        if(appVMag == null){
            return appBMag
        }

        if(appBMag == null){
            return appVMag
        }

        return (appVMag + appBMag) / 2
    }

    //return the arcsec dimensions of the object (Maj, Min)
    fun getSize(): Pair<String, String> {
        return Pair(MajAx?:"",MinAx?:"")
    }

    fun toAstroObjectEntity(): AstroObjectEntity{
        return AstroObjectEntity(Name, Type, RA, Dec, Const, MajAx, MinAx, PosAng, BMag, VMag, JMag,
            HMag, KMag, SurfBr, Hubble, Pax, PmRA, PMDec, RadVel, Redshift, CstarUMag, CstarBMag,
            CstarVMag, M, NGC, IC, CstarNames, Identifiers, CommonNames)
    }

}