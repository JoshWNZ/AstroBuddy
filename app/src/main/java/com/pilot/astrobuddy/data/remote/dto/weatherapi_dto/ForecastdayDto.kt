package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

import com.pilot.astrobuddy.domain.model.weatherapi.ForecastDay

data class ForecastdayDto(
    val astro: AstroDto,
    val date: String,
    val date_epoch: Int,
    val day: DayDto,
    val hour: List<HourDto>
){
    fun toForecastDay(): ForecastDay {
        return ForecastDay(
        astro = astro.toAstro(),
        date = date,
        date_epoch = date_epoch,
        hour = hour.map{h->h.toForecastHour()}
        )
    }
}