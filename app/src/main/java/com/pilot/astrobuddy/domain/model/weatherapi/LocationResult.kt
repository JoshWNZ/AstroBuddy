package com.pilot.astrobuddy.domain.model.weatherapi

data class LocationResult(
    val country: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val coord: String,
    val name: String,
    val region: String,
    val url: String
    )
