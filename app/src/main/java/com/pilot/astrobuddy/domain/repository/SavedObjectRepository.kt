package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject

interface SavedObjectRepository {
    suspend fun insertObject(loc: AstroObject)
    suspend fun deleteObject(delName: String)
    suspend fun getObject(getName: String) : AstroObject
    suspend fun getAllObjects() : List<AstroObject>
}