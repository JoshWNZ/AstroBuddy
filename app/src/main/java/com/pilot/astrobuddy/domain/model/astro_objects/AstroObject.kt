package com.pilot.astrobuddy.domain.model.astro_objects

import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity

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
    //returns the magnitude as an average of BMag and VMag
    fun getMagnitude(): Double?{

        val vMag = VMag?.toDouble()
        val bMag = BMag?.toDouble()

        if(vMag == null){
            return bMag
        }

        if(bMag == null){
            return vMag
        }

        return (vMag + bMag) / 2
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