package com.pilot.astrobuddy.presentation.object_search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
            .padding(16.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            modifier = Modifier.weight(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                if (!astroObject.CommonNames.isNullOrEmpty()) {
                    var commonName = astroObject.CommonNames.replace(",", ", ")
                    if (commonName.length > 22) {
                        commonName = commonName.substring(0, 22) + "..."
                    }
                    Text(
                        text = commonName,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!astroObject.NGC.isNullOrEmpty()) {
                    val NGC = astroObject.NGC.removePrefix("0")
                    Text(
                        text = "NGC $NGC",
                        style = MaterialTheme.typography.body2
                    )
                }
                if (!astroObject.IC.isNullOrEmpty()) {
                    val IC = astroObject.IC.removePrefix("0")
                    Text(
                        text = "IC $IC",
                        style = MaterialTheme.typography.body2
                    )
                }
                if (!astroObject.M.isNullOrEmpty()) {
                    val M = astroObject.M.removePrefix("0")
                    Text(
                        text = "M $M",
                        style = MaterialTheme.typography.body2
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "App. Mag: " + String.format("%.2f", astroObject.getMagnitude()).replace("nu","NA"),
                    style = MaterialTheme.typography.body2
                )

                val size = astroObject.getSize()
                val sizeString = if(size.second.isEmpty()){
                    size.first + "\'"
                }else{
                    size.first + "\', " + size.second + "\'"
                }
                Text(
                    text = "Size: $sizeString",
                    style = MaterialTheme.typography.body2
                )
            }
        }

        Box(
            modifier = Modifier.weight(0.1f),
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