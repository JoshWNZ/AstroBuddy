package com.pilot.astrobuddy.presentation.object_search.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject

@Composable
fun ObjSearchItem(
    astroObject: AstroObject,
    onItemClick: (AstroObject) -> Unit,
    recommended: Boolean
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(astroObject) }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        var commonName = ""
        var NGC = ""
        var IC = ""
        var M = ""

        Log.i("OBJECT", "Object: NGC:$NGC IC:$IC M:$M")


        Column (
            modifier = Modifier.align(Alignment.CenterVertically)
        ){

            if(!astroObject.CommonNames.isNullOrEmpty()){
                commonName = astroObject.CommonNames
                Text(
                    text= commonName,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if(!astroObject.NGC.isNullOrEmpty()){
                NGC = astroObject.NGC
                Text(
                    text= "NGC $NGC",
                    style = MaterialTheme.typography.body2
                )
            }
            if(!astroObject.IC.isNullOrEmpty()){
                IC = astroObject.IC
                Text(
                    text= "IC $IC",
                    style = MaterialTheme.typography.body2
                )
            }
            if(!astroObject.M.isNullOrEmpty()){
                M = astroObject.M
                Text(
                    text= "M $M",
                    style = MaterialTheme.typography.body2
                )
            }
        }

        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ){
            Text(
                text= "App. Mag: "+String.format("%.2f",astroObject.getMagnitude()),
                style = MaterialTheme.typography.body2
            )
            Text(
                text= "Size (arcmin): "+astroObject.getSize().first+","+astroObject.getSize().second,
                style = MaterialTheme.typography.body2
            )
        }

        Box(
            contentAlignment = Alignment.CenterEnd
        ){
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = null,
            )
        }

    }
    Divider()
}