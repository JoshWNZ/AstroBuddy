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
import kotlin.math.round

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
            locname += location.name
            if(location.admin1?.isNotEmpty() == true){locname += ", ${location.admin1}"}
            if(location.country.isNotEmpty()){locname += ", ${location.country}"}
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
                    text= "${round(location.latitude * 100000) / 100000}, ${round(location.longitude * 100000) / 100000}",
                    style = MaterialTheme.typography.body1,
                )
            }else{
                Text(
                    text= "${round(location.latitude * 100000) / 100000}, ${round(location.longitude * 100000) / 100000}",
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