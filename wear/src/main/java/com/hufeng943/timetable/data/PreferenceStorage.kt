package com.hufeng943.timetable.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
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
        val setting = runCatching {
            TimeFormat.valueOf(prefs[Keys.TIME_FORMAT] ?: TimeFormat.SYSTEM.name)
        }.getOrDefault(TimeFormat.SYSTEM)

        when (setting) {
            TimeFormat.SYSTEM -> android.text.format.DateFormat.is24HourFormat(context)
            TimeFormat.H12 -> false
            TimeFormat.H24 -> true
        }
    }

    val timeFormatSettingFlow: Flow<TimeFormat> = context.dataStore.data.map { prefs ->
        runCatching {
            TimeFormat.valueOf(prefs[Keys.TIME_FORMAT] ?: TimeFormat.SYSTEM.name)
        }.getOrDefault(TimeFormat.SYSTEM)
    }

    suspend fun setTimeFormat(format: TimeFormat) {
        context.dataStore.edit { it[Keys.TIME_FORMAT] = format.name }
    }

    val appLanguageFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.APP_LANGUAGE] ?: "system" // 默认跟随系统
    }

    val currentLocaleFlow: Flow<Locale> = appLanguageFlow.map { lang ->
        if (lang == "system" || lang.isBlank()) {
            context.resources.configuration.locales[0] ?: Locale.getDefault()
        } else {
            runCatching { Locale.forLanguageTag(lang) }.getOrDefault(Locale.getDefault())
        }
    }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { it[Keys.APP_LANGUAGE] = languageCode }
    }
}