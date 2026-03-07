package com.hufeng943.timetable.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        val IS_24_HOUR = booleanPreferencesKey("is_24_hour_override")
        val USE_24_HOUR = booleanPreferencesKey("use_24_hour")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
    }

    // 默认值
    val use24HourFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.IS_24_HOUR]?.let { override ->
            if (override) prefs[Keys.USE_24_HOUR] ?: true else null
        } ?: android.text.format.DateFormat.is24HourFormat(context)
    }

    suspend fun set24HourOverride(use24Hour: Boolean) {
        context.dataStore.edit { it[Keys.IS_24_HOUR] = true; it[Keys.USE_24_HOUR] = use24Hour }
    }
}