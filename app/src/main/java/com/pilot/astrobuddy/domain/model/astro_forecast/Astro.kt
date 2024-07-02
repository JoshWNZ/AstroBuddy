package com.pilot.astrobuddy.domain.model.astro_forecast

import java.time.LocalDateTime

data class Astro(
    val moon_illumination: String,
    val moon_phase: String,
    val moonRiseSet: Pair<String,String>,
    val sunRiseSet: Pair<String,String>,
    val civilDark: Pair<LocalDateTime,LocalDateTime>,
    val nauticalDark: Pair<LocalDateTime,LocalDateTime>,
    val astroDark: Pair<LocalDateTime,LocalDateTime>
    )
