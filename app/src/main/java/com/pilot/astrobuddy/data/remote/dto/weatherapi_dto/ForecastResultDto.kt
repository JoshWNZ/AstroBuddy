package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

data class ForecastResultDto(
    val current: CurrentDto,
    val forecast: ForecastDto,
    val location: LocationDto
)