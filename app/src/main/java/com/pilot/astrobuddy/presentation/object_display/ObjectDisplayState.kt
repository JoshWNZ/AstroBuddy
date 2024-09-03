package com.pilot.astrobuddy.presentation.object_display

import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import java.time.LocalDateTime

data class ObjectDisplayState(
    val isLoading: Boolean = false,
    val astroObject: AstroObject? = null,
    val apparentPos: Pair<String,String>? = null,
    val riseSet: Pair<LocalDateTime,LocalDateTime>? = null,
    val transit: Pair<LocalDateTime,Double>? = null,
    val error: String = "",
    val isSaved: Boolean = false
)