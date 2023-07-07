package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

data class CurrentDto(
    val condition: ConditionDto,
    val uv: Double
)