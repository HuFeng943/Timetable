package com.hufeng943.timetable.data

import android.content.Context
import android.text.format.DateFormat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hufeng943.timetable.presentation.theme.AppConfig
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

    val appConfigFlow: Flow<AppConfig> = context.dataStore.data.map { prefs ->
        val langSetting = prefs[Keys.APP_LANGUAGE] ?: "system" // 默认跟随系统
        val formatSetting = runCatching {
            TimeFormat.valueOf(prefs[Keys.TIME_FORMAT] ?: TimeFormat.SYSTEM.name)
        }.getOrDefault(TimeFormat.SYSTEM)

        AppConfig(
            is24Hour = when (formatSetting) {
                TimeFormat.SYSTEM -> DateFormat.is24HourFormat(context)
                TimeFormat.H12 -> false
                TimeFormat.H24 -> true
            },
            timeFormatSetting = formatSetting,
            locale = if (langSetting == "system" || langSetting.isBlank()) {
                context.resources.configuration.locales[0] ?: Locale.getDefault()
            } else {
                runCatching { Locale.forLanguageTag(langSetting) }.getOrDefault(Locale.getDefault())
            },
            appLanguage = langSetting
        )
    }

    suspend fun setTimeFormat(format: TimeFormat) {
        context.dataStore.edit { it[Keys.TIME_FORMAT] = format.name }
    }

    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { it[Keys.APP_LANGUAGE] = languageCode }
    }
}