package com.pilot.astrobuddy.presentation.equipment_screen.components

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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun EquipmentCreateItem(
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column (
            modifier = Modifier.align(Alignment.CenterVertically)
        ){
            Text(
                text= "Create New Setup",
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            contentAlignment = Alignment.CenterEnd
        ){
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
            )
        }
    }
    Divider()
}