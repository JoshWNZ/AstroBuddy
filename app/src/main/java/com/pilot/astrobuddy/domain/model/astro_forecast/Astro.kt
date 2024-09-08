package com.pilot.astrobuddy.domain.model.astro_forecast

import java.time.LocalDateTime

/**
 * Record to represent an astronomical forecast
 *
 * @param moon_illumination moon illumination (% lit)
 * @param moon_phase moon phase (waxing/waning/full/new)
 * @param moonRiseSet pair of times for next moonrise and moonset
 * @param sunRiseSet pair of times for next sunrise and sunset
 * @param civilDark pair of times for civil dusk and civil dawn
 * @param nauticalDark pair of times for nautical dusk and nautical dawn
 * @param astroDark pair of times for astronomical dark and astronomical dawn
 */
data class Astro(
    val moon_illumination: String,
    val moon_phase: String,
    val moonRiseSet: Pair<LocalDateTime,LocalDateTime>,
    val sunRiseSet: Pair<LocalDateTime,LocalDateTime>,
    val civilDark: Pair<LocalDateTime,LocalDateTime>,
    val nauticalDark: Pair<LocalDateTime,LocalDateTime>,
    val astroDark: Pair<LocalDateTime,LocalDateTime>
)
