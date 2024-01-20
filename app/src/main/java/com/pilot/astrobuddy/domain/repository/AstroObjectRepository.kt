package com.pilot.astrobuddy.domain.repository

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject

interface AstroObjectRepository {
    suspend fun getAllAstroObjects(): List<AstroObject>

    suspend fun searchAstroObjects(query: String): List<AstroObject>

    suspend fun getAstroObject(name: String): List<AstroObject>
}