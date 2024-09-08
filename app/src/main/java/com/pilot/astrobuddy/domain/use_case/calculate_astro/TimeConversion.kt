package com.pilot.astrobuddy.domain.use_case.calculate_astro

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone

/**
 * Helper object containing timezone conversion functions
 */
object TimeConversion {
    /**
     * Convert from device timezone to UTC.
     *
     * @param time local time to convert
     * @return resulting UTC time
     */
    fun convertToUTC(time: LocalDateTime): ZonedDateTime {
        val timeZone = TimeZone.getDefault().toZoneId()
        val utcZone = ZoneId.of("UTC")

        val zonedDateTime = ZonedDateTime.of(time, timeZone)
        val utcDateTime = zonedDateTime.withZoneSameInstant(utcZone)
        return utcDateTime
    }

    /**
     * Convert from UTC to device timezone
     *
     * @param time UTC time to convert
     * @return resulting local time
     */
    fun convertToLocalTZ(time: LocalDateTime): ZonedDateTime {
        val timeZone = TimeZone.getDefault().toZoneId()
        val utcZone = ZoneId.of("UTC")

        val zonedDateTime = ZonedDateTime.of(time, utcZone)
        val localDateTime = zonedDateTime.withZoneSameInstant(timeZone)
        return localDateTime
    }
}