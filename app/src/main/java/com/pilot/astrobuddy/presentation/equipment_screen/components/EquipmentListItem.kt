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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment

@Composable
fun EquipmentListItem(
    equipment: AstroEquipment,
    onItemClick: (Int) -> Unit,
    editing: Boolean,
    delAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if(editing) {
                    delAction()
                }else{
                    onItemClick(equipment.id)
                }
            }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column (
            modifier = Modifier.align(Alignment.CenterVertically)
        ){
            Text(
                text= equipment.setupName,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            contentAlignment = Alignment.CenterEnd
        ){
            if(editing){
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
            }else{
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null,
                )
            }
        }

    }
    Divider()
}