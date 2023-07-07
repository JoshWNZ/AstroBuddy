package com.pilot.astrobuddy.presentation.location_search

import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

data class SavedLocState (
    val favourites: List<OMLocation> = emptyList()
)