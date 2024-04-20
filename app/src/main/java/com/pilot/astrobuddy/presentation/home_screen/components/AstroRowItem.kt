package com.pilot.astrobuddy.presentation.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject

@Composable
fun AstroRowItem(
    obj: AstroObject
){
        Column(
            modifier = Modifier.padding(3.dp).background(Color.DarkGray)
        ){
            if(!obj.CommonNames.isNullOrEmpty()){
                val commonName = obj.CommonNames
                Text(
                    text= commonName,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column (
                    modifier = Modifier.align(Alignment.CenterVertically)
                ){
                    if(!obj.NGC.isNullOrEmpty()){
                        val NGC = obj.NGC
                        Text(
                            text= "NGC $NGC",
                            style = MaterialTheme.typography.body2
                        )
                    }
                    if(!obj.IC.isNullOrEmpty()){
                        val IC = obj.IC
                        Text(
                            text= "IC $IC",
                            style = MaterialTheme.typography.body2
                        )
                    }
                    if(!obj.M.isNullOrEmpty()){
                        val M = obj.M
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
                        text= "App. Mag: "+String.format("%.2f",obj.getMagnitude()),
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text= "Size (arcmin): "+obj.getSize().first+","+obj.getSize().second,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }


}