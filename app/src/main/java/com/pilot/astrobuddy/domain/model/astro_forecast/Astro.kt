package com.pilot.astrobuddy.domain.model.astro_forecast

import java.time.LocalDateTime

data class Astro(
    val moon_illumination: String,
    val moon_phase: String,
    val moonRiseSet: Pair<LocalDateTime,LocalDateTime>,
    val sunRiseSet: Pair<LocalDateTime,LocalDateTime>,
    val civilDark: Pair<LocalDateTime,LocalDateTime>,
    val nauticalDark: Pair<LocalDateTime,LocalDateTime>,
    val astroDark: Pair<LocalDateTime,LocalDateTime>
    )
