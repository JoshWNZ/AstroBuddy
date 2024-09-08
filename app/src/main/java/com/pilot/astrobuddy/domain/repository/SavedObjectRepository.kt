package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject

/**
 * Repository interface for saved astro objects
 */
interface SavedObjectRepository {
    /**
     * Save an astro object
     * @param obj AstroObject to convert and insert
     */
    suspend fun insertObject(obj: AstroObject)

    /**
     * Delete an astro object
     *
     * @param delName name of the object to be deleted
     */
    suspend fun deleteObject(delName: String)

    /**
     * Fetch an astro object by name
     *
     * @param getName name of the object to fetch
     * @return resulting AstroObject
     */
    suspend fun getObject(getName: String) : AstroObject

    /**
     * Fetch all astro objects
     *
     * @return list of all AstroObjects
     */
    suspend fun getAllObjects() : List<AstroObject>
}