package com.pilot.astrobuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity

/**
 * Room Database to store pre-defined astronomical objects
 */
@Database(
    entities = [AstroObjectEntity::class],
    exportSchema = false,
    version = 1
)
abstract class AstroObjectDatabase: RoomDatabase() {
    abstract val objDao: AstroObjectDao
}