package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

data class DayDto(
    val condition: ConditionDto,
    val totalsnow_cm: Double
)