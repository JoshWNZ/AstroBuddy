package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

data class LocationResultDto(
    val country: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val name: String,
    val region: String,
    val url: String
){
//    fun toLocation() : LocationResult {
//        return LocationResult(
//            country = country,
//            id = id,
//            lat = lat,
//            lon = lon,
//            name = name,
//            region = region,
//            url = url,
//            coord = "$lat,$lon"
//        )
//    }
}

