package com.pilot.astrobuddy.domain.use_case.get_objects

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import com.pilot.astrobuddy.domain.repository.SavedObjectRepository
import javax.inject.Inject

class GetSavedObjectUseCase @Inject constructor(
    private val repository: SavedObjectRepository
) {
    suspend fun insertObject(loc: AstroObject) {
        repository.insertObject(loc)
    }

    suspend fun deleteObject(delName: String) {
        repository.deleteObject(delName)
    }

    suspend fun getObject(getName: String): AstroObject {
        return repository.getObject(getName)
    }

    suspend fun getAllObjects(): List<AstroObject> {
        return repository.getAllObjects()
    }
}