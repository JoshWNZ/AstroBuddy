package com.pilot.astrobuddy.data.repository

import com.pilot.astrobuddy.data.local.AstroObjectDao
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import com.pilot.astrobuddy.domain.repository.AstroObjectRepository
import javax.inject.Inject

/**
 * Implementation of AstroobjectRepository utilising AstroObjectDao
 *
 * DAO is provided via hilt DI
 */
class AstroObjectRepositoryImpl @Inject constructor(
    private val objDao: AstroObjectDao
) : AstroObjectRepository {
    override suspend fun getAllAstroObjects(): List<AstroObject> {
        return objDao.getAllAstroObjects().map { e -> e.toAstroObject() }
    }

    override suspend fun searchAstroObjects(query: String): List<AstroObject> {
        return objDao.searchAstroObjects(query).map { e -> e.toAstroObject() }
    }

    override suspend fun getAstroObject(name: String): List<AstroObject> {
        return objDao.getAstroObject(name).map { e -> e.toAstroObject() }
    }

}