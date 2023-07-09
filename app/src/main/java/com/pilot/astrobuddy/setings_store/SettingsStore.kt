package com.pilot.astrobuddy.setings_store


interface SettingsStore {
    suspend fun saveDaysToDataStore(forecastDays: Int)

    suspend fun getDaysFromDataStore(): Int

    suspend fun getUnits(): String
    suspend fun toggleUnits()
}