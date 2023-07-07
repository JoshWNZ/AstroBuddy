package com.pilot.astrobuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
Databse entity to contain the IDs of saved locations
 */
@Entity
data class OMLocationSavedEntity(
    @PrimaryKey val id: Int,
){
    fun toId(): Int{
        return id
    }
}