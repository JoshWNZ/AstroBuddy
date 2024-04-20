package com.pilot.astrobuddy.presentation.home_screen

import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

data class HomeScreenState (
    val isLoading: Boolean = false,
    val error: String = "",
    val editing: Boolean = false,
    val savedLocs: List<OMLocation> = emptyList(),
    val savedEquip: List<AstroEquipment> = emptyList(),
    val savedObjects: List<AstroObject> = emptyList(),
    val currentForecast: OMForecast? = null
)