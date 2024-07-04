package com.pilot.astrobuddy.setings_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pilot.astrobuddy.domain.model.warning.WarningSeverity
import com.pilot.astrobuddy.domain.model.warning.WarningType
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

    override suspend fun saveThresToDataStore(warningType: WarningType, warningSeverity: WarningSeverity, value: Int){
        val key = warnKeyMap[warningTypeToString(warningType)+"_"+warningSeverityToString(warningSeverity)]?:return

        dataStore.edit{
            it[key] = value
        }
    }

    override suspend fun getThresFromDataStore(warningType: WarningType, warningSeverity: WarningSeverity): Int {
        val keyString = warningTypeToString(warningType)+"_"+warningSeverityToString(warningSeverity)

        val defaultValue = warnDefaultMap[keyString]?:0

        val key = warnKeyMap[keyString]?:return defaultValue

        val values = dataStore.data.first()
        return values[key]?:defaultValue
    }

    private fun warningTypeToString(warningType: WarningType):String{
        val type = when(warningType){
            WarningType.DEW -> "DEW"
            WarningType.RAIN -> "RAIN"
            WarningType.WIND -> "WIND"
        }
        return type
    }

    private fun warningSeverityToString(warningSeverity: WarningSeverity):String{
        val severity = when(warningSeverity){
            WarningSeverity.LOW -> "LOW"
            WarningSeverity.MED -> "MED"
            WarningSeverity.HIGH -> "HIGH"
        }
        return severity
    }

    companion object PrefKeys{
        val DAYS_KEY = intPreferencesKey("forecast_days")
        val UNITS_KEY = stringPreferencesKey("units")
        val TIMEFORMAT_KEY = stringPreferencesKey("time_format")
        const val DEFAULT_DAYS = 7
        const val DEFAULT_UNIT = "C"
        const val DEFAULT_TIMEFORMAT = "12h"

        val warnKeyMap: HashMap<String,Preferences.Key<Int>> = hashMapOf(
            Pair("DEW_LOW",intPreferencesKey("dew_thres_low")),
            Pair("DEW_MED",intPreferencesKey("dew_thres_med")),
            Pair("DEW_HIGH",intPreferencesKey("dew_thres_high")),
            Pair("WIND_LOW",intPreferencesKey("wind_thres_low")),
            Pair("WIND_MED",intPreferencesKey("wind_thres_med")),
            Pair("WIND_HIGH",intPreferencesKey("wind_thres_high")),
            Pair("RAIN_LOW",intPreferencesKey("rain_thres_low")),
            Pair("RAIN_MED",intPreferencesKey("rain_thres_med")),
            Pair("RAIN_HIGH",intPreferencesKey("rain_thres_high"))
        )
        val warnDefaultMap: HashMap<String,Int> = hashMapOf(
            Pair("DEW_LOW",1),
            Pair("DEW_MED",2),
            Pair("DEW_HIGH",3),
            Pair("WIND_LOW",5),
            Pair("WIND_MED",10),
            Pair("WIND_HIGH",15),
            Pair("RAIN_LOW",5),
            Pair("RAIN_MED",10),
            Pair("RAIN_HIGH",15)
        )
    }

}