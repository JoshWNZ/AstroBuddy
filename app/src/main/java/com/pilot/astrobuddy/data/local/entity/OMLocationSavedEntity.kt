package com.pilot.astrobuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OMLocationSavedEntity(
    @PrimaryKey val id: Int,
){
    fun toId(): Int{
        return id
    }
}