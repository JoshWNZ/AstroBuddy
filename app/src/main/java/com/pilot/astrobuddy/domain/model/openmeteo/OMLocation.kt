package com.pilot.astrobuddy.domain.model.openmeteo

import com.pilot.astrobuddy.data.local.entity.OMLocationEntity

/**
 * Record to represent an OpenMeteo Location.
 * admin1-4 is dependent on location, generally city, region, state, but often differs.
 *
 * @param admin1 first administrative subdivision name
 * @param admin2 second administrative subdivision name
 * @param admin3 third administrative subdivision name
 * @param admin4 fourth administrative subdivision name
 * @param country country of the location
 * @param country_code two-letter code for the country (e.g. NZ, US)
 * @param elevation elevation of the location above sea level
 * @param id unique id for the location (defined by OM or by lat/long hashcode for custom locs)
 * @param latitude latitude of the location (decimal, i.e. (-)###.##### degrees)
 * @param longitude longitude of the location (decimal, i.e. (-)###.##### degrees)
 * @param name overall name of the location
 */
data class OMLocation(
    val admin1: String?,
    val admin2: String?,
    val admin3: String?,
    val admin4: String?,
    val country: String,
    val country_code: String,
    val elevation: Double,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
){
    /**
     * Convert the object to an Entity for use with the database
     */
    fun toLocationEntity(): OMLocationEntity{
        return OMLocationEntity(
            admin1,
            admin2,
            admin3,
            admin4,
            country,
            country_code,
            elevation,
            id,
            latitude,
            longitude,
            name
        )
    }

    /**
     * Companion object to create a dummy OMLocation
     */
    companion object Dummy{
        /**
         * Get a dummy OMLocation object
         *
         * @return OMLocation with all values set to 0 or empty string
         */
        fun getDummyOMLocation(): OMLocation{
            return OMLocation(
                "",
                "",
                "",
                "",
                "",
                "",
                0.0,
                0,
                0.0,
                0.0,
                "",
            )
        }
    }
}
