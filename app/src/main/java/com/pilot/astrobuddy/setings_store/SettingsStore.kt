package com.pilot.astrobuddy.setings_store


interface SettingsStore {
    suspend fun saveDaysToDataStore(forecastDays: Int)
    suspend fun getDaysFromDataStore(): Int
    suspend fun getUnitsFromDataStore(): String
    suspend fun toggleUnits()
    suspend fun toggleTimeFormat()
    suspend fun getTimeFormatFromDataStore(): String
}