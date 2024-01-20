package com.pilot.astrobuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity

@Database(
    entities = [AstroObjectEntity::class],
    exportSchema = false,
    version = 1
)
abstract class AstroObjectDatabase: RoomDatabase() {
    abstract val objDao: AstroObjectDao
}