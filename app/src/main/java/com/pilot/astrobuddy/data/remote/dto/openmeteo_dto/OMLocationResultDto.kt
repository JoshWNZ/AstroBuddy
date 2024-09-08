package com.pilot.astrobuddy.data.remote.dto.openmeteo_dto

/**
 * DTO for OpenMeteo location search results.
 */
data class OMLocationResultDto(
    val generationtime_ms: Double,
    val results: List<ResultDto>
){
//    fun toLocationResult(): OMLocationResult{
//        return OMLocationResult(
//            results = results.map{r->r.toLocation()}
//        )
//    }
}