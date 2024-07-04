package com.pilot.astrobuddy.setings_store

import com.pilot.astrobuddy.domain.model.warning.WarningSeverity
import com.pilot.astrobuddy.domain.model.warning.WarningType


interface SettingsStore {
    suspend fun saveDaysToDataStore(forecastDays: Int)
    suspend fun getDaysFromDataStore(): Int
    suspend fun getUnitsFromDataStore(): String
    suspend fun toggleUnits()
    suspend fun toggleTimeFormat()
    suspend fun getTimeFormatFromDataStore(): String
    suspend fun saveThresToDataStore(warningType: WarningType, warningSeverity: WarningSeverity, value: Int)
    suspend fun getThresFromDataStore(warningType: WarningType, warningSeverity: WarningSeverity): Int
}
