package com.pilot.astrobuddy.domain.use_case.calculate_lightpollution

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.pilot.astrobuddy.R
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateLightPollUseCase @Inject constructor(
    context: Context
){
    private val colorMap: Map<Color,Pair<Double,Double>> = mapOf(
        Pair(Color.hsv(0f,0f,0f),Pair(22.00,21.99)),
        Pair(Color.hsv(0f,0f,0.14f),Pair(21.99,21.93)),
        Pair(Color.hsv(0f,0f,0.27f),Pair(21.93,21.89)),
        Pair(Color.hsv(224f,0.99f,0.52f),Pair(21.89,21.81)),
        Pair(Color.hsv(223f,1f,0.86f),Pair(21.81,21.69)),
        Pair(Color.hsv(103f,0.75f,0.42f),Pair(21.69,21.51)),
        Pair(Color.hsv(103f,0.74f,0.69f),Pair(21.51,21.25)),
        Pair(Color.hsv(58f,0.68f,0.56f),Pair(21.25,20.91)),
        Pair(Color.hsv(57f,0.68f,0.81f),Pair(20.91,20.49)),
        Pair(Color.hsv(24f,0.76f,0.64f),Pair(20.49,20.02)),
        Pair(Color.hsv(24f,0.75f,0.85f),Pair(20.02,19.50)),
        Pair(Color.hsv(1f,0.79f,0.60f),Pair(19.50,18.95)),
        Pair(Color.hsv(2f,0.78f,0.93f),Pair(18.95,18.38)),
        Pair(Color.hsv(0f,0f,0.75f),Pair(18.38,17.80)),
        Pair(Color.hsv(0f,0f,1f),Pair(17.80,Double.MIN_VALUE))
    )

    private val image: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.world2022)

    init{
        Log.i("LIGHTPOLL","init")
    }

    /*
      Calculate the light pollution range in magnitude per square metre (sqm) from lat/long
     */
    fun calcLightPol(lat: Double, long: Double): Pair<Double,Double>{
        val pixelCoord = convertLatLongToPixelCoords(lat,long)
        Log.i("IMAGENULLsqm",(image==null).toString())
        //get pixel and extract hsv colour values
        val pixelVal = image?.getPixel(pixelCoord.first,pixelCoord.second)?:0
        val red = android.graphics.Color.red(pixelVal)
        val green = android.graphics.Color.green(pixelVal)
        val blue = android.graphics.Color.blue(pixelVal)
        val hsv = FloatArray(3)
        android.graphics.Color.RGBToHSV(red, green, blue, hsv)

        //construct colour object
        val colour = Color.hsv(hsv[0],hsv[1],hsv[2])

        //get sqm range
        val range = colorMap[colour]?:Pair(-1.0,-1.0)

        Log.i("USECASE",range.toString())
        return range
    }

    fun calcBortleFromSQM(sqm: Double): Int {
        return when{
            sqm < 18.0 -> 8
            else -> -1
        }
    }

    private fun convertLatLongToPixelCoords(lat: Double, long: Double): Pair<Int,Int>{
        //65S to 75N, 180W to 180E
        Log.i("IMAGENULLcoord",(image==null).toString())
        val width = image?.width?:0
        val height = image?.height?:0

        var limitLat = lat
        if(lat > 75.0){limitLat = 75.0}
        if(lat < -65.0){limitLat = -65.0}

        val normLat = (limitLat + 90) / 180
        val normLong = (long + 180) / 360

        val pixelX = (normLong * width).roundToInt()
        val pixelY = (normLat * height).roundToInt()

        Log.i("Coords",Pair(pixelX,pixelY).toString())
        return Pair(pixelX,pixelY)
    }
}