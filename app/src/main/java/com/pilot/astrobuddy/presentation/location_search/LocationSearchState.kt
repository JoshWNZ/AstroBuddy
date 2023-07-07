package com.pilot.astrobuddy.presentation.location_search

import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

data class LocationSearchState(
    val isLoading: Boolean = false,
    val locations: List<OMLocation> = emptyList(),
    val error: String = ""
)
