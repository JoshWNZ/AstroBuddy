package com.pilot.astrobuddy.domain.use_case.calculate_astro

import com.pilot.astrobuddy.domain.use_case.calculate_astro.TimeConversion.convertToLocalTZ
import com.pilot.astrobuddy.domain.use_case.calculate_astro.TimeConversion.convertToUTC
import io.github.cosinekitty.astronomy.Aberration
import io.github.cosinekitty.astronomy.Body
import io.github.cosinekitty.astronomy.Direction
import io.github.cosinekitty.astronomy.EquatorEpoch
import io.github.cosinekitty.astronomy.Observer
import io.github.cosinekitty.astronomy.Time
import io.github.cosinekitty.astronomy.defineStar
import io.github.cosinekitty.astronomy.equator
import io.github.cosinekitty.astronomy.searchRiseSet
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CalculateDsoUseCase {

    /*
     * Define and return a custom body based on given ra/dec
     */
    fun getCustomBody(ra: String, dec: String): Body{

        val raSplit = ra.split(':').map{e->e.toDouble()}

        val raDecimal = raSplit[0] + (raSplit[1] / 60) + (raSplit[2] / 3600)

        val raSidereal = raDecimal / 15

        val decSplit = dec.split(':').map{e->e.toDouble()}

        val decDecimal = decSplit[0] + (decSplit[1] / 60) + (decSplit[2] / 3600)

        defineStar(body = Body.Star1, ra=raSidereal, dec= decDecimal, distanceLightYears = 1000.0)

        return Body.Star1
    }

    fun calcObjRiseSet(time: String, latitude: String, longitude: String, elevation: Double, body: Body): Pair<LocalDateTime,LocalDateTime> {
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

        val rise = searchRiseSet(body, obs, Direction.Rise, astTime, 1.0, elevation)
        val set = searchRiseSet(body, obs, Direction.Set, astTime, 1.0, elevation)

        var riseUserDateTime: LocalDateTime = LocalDateTime.MIN
        var setUserDateTime: LocalDateTime = LocalDateTime.MIN

        if(rise != null){
            val sunRiseDateTime = rise.toDateTime()
            val sunRiseLocalDateTime = LocalDateTime.parse(sunRiseDateTime.toString().dropLast(5), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            riseUserDateTime = convertToLocalTZ(sunRiseLocalDateTime).toLocalDateTime()
        }
        if(set != null){
            val sunSetDateTime = set.toDateTime()
            val sunSetLocalDateTime = LocalDateTime.parse(sunSetDateTime.toString().dropLast(5), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            setUserDateTime = convertToLocalTZ(sunSetLocalDateTime).toLocalDateTime()
        }

        return Pair(riseUserDateTime,setUserDateTime)
    }

    fun calcObjPosition(time: String, latitude: String, longitude: String, elevation: Double, body: Body): Pair<String,String> {

        val df = DecimalFormat("#.##")

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

        val equatorial = equator(
            body = body,
            time = astTime,
            observer = obs,
            equdate = EquatorEpoch.OfDate,
            aberration = Aberration.Corrected
        )

        val raSidereal = equatorial.ra

        val raDecimal = raSidereal * 15

        val raDeg = raDecimal.toInt()
        val raMin = (raDecimal - raDeg) * 60
        val raSec = (raMin - raMin.toInt()) * 60

        val raString = "${raDeg}:${raMin.toInt().toString().trimStart('-')}:${df.format(raSec).trimStart('-')}"

        val decDecimal = equatorial.dec

        val decDeg = decDecimal.toInt()
        val decMin = (decDecimal - decDeg) * 60
        val decSec = (decMin - decMin.toInt()) * 60

        val decString = "${decDeg}:${decMin.toInt().toString().trimStart('-')}:${df.format(decSec).trimStart('-')}"

        return Pair(raString,decString)
    }
}