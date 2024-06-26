package com.pilot.astrobuddy.presentation.object_display.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.pilot.astrobuddy.presentation.object_display.imageScale

@Composable
fun CoordImageFromHiPS(
    fov: String,
    ra: String,
    dec: String,
){
    val url = "https://alasky.cds.unistra.fr/hips-image-services/hips2fits?" +
            "hips=CDS%2FP%2FDSS2%2Fcolor" +
            "&width=720&height=720" +
            "&fov=$fov" +
            "&projection=TAN" +
            "&coordsys=icrs" +
            "&rotation_angle=0.0" +
            "&ra=$ra" +
            "&dec=$dec" +
            "&format=jpg"

    Box(modifier = Modifier.size(imageScale.dp), contentAlignment = Alignment.Center){
        SubcomposeAsyncImage(
            model = url,
            contentDescription = "image",
            modifier = Modifier.size(imageScale.dp),
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.High,
            loading = {
                Box(
                    modifier = Modifier.size((imageScale /1.5).dp),
                    contentAlignment = Alignment.Center)
                {
                    CircularProgressIndicator()
                }
            }
        )
    }
}