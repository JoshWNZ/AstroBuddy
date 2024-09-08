package com.pilot.astrobuddy.domain.model.astro_objects

import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity

/**
 * Record to represent a deep-sky astronomical object.
 *
 * @param Name primary name of the object (NGC, IC, or M id)
 * @param Type type of object (galaxy, nebula, cluster, etc)
 * @param RA J2000 RA coordinates
 * @param Dec J2000 Dec coordinates
 * @param Const Constellation the object is contained within
 * @param MajAx the largest side length of the object (arcseconds)
 * @param MinAx the shortest side length of the object (arcseconds)
 * @param PosAng angle of orientation of the major axis (degrees)
 * @param BMag apparent magnitude in B-band (visible blue filter)
 * @param VMag apparent magnitude in V-band (visible green/yellow filter)
 * @param HMag apparent magnitude in H-band (near IR filter)
 * @param KMag apparent magnitude in K-band (deeper IR filter)
 * @param SurfBr surface brightness of the object (magnitude accounting for size)
 * @param Hubble hubble sequence classification (galaxies only)
 * @param Pax parallax of the object
 * @param PmRA proper motion in RA (milliarcseconds per year)
 * @param PMDec proper motion in Dec (milliarcseconds per year)
 * @param RadVel radial velocity, velocity towards (-) or away (+) from earth (possible KM/S)
 * @param Redshift redshift of the object, a function of velocity/distance from earth
 * @param CstarUMag component star magnitudes in U-band
 * @param CstarBMag component star magnitudes in B-band
 * @param CstarVMag component star magnitudes in V-band
 * @param M M-catalogue object identifier (000 - 110)
 * @param NGC NGC-catalogue object identifier (0000 - 9999)
 * @param IC IC-catalogue object identifier (0000 - 9999) (with sub-IDs sometimes)
 * @param CstarNames catalogue identifiers for component stars
 * @param Identifiers Other misc. catalogue identifiers for the object
 * @param CommonNames Common name(s) for the object
 */
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
    /**
     * Calculates and returns the object's apparent magnitude.
     * NEEDS FIXING, B-V average is not accurate, should use bolometric or something better
     *
     * @return apparent magnitude (or null if neither B or V specified in DB)
     */
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

    /**
     * Return the dimensions of the object in arcseconds.
     *
     * @return pair of String dimensions (Maj,Min) (blank string if unspecified).
     */
    fun getSize(): Pair<String, String> {
        return Pair(MajAx?:"",MinAx?:"")
    }

    /**
     * Convert AstroObject to Entity for use in database
     *
     * @return AstroObjectEntity
     */
    fun toAstroObjectEntity(): AstroObjectEntity{
        return AstroObjectEntity(Name, Type, RA, Dec, Const, MajAx, MinAx, PosAng, BMag, VMag, JMag,
            HMag, KMag, SurfBr, Hubble, Pax, PmRA, PMDec, RadVel, Redshift, CstarUMag, CstarBMag,
            CstarVMag, M, NGC, IC, CstarNames, Identifiers, CommonNames)
    }

}