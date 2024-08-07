package com.pilot.astrobuddy.domain.use_case.calculate_sunmoon

import io.github.cosinekitty.astronomy.Body
import io.github.cosinekitty.astronomy.Direction
import io.github.cosinekitty.astronomy.Observer
import io.github.cosinekitty.astronomy.Time
import io.github.cosinekitty.astronomy.illumination
import io.github.cosinekitty.astronomy.moonPhase
import io.github.cosinekitty.astronomy.searchAltitude
import io.github.cosinekitty.astronomy.searchRiseSet
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import kotlin.math.round

/*
  Use-case to calculate rising and setting for the sun and moon.
 */
object CalculateSunMoonUseCase {

    /*
      Convert from device timezone to utc
     */
    private fun convertToUTC(time: LocalDateTime): ZonedDateTime{
        val timeZone = TimeZone.getDefault().toZoneId()
        val utcZone = ZoneId.of("UTC")

        val zonedDateTime = ZonedDateTime.of(time, timeZone)
        val utcDateTime = zonedDateTime.withZoneSameInstant(utcZone)
        return utcDateTime
    }

    /*
      Convert from UTC to device timezone
     */
    private fun convertToLocalTZ(time: LocalDateTime): ZonedDateTime{
        val timeZone = TimeZone.getDefault().toZoneId()
        val utcZone = ZoneId.of("UTC")

        val zonedDateTime = ZonedDateTime.of(time, utcZone)
        val localDateTime = zonedDateTime.withZoneSameInstant(timeZone)
        return localDateTime
    }

    /*
      Calculate the time of sunrise and sunset for a given day
      Returns a pair of strings in the specified time format
     */
    fun calculateSunRiseSet(time: String, latitude: String, longitude: String, elevation: Double): Pair<LocalDateTime,LocalDateTime> {

        val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val utcDateTime = convertToUTC(localDateTime)

        val obs = Observer(latitude.toDouble(), longitude.toDouble(), elevation)

        val astTime = Time(
            utcDateTime.year,
            utcDateTime.monthValue,
            utcDateTime.dayOfMonth,
            utcDateTime.hour,
            utcDateTime.minute,
            utcDateTime.second.toDouble()
        )

        val sunRise = searchRiseSet(Body.Sun,obs,Direction.Rise,astTime,1.0,elevation)
        val sunSet = searchRiseSet(Body.Sun,obs,Direction.Set,astTime,1.0,elevation)

        var sunRiseUserDateTime: LocalDateTime = LocalDateTime.MIN
        var sunSetUserDateTime: LocalDateTime = LocalDateTime.MIN

        if(sunRise != null){
            val sunRiseDateTime = sunRise.toDateTime()
            val sunRiseLocalDateTime = LocalDateTime.parse(sunRiseDateTime.toString().dropLast(5), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            sunRiseUserDateTime = convertToLocalTZ(sunRiseLocalDateTime).toLocalDateTime()
        }
        if(sunSet != null){
            val sunSetDateTime = sunSet.toDateTime()
            val sunSetLocalDateTime = LocalDateTime.parse(sunSetDateTime.toString().dropLast(5), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            sunSetUserDateTime = convertToLocalTZ(sunSetLocalDateTime).toLocalDateTime()
        }

        return Pair(sunRiseUserDateTime,sunSetUserDateTime)
    }

    /*
      Calculate the time of moonrise and moonset for a given day
      Returns a pair of strings in the specified time format
     */
    fun calculateMoonRiseSet(time: String, latitude: String, longitude: String, elevation: Double): Pair<LocalDateTime,LocalDateTime> {

        val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val utcDateTime = convertToUTC(localDateTime)

        val obs = Observer(latitude.toDouble(), longitude.toDouble(), elevation)

        val astTime = Time(
            utcDateTime.year,
            utcDateTime.monthValue,
            utcDateTime.dayOfMonth,
            utcDateTime.hour,
            utcDateTime.minute,
            utcDateTime.second.toDouble()
        )

        val moonRise = searchRiseSet(Body.Moon,obs,Direction.Rise,astTime,1.0,elevation)
        val moonSet = searchRiseSet(Body.Moon,obs,Direction.Set,astTime,1.0,elevation)

        var moonRiseUserDateTime: LocalDateTime = LocalDateTime.MIN
        var moonSetUserDateTime: LocalDateTime = LocalDateTime.MIN

        if(moonRise != null){
            val moonRiseDateTime = moonRise.toDateTime()
            val moonRiseLocalDateTime = LocalDateTime.parse(moonRiseDateTime.toString().dropLast(5), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            moonRiseUserDateTime = convertToLocalTZ(moonRiseLocalDateTime).toLocalDateTime()
        }
        if(moonSet != null){
            val moonSetDateTime = moonSet.toDateTime()
            val moonSetLocalDateTime = LocalDateTime.parse(moonSetDateTime.toString().dropLast(5), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            moonSetUserDateTime = convertToLocalTZ(moonSetLocalDateTime).toLocalDateTime()
        }

        return Pair(moonRiseUserDateTime,moonSetUserDateTime)
    }

    /*
      Calculate the percentage of illumination and the phase of the moon for a given day
      Returns a pair of strings containing illumination then phase.
     */
    fun calculateMoonIllumPhase(time: String): Pair<String,String> {
        val moonIllumString: String
        val moonPhaseString: String

        val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val astTime = Time(
            localDateTime.year,
            localDateTime.monthValue,
            localDateTime.dayOfMonth,
            localDateTime.hour,
            localDateTime.minute,
            localDateTime.second.toDouble()
        )

        val phaseDeg = moonPhase(astTime)
        val illum = round(illumination(Body.Moon, astTime).phaseFraction*100).toInt()

        moonPhaseString = when{
            phaseDeg < 5 || phaseDeg > 355 -> "New Moon"
            phaseDeg >= 5 && phaseDeg < 85 -> "Waxing Crescent"
            phaseDeg >= 85 && phaseDeg < 95 -> "First Quarter"
            phaseDeg >= 95 && phaseDeg < 175 -> "Waxing Gibbous"
            phaseDeg >= 175 && phaseDeg < 185 -> "Full Moon"
            phaseDeg >= 185 && phaseDeg < 265 -> "Waning Gibbous"
            phaseDeg >= 265 && phaseDeg < 275 -> "Last Quarter"
            phaseDeg >= 275 -> "Waning Crescent"
            else -> "N/A"
        }

        moonIllumString = illum.toString()

        return Pair(moonIllumString,moonPhaseString)
    }

    private fun calcDuskDawn(time: String, latitude: String, longitude: String, elevation: Double, altitude: Double): Pair<LocalDateTime,LocalDateTime>{
        val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val astTime = Time(
            localDateTime.year,
            localDateTime.monthValue,
            localDateTime.dayOfMonth,
            localDateTime.hour,
            localDateTime.minute,
            localDateTime.second.toDouble()
        )

        val obs = Observer(latitude.toDouble(), longitude.toDouble(), elevation)

        val dusk = searchAltitude(Body.Sun,obs,Direction.Set,astTime,1.0,altitude)
        val dawn = searchAltitude(Body.Sun,obs,Direction.Rise,astTime,1.0,altitude)

        var duskLocalDateTime: LocalDateTime = LocalDateTime.MIN
        var dawnLocalDateTime: LocalDateTime = LocalDateTime.MIN

        if(dusk != null){
            duskLocalDateTime = convertToLocalTZ(LocalDateTime.parse(dusk.toDateTime().toString().dropLast(5), DateTimeFormatter.ISO_LOCAL_DATE_TIME)).toLocalDateTime()
        }
        if(dawn != null){
            dawnLocalDateTime = convertToLocalTZ(LocalDateTime.parse(dawn.toDateTime().toString().dropLast(5), DateTimeFormatter.ISO_LOCAL_DATE_TIME)).toLocalDateTime()
        }

        return Pair(duskLocalDateTime,dawnLocalDateTime)
    }

    fun calcCivilDark(time: String, latitude: String, longitude: String, elevation: Double): Pair<LocalDateTime,LocalDateTime>{
        return calcDuskDawn(time,latitude,longitude,elevation,-6.0)
    }

    fun calcNauticalDark(time: String, latitude: String, longitude: String, elevation: Double): Pair<LocalDateTime,LocalDateTime>{
        return calcDuskDawn(time,latitude,longitude,elevation,-12.0)
    }

    fun calcAstroDark(time: String, latitude: String, longitude: String, elevation: Double): Pair<LocalDateTime,LocalDateTime>{
        return calcDuskDawn(time,latitude,longitude,elevation,-18.0)
    }

}