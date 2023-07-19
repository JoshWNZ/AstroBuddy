package com.pilot.astrobuddy.domain.use_case.calculate_sunmoon

import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.JulianFields
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt


object CalculateSunMoonUseCase {
    fun calculateSun(time: String, latitude: String, longitude: String, elevation: Double): Pair<String,String>{
        val date = LocalDate.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        Log.i("STEP","NEW DAY")

        // Jdate - get number of julian days since jan 1 2000
        val julianDay = getJulianDay(date)
        val curJulian = ceil(julianDay - 2451545.0 + 0.0008)


        // J* - calculate mean solar time
        val meanSol = curJulian - ((longitude.toDouble()*-1) / 360)

        // M - calculate solar mean anomaly
        val solMeanAnom = (357.5291 + 0.98560028 * meanSol) % 360

        //C - equation of the center
        val center = 1.9148*sin(solMeanAnom) + 0.0200*sin(2*solMeanAnom) + 0.0003*sin(3*solMeanAnom)

        //Lambda - ecliptic longitude
        val eclipLong = (solMeanAnom + center + 180 + 102.9372) % 360

        // Jtransit - solar transit
        val solarTrans = 2451545.0 + meanSol + (0.0053*sin(solMeanAnom)) - (0.0069*sin(2*eclipLong))

        // sigma - declination of the sun
        val declination = asin(sin(eclipLong) * sin((23.44*(Math.PI/180))))

        //altitude correction
        val altCorrect = (-2.076*sqrt(elevation))/60

        //hour angle
        val hourAngle = acos((sin(((-0.83+altCorrect)*(Math.PI/180)))-(sin(latitude.toDouble())*sin(declination)))/
                (cos(latitude.toDouble())*cos(declination)))*(180/Math.PI)

        Log.i("HOURANGLE",hourAngle.toString())
        Log.i("SOLARTRANS",solarTrans.toString())
        //grand finale
        val sunrise = solarTrans - hourAngle/360
        val sunset = solarTrans + hourAngle/360

        Log.i("STEP","BACK TO DATE")
        return Pair(getRealTime(sunrise), getRealTime(sunset))
    }

    /*
    Convert LocalDate into integer julian date (midday?)
    appears to be adding 1.5 days somewhere along the line
     */
    private fun getJulianDay(date: LocalDate): Double{
        //outputs integer julian day, midday on current day
        val result = date.getLong(JulianFields.JULIAN_DAY).toDouble()-0.5
        Log.i("DATE",date.toString())
        Log.i("JULIAN",result.toString())
        return result
    }
    /*
    Convert fractional julian day into readable time (buggered)
     */
    private fun getRealTime(julianDay: Double): String{
        //use int to find fractional component
        val wholeJulianDay = julianDay.toInt()
        val fractionalDay: Double = julianDay - wholeJulianDay.toDouble()
        //hour is the fraction*24 e.g. 0.5*24 = 12:00
        var hour = (fractionalDay * 24).toInt().toString()
        //minute is the hour * 60 then the remainder from 60
        var minute = (fractionalDay * 24 * 60 % 60).toInt().toString()

        if(minute.length < 2){minute = "0$minute"}
        if(hour.length < 2){ hour = "0$hour" }

        val time = "$hour:$minute"

        Log.i("JULIAN",julianDay.toString())
        Log.i("DATE",time)
        return time
    }
}