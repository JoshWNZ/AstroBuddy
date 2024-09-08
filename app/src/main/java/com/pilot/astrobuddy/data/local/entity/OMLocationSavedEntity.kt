package com.pilot.astrobuddy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity to contain the IDs of saved locations
 */
@Entity
data class OMLocationSavedEntity(
    @PrimaryKey val id: Int,
){
    /**
     * Convert db record to int
     *
     * @return ID as Int
     */
    fun toId(): Int{
        return id
    }
}