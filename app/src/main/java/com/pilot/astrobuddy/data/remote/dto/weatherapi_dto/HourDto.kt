package com.pilot.astrobuddy.data.remote.dto.weatherapi_dto

import com.pilot.astrobuddy.domain.model.weatherapi.ForecastHour

data class HourDto(
    val chance_of_rain: Int,
    val cloud: Int,
    val condition: ConditionDto,
    val dewpoint_c: Double,
    val feelslike_c: Double,
    val humidity: Int,
    val precip_mm: Double,
    val temp_c: Double,
    val time: String,
    val vis_km: Double,
    val wind_degree: Int,
    val wind_kph: Double
){
    fun toForecastHour() : ForecastHour {
        return ForecastHour(
            chance_of_rain = chance_of_rain,
            cloud = cloud,
            dewpoint_c = dewpoint_c,
            feelslike_c = feelslike_c,
            humidity = humidity,
            precip_mm = precip_mm,
            temp_c = temp_c,
            time = time,
            vis_km = vis_km,
            wind_degree = wind_degree,
            wind_kph = wind_kph
        )
    }
}