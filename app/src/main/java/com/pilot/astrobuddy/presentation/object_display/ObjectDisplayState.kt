package com.pilot.astrobuddy.presentation.object_display

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
data class ObjectDisplayState(
    val isLoading: Boolean = false,
    val astroObject: AstroObject? = null,
    val error: String = "",
    val isSaved: Boolean = false
)