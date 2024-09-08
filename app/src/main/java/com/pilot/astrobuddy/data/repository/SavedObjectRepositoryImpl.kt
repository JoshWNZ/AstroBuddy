package com.pilot.astrobuddy.data.repository

import com.pilot.astrobuddy.data.local.SavedObjectDao
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import com.pilot.astrobuddy.domain.repository.SavedObjectRepository
import javax.inject.Inject

/**
 * Implementation of SavedObjectRepository utilising SavedObjectDao
 *
 * Dao is provided via hilt DI
 */
class SavedObjectRepositoryImpl @Inject constructor(
    private val saveObjDao: SavedObjectDao
): SavedObjectRepository {
    override suspend fun insertObject(obj: AstroObject) {
        saveObjDao.insertObject(obj.toAstroObjectEntity())
    }

    override suspend fun deleteObject(delName: String) {
        saveObjDao.deleteObject(delName)
    }

    override suspend fun getObject(getName: String): AstroObject {
        return saveObjDao.getObject(getName).toAstroObject()
    }

    override suspend fun getAllObjects(): List<AstroObject> {
        return saveObjDao.getAllObjects().map{e->e.toAstroObject()}
    }

}