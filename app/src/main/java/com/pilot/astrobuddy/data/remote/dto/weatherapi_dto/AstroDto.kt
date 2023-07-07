package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

import com.pilot.astrobuddy.domain.model.weatherapi.Astro

data class AstroDto(
    val moon_illumination: String,
    val moon_phase: String,
    val moonrise: String,
    val moonset: String,
    val sunrise: String,
    val sunset: String
){
    fun toAstro() : Astro {
        return Astro(
            moon_illumination = moon_illumination,
            moon_phase = moon_phase,
            moonrise = moonrise,
            moonset = moonset,
            sunrise = sunrise,
            sunset = sunset
            )
    }
}