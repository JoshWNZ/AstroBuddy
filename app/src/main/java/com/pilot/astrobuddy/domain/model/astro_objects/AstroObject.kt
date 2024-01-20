package com.pilot.astrobuddy.domain.model.astro_objects

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

    //return the RA and DEC coords in decimal form (converted from Deg,Min,Sec)
    fun getDecimalCoords(): Pair<Double,Double>{
        if(RA.isNullOrEmpty() || Dec.isNullOrEmpty()){
            return Pair(0.0,0.0)
        }else{
            val raVals = RA.split(':').map { s -> s.toDouble() }
            val decVals = Dec.split(':').map { s -> s.toDouble() }

            val raDouble = raVals[0]+raVals[1]/60+raVals[2]/3600
            val decDouble = decVals[0]+decVals[1]/60+decVals[2]/3600

            return Pair(raDouble,decDouble)
        }
    }
}