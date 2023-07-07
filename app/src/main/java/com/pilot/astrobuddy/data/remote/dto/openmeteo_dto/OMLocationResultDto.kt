package com.pilot.astrobuddy.data.remote.dto.openmeteo_dto

import com.pilot.astrobuddy.domain.model.openmeteo.OMLocationResult

data class OMLocationResultDto(
    val generationtime_ms: Double,
    val results: List<ResultDto>
){
    fun toLocationResult(): OMLocationResult{
        return OMLocationResult(
            results = results.map{r->r.toLocation()}
        )
    }
}