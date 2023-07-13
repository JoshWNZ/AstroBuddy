package com.pilot.astrobuddy.domain.use_case.calculate_sunmoon

import java.lang.Math.toRadians
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


object CalculateSunMoonUseCase {
    fun calculateSun(time: String, latitude: String, longitude: String, elevation: Double): Pair<String,String>{
        val date = LocalDate.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        //get number of julian days since jan 1 2000
        val julianDay = getJulianDay(date)
        val curJulian = ceil(julianDay - 2451545 + 0.0008)

        //calculate mean solar time
        val meanSol = curJulian - (longitude.toDouble() / 360)

        //calculate solar mean anomaly
        val solMeanAnom = (357.5291 + 0.98560028 * meanSol) % 360

        //equation of the center
        val center = 1.9148*sin(solMeanAnom) + 0.0200*sin(2*solMeanAnom) + 0.0003*sin(3*solMeanAnom)

        //ecliptic longitude
        val eclipLong = (solMeanAnom + center + 180 + 102.9372) % 360

        //solar transit
        val solarTrans = 2451545.0 + meanSol + 0.0053*sin(solMeanAnom) - 0.0069*sin(2*eclipLong)

        //declination of the sun
        val declination = asin(sin(eclipLong) * sin(toRadians(23.44)))

        //altitude correction
        val altCorrect = -2.076*sqrt(elevation)/60

        //hour angle
        val hourAngle = acos((sin(toRadians(-0.83+altCorrect))-sin(latitude.toDouble())*sin(declination))/
                (cos(latitude.toDouble())*cos(declination)))

        //grand finale
        val sunrise = solarTrans - hourAngle/360

        val sunset = solarTrans + hourAngle/360

        return Pair(getRealTime(sunrise), getRealTime(sunset))
    }


    private fun getJulianDay(date: LocalDate): Int{
        var year = date.getYear()
        var month = date.getMonthValue()
        var day = date.getDayOfMonth()

        if(month <= 2){
            year--
            month +=12
        }

        val a = year / 100
        val b = a / 4
        val c = 2 - a + b
        val e = (365.25 * (year + 4716)).toInt()
        val f = (30.6001 * (month + 1)).toInt()

        return c + day + e + f - 1524
    }
    private fun getRealTime(julianDay: Double): String{
        val wholeJulianDay = julianDay.toInt()
        val fractionalDay: Double = julianDay - wholeJulianDay.toDouble()
        val hour = (fractionalDay * 24).toInt()
        val minute = (fractionalDay * 24 * 60 % 60).toInt()

        return "$hour:$minute"
    }
}