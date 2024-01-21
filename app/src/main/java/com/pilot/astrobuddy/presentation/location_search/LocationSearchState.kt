package com.pilot.astrobuddy.presentation.location_search

import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

/*
State record to contain location information
 */
data class LocationSearchState(
    val isLoading: Boolean = false,
    val locations: List<OMLocation> = emptyList(),
    val savedLocs: List<OMLocation> = emptyList(),
    val error: String = ""
)
