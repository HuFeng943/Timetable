package com.hufeng943.timetable.data

import android.content.Context
import android.text.format.DateFormat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hufeng943.timetable.presentation.ui.common.AppConfig
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

        val finalLocale = if (langSetting == "system") {
            context.resources.configuration.locales[0] ?: Locale.getDefault()
        } else {
            Locale.forLanguageTag(langSetting)
        }

        val finalIs24Hour = when (formatSetting) {
            TimeFormat.H12 -> false
            TimeFormat.H24 -> true
            TimeFormat.SYSTEM -> DateFormat.is24HourFormat(context)
        }

        AppConfig(
            useSystemLanguage = langSetting == "system",
            locale = finalLocale,
            is24HourFormat = finalIs24Hour,
            timeFormatSetting = formatSetting
        )
    }

    suspend fun setTimeFormat(format: TimeFormat) {
        context.dataStore.edit { it[Keys.TIME_FORMAT] = format.name }
    }

    suspend fun setLanguage(locale: Locale?) {
        val langTag = locale?.toLanguageTag() ?: "system"
        context.dataStore.edit { it[Keys.APP_LANGUAGE] = langTag }
    }
}