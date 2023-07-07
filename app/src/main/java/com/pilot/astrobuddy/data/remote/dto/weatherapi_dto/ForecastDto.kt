package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

import com.pilot.astrobuddy.domain.model.weatherapi.Astro

data class ForecastDto(
    val forecastday: List<ForecastdayDto>
){
    fun toAstroList(): List<Astro>{
        return forecastday.map{fd->fd.astro.toAstro()}
    }
}