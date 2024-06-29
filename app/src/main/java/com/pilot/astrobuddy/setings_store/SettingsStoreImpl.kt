package com.pilot.astrobuddy.setings_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject


val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsStoreImpl @Inject constructor(
    @ApplicationContext context: Context
): SettingsStore {

    private val dataStore: DataStore<Preferences> = context.settingsDataStore

    override suspend fun saveDaysToDataStore(forecastDays: Int) {
        dataStore.edit {
            it[DAYS_KEY] = forecastDays
        }
    }

    override suspend fun getDaysFromDataStore(): Int {
        val values = dataStore.data.first()
        return values[DAYS_KEY]?: DEFAULT_DAYS
    }

    override suspend fun toggleUnits() {
        if(getUnitsFromDataStore() == "C"){
            dataStore.edit {
                it[UNITS_KEY] = "F"
            }
        }else{
            dataStore.edit {
                it[UNITS_KEY] = "C"
            }
        }
    }

    override suspend fun getUnitsFromDataStore(): String{
        val values = dataStore.data.first()
        return values[UNITS_KEY]?: DEFAULT_UNIT
    }

    override suspend fun toggleTimeFormat() {
        if(getTimeFormatFromDataStore() == "12h"){
            dataStore.edit{
                it[TIMEFORMAT_KEY] = "24h"
            }
        }else{
            dataStore.edit{
                it[TIMEFORMAT_KEY] = "12h"
            }
        }
    }

    override suspend fun getTimeFormatFromDataStore(): String {
        val values = dataStore.data.first()
        return values[TIMEFORMAT_KEY]?: DEFAULT_TIMEFORMAT
    }

    companion object PrefKeys{
        val DAYS_KEY = intPreferencesKey("forecast_days")
        val UNITS_KEY = stringPreferencesKey("units")
        val TIMEFORMAT_KEY = stringPreferencesKey("time_format")
        const val DEFAULT_DAYS = 7
        const val DEFAULT_UNIT = "C"
        const val DEFAULT_TIMEFORMAT = "12h"
    }

}