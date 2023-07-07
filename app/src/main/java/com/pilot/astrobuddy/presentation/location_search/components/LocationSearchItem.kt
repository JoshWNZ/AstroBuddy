package com.pilot.astrobuddy.presentation.location_search.components

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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation

@Composable
fun LocationSearchItem(
    location: OMLocation,
    onItemClick: (OMLocation) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(location) }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        var locname = ""
        //if a real location and not a generated blank one (for raw coord entry)
        if(location.name.isNotEmpty()) {
            locname = "${location.name}, ${location.admin1}, ${location.country}"
        }

        Column (
            modifier = Modifier.align(CenterVertically)
        ){
            if(locname.isNotEmpty()){
                Text(
                    text= locname,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text= "${location.latitude},${location.longitude}",
                    style = MaterialTheme.typography.body1,
                )
            }else{
                Text(
                    text= "${location.latitude},${location.longitude}",
                    style = MaterialTheme.typography.body2,
                )
            }

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