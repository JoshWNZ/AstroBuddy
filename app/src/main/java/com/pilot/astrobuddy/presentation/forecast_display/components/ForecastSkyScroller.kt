package com.pilot.astrobuddy.presentation.forecast_display.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.R
import com.pilot.astrobuddy.domain.model.astro_forecast.Astro
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.warning.WarningSeverity
import java.time.LocalDateTime

@Composable
fun ForecastSkyScroller(
    fd: OMForecast,
    astros: List<Astro>,
    listState: LazyListState,
    scrollState: ScrollableState,
    dewThres: Triple<Int,Int,Int>,
    windThres: Triple<Int,Int,Int>,
    rainThres: Triple<Int,Int,Int>,
    dpScale: Float
) {
    //val scope = rememberCoroutineScope()

    //Colours for times of day
    val skyMidDay = Color.hsv(197f,0.43f,0.92f)
    val skyCivilDark = Color.hsv(235f,1f,0.22f)
    val skyNauticalDark = Color.hsv(235f,1f,0.13f)
    val skyAstroDark = Color.hsv(235f,1f,0f)
    val skySunRiseSet = Color.hsv(27f,0.94f,0.9f)

    //Icon painters and colours
    val frostPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.severe_cold))
    val windPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.wind))
    val rainPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.rainy))
    val cloudPainter = rememberVectorPainter(image = Icons.Rounded.Cloud)
    val minorTint = remember { ColorFilter.tint(Color.hsv(201f,0.1f,0.98f))}
    val moderateTint = remember { ColorFilter.tint(Color.hsv(34f,1f,0.93f))}
    val severeTint = remember { ColorFilter.tint(Color.hsv(3f,1f,1f))}

    LazyRow(
        state = listState,
        userScrollEnabled = false,
        modifier = Modifier
            .scrollable(
                state = scrollState,
                orientation = Orientation.Horizontal,
                flingBehavior = ScrollableDefaults.flingBehavior()
            )
            .fillMaxHeight(fraction = 0.995f)
    ){
        val hours = fd.hourly.time.size
        val days = hours / 24
        items(days){i->
            val curAstro = astros[i]
            Box(modifier = Modifier
                .width(720.dp)
                .fillMaxHeight()
                .background(color = skyAstroDark)
                //.border(width = 1.dp, color = Color.Green)
            ){
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val hourWidth = width/24

                    //day start and end time (midnight either side of day)
                    val dayStart = LocalDateTime.parse(curAstro.astroDark.first.toString().replaceAfter("T","00:00:00"))
                    val dayEnd = LocalDateTime.parse(curAstro.astroDark.first.toString().replaceAfter("T","23:59:59"))

                    //blocks for darkness's, sunrise/set, and day
                    val dayBlock = calcOffsetAndSize(curAstro.sunRiseSet.first,curAstro.sunRiseSet.second,width)

                    val sunRiseBlock = calcOffsetAndSize(curAstro.civilDark.first,curAstro.sunRiseSet.first,width)
                    val sunSetBlock = calcOffsetAndSize(curAstro.sunRiseSet.second,curAstro.civilDark.second,width)

                    val civilDarkMorningBlock = calcOffsetAndSize(curAstro.nauticalDark.first,curAstro.civilDark.first,width)
                    val civilDarkEveningBlock = calcOffsetAndSize(curAstro.civilDark.second,curAstro.nauticalDark.second,width)

                    val nautDarkMorningBlock = calcOffsetAndSize(curAstro.astroDark.first,curAstro.nauticalDark.first,width)
                    val nautDarkEveningBlock = calcOffsetAndSize(curAstro.nauticalDark.second,curAstro.astroDark.second,width)

                    val astroDarkMorningBlock = calcOffsetAndSize(dayStart,curAstro.astroDark.first,width)
                    val astroDarkEveningBlock = calcOffsetAndSize(curAstro.astroDark.second,dayEnd,width)

                    val curTime = calcOffsetAndSize(LocalDateTime.now(),LocalDateTime.now(),width)

                    //astroDarks
                    drawRect(
                        color = skyAstroDark,
                        topLeft = Offset(x=astroDarkMorningBlock.first,y=0f),
                        size = Size(width=astroDarkMorningBlock.second,height=height)
                    )
                    drawRect(
                        color = skyAstroDark,
                        topLeft = Offset(x=astroDarkEveningBlock.first,y=0f),
                        size = Size(width=astroDarkEveningBlock.second,height=height)
                    )

                    //nauticalDarks
                    drawRect(
                        color = skyNauticalDark,
                        topLeft = Offset(x=nautDarkMorningBlock.first,y=0f),
                        size = Size(width=nautDarkMorningBlock.second,height=height)
                    )
                    drawRect(
                        color = skyNauticalDark,
                        topLeft = Offset(x=nautDarkEveningBlock.first,y=0f),
                        size = Size(width=nautDarkEveningBlock.second,height=height)
                    )

                    //civilDarks
                    drawRect(
                        color = skyCivilDark,
                        topLeft = Offset(x=civilDarkMorningBlock.first,y=0f),
                        size = Size(width=civilDarkMorningBlock.second,height=height)
                    )
                    drawRect(
                        color = skyCivilDark,
                        topLeft = Offset(x=civilDarkEveningBlock.first,y=0f),
                        size = Size(width=civilDarkEveningBlock.second,height=height)
                    )

                    //sunrise/sunset
                    drawRect(
                        color = skySunRiseSet,
                        topLeft = Offset(x=sunRiseBlock.first,y=0f),
                        size = Size(width=sunRiseBlock.second,height=height)
                    )
                    drawRect(
                        color = skySunRiseSet,
                        topLeft = Offset(x=sunSetBlock.first,y=0f),
                        size = Size(width=sunSetBlock.second,height=height)
                    )

                    //daytime
                    drawRect(
                        color = skyMidDay,
                        topLeft = Offset(x=dayBlock.first,y=0f),
                        size = Size(width=dayBlock.second,height=height)
                    )

                    if(i==0){
                        //current time line
                        drawLine(
                            color = Color.Magenta.copy(alpha = 0.9f),
                            start = Offset(x=curTime.first,y=0f),
                            end = Offset(x=curTime.first,y=height),
                            strokeWidth = 8f
                        )
                    }

                    //daily weather values
                    val dewPoints = fd.hourly.dewpoint_2m.subList(i*24,(i+1)*24)
                    val temperatures = fd.hourly.temperature_2m.subList(i*24,(i+1)*24)
                    val rainProbs = fd.hourly.precipitation_probability.subList(i*24,(i+1)*24)
                    val windSpeeds = fd.hourly.windspeed_10m.subList(i*24,(i+1)*24)

                    val dewTimes: HashMap<Int,ColorFilter> = hashMapOf()
                    val windTimes: HashMap<Int,ColorFilter> = hashMapOf()
                    val rainTimes: HashMap<Int,ColorFilter> = hashMapOf()

                    val cloudThres = Triple(25.0,50.0,75.0)

                    val lowCloudCovers = fd.hourly.cloudcover_low.subList(i*24,(i+1)*24)
                    val medCloudCovers = fd.hourly.cloudcover_mid.subList(i*24,(i+1)*24)
                    val highCloudCovers = fd.hourly.cloudcover_high.subList(i*24,(i+1)*24)

                    val lowCloudTimes: HashMap<Int,WarningSeverity> = hashMapOf()
                    val medCloudTimes: HashMap<Int,WarningSeverity> = hashMapOf()
                    val highCloudTimes: HashMap<Int,WarningSeverity> = hashMapOf()

                    //check conditions for each hour
                    for(h in 0 until 24){
                        //dew/frost warning
                        val dewPoint = dewPoints[h]
                        val temperature = temperatures[h]
                        if(dewPoint >= temperature){
                            val tempDelta = dewPoint - temperature
                            val tint = when{
                                tempDelta >= dewThres.third -> {severeTint}
                                tempDelta >= dewThres.second -> {moderateTint}
                                tempDelta >= dewThres.first -> {minorTint}
                                else -> {minorTint}
                            }
                            dewTimes[h] = tint
                        }
                        //high wind warning
                        val speed = windSpeeds[h]
                        if(speed >= windThres.first){
                            val tint = when{
                                speed >= windThres.third -> {severeTint}
                                speed >= windThres.second -> {moderateTint}
                                speed >= windThres.first -> {minorTint}
                                else -> {minorTint}
                            }
                            windTimes[h] = tint
                        }
                        //rain warning
                        val prob = rainProbs[h]?:0
                        if(prob >= rainThres.first){
                            val tint = when{
                                prob >= rainThres.third -> {severeTint}
                                prob >= rainThres.second -> {moderateTint}
                                prob >= rainThres.first -> {minorTint}
                                else -> {minorTint}
                            }
                            rainTimes[h] = tint
                        }
                        //low cloud
                        val lowCover = lowCloudCovers[h]
                        if(lowCover >= cloudThres.first){
                            val severity = when{
                                lowCover >= cloudThres.third -> {WarningSeverity.HIGH}
                                lowCover >= cloudThres.second -> {WarningSeverity.MED}
                                lowCover >= cloudThres.first -> {WarningSeverity.LOW}
                                else -> {WarningSeverity.LOW}
                            }
                            lowCloudTimes[h] = severity
                        }
                        //mid cloud
                        val medCover = medCloudCovers[h]
                        if(medCover >= cloudThres.first){
                            val severity = when{
                                medCover >= cloudThres.third -> {WarningSeverity.HIGH}
                                medCover >= cloudThres.second -> {WarningSeverity.MED}
                                medCover >= cloudThres.first -> {WarningSeverity.LOW}
                                else -> {WarningSeverity.LOW}
                            }
                            medCloudTimes[h] = severity
                        }
                        //high cloud
                        val highCover = highCloudCovers[h]
                        if(highCover >= cloudThres.first){
                            val severity = when{
                                highCover >= cloudThres.third -> {WarningSeverity.HIGH}
                                highCover >= cloudThres.second -> {WarningSeverity.MED}
                                highCover >= cloudThres.first -> {WarningSeverity.LOW}
                                else -> {WarningSeverity.LOW}
                            }
                            highCloudTimes[h] = severity
                        }
                        //visibility (?)

                    }

                    //draw dew icons
                    for(h in dewTimes.keys){
                        translate(left=hourWidth*h,top=/*height*0.90f*/dpScale*27*9){
                            with(frostPainter){
                                draw(
                                    size = frostPainter.intrinsicSize,
                                    colorFilter = dewTimes[h]
                                )
                            }
                        }
                    }

                    //draw wind icons
                    for(h in windTimes.keys){
                        translate(left=hourWidth*h,top=/*height*0.80f*/dpScale*27*18.75f){
                            with(windPainter){
                                draw(
                                    size = windPainter.intrinsicSize,
                                    colorFilter = windTimes[h]
                                )
                            }
                        }
                    }

                    //draw rain icons
                    for(h in rainTimes.keys){
                        translate(left=hourWidth*h,top=/*height*0.70f*/dpScale*27*16){
                            with(rainPainter){
                                draw(
                                    size = rainPainter.intrinsicSize,
                                    colorFilter = rainTimes[h]
                                )
                            }
                        }
                    }

                    //draw low cloud
                    for(h in lowCloudTimes.keys){
                        translate(left=hourWidth*h,top=/*height*0.40f*/dpScale*27*11){
                            with(cloudPainter){
                                draw(
                                    size = cloudPainter.intrinsicSize,
                                    colorFilter = minorTint
                                )
                            }
                        }
                    }
                    //draw med cloud
                    for(h in medCloudTimes.keys){
                        translate(left=hourWidth*h,top=/*height*0.30f*/dpScale*27*8.25f){
                            with(cloudPainter){
                                draw(
                                    size = cloudPainter.intrinsicSize,
                                    colorFilter = minorTint
                                )
                            }
                        }
                    }
                    //draw high cloud
                    for(h in highCloudTimes.keys){
                        translate(left=hourWidth*h,top=/*height*0.20f*/dpScale*27*5.5f){
                            with(cloudPainter){
                                draw(
                                    size = cloudPainter.intrinsicSize,
                                    colorFilter = minorTint
                                )
                            }
                        }
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp),
                color = Color.Blue
            )
        }
    }
}

/*
Calculate and return x-offset and x-size for time period block
 */
fun calcOffsetAndSize(startTime: LocalDateTime, endTime: LocalDateTime, width: Float):Pair<Float,Float>{

    val minutesInDay = 24 * 60
    val startMinutes = (startTime.hour * 60) + startTime.minute
    val endMinutes = (endTime.hour * 60) + endTime.minute

    val startProportion = startMinutes.toFloat() / minutesInDay
    val endProportion = endMinutes.toFloat() / minutesInDay

    val startOffset = width * startProportion
    val endOffset = width * endProportion

    val size = endOffset - startOffset

    return Pair(startOffset,size)
}