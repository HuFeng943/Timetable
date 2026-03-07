package com.hufeng943.timetable.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "timetable_settings")

@Singleton
class PreferenceStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val TIME_FORMAT = stringPreferencesKey("time_format")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
    }

    val is24HourFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        val setting = try {
            TimeFormat.valueOf(prefs[Keys.TIME_FORMAT] ?: TimeFormat.SYSTEM.name)
        } catch (e: Exception) {
            Log.e("PreferenceStorage", "Invalid time format setting: ${prefs[Keys.TIME_FORMAT]}")
            TimeFormat.SYSTEM
        }

        when (setting) {
            TimeFormat.SYSTEM -> android.text.format.DateFormat.is24HourFormat(context)
            TimeFormat.H12 -> false
            TimeFormat.H24 -> true
        }
    }

    suspend fun setTimeFormat(format: TimeFormat) {
        context.dataStore.edit { it[Keys.TIME_FORMAT] = format.name }
    }
}