package com.pilot.astrobuddy.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(val context: Context) {
    companion object{
        val FORECAST_DAYS = intPreferencesKey("forecast_days")
        val UNITS = stringPreferencesKey("units")
    }

    suspend fun saveDaysToDataStore(forecastDays: Int) {
        context.dataStore.edit {
            it[FORECAST_DAYS] = forecastDays
        }
    }

    suspend fun getDaysFromDataStore(): Int? {
        val values = context.dataStore.data.first()
        return values[FORECAST_DAYS]
    }
}