package com.pilot.astrobuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pilot.astrobuddy.data.local.entity.AstroObjectEntity
import com.pilot.astrobuddy.data.local.entity.OMLocationEntity
import com.pilot.astrobuddy.data.local.entity.OMLocationSavedEntity

@Database(
    entities = [OMLocationEntity::class, OMLocationSavedEntity::class, AstroObjectEntity::class],
    exportSchema = false,
    version = 6
)
abstract class AstroBuddyDatabase: RoomDatabase() {
    abstract val locDao: OMLocationDao
    abstract val saveObjDao: SavedObjectDao
}