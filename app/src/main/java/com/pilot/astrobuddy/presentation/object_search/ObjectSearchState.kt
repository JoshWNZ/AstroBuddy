package com.pilot.astrobuddy.presentation.object_search

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject

data class ObjectSearchState(
    val isLoading: Boolean = false,
    val objects: List<AstroObject> = emptyList(),
    val error: String = ""
)