package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject

/**
 * Repository interface for Astronomical Objects
 */
interface AstroObjectRepository {
    /**
     * Fetch all AstroObject records
     *
     * @return all objects mapped to AstroObjects, as a list
     */
    suspend fun getAllAstroObjects(): List<AstroObject>

    /**
     * Search astro object db and return results
     *
     * @param query search query
     * @return list of results mapped to AstroObject
     */
    suspend fun searchAstroObjects(query: String): List<AstroObject>

    /**
     * Fetch a specific AstroObject by name
     *
     * @param name name of object to fetch
     * @return List of results mapped to AstroObject (should only contain one entry)
     */
    suspend fun getAstroObject(name: String): List<AstroObject>
}