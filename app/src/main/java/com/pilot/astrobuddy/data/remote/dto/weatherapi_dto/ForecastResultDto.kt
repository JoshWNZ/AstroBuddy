package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

import com.pilot.astrobuddy.domain.model.weatherapi.ForecastResult

data class ForecastResultDto(
    val current: CurrentDto,
    val forecast: ForecastDto,
    val location: LocationDto
){
    fun toForecastResult() : ForecastResult {
        return ForecastResult(
            forecast = forecast.forecastday.map{f->f.toForecastDay()},
            location = location.toLocation()
        )
    }
}