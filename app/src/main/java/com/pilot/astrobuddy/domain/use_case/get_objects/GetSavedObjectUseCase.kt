package com.pilot.astrobuddy.domain.use_case.get_objects

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import com.pilot.astrobuddy.domain.repository.SavedObjectRepository
import javax.inject.Inject

/**
 * Use-case to interact with saved-object database
 */
class GetSavedObjectUseCase @Inject constructor(
    private val repository: SavedObjectRepository
) {
    /**
     * Insert an object into the savedObject database
     *
     * @param obj AstroObject to insert
     */
    suspend fun insertObject(obj: AstroObject) {
        repository.insertObject(obj)
    }

    /**
     * Delete an object from the savedObject database
     *
     * @param delName name of the object to be removed
     */
    suspend fun deleteObject(delName: String) {
        repository.deleteObject(delName)
    }

    /**
     * Fetch a saved object
     *
     * @param getName name of the object to fetch
     * @return specified AstroObject
     */
    suspend fun getObject(getName: String): AstroObject {
        return repository.getObject(getName)
    }

    /**
     * Fetch all saved objects
     *
     * @return List of all saved AstroObjects
     */
    suspend fun getAllObjects(): List<AstroObject> {
        return repository.getAllObjects()
    }
}