package com.hufeng943.timetable.data

import android.content.Context
import android.text.format.DateFormat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hufeng943.timetable.presentation.ui.common.AppConfig
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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

    val appConfigFlow: Flow<AppConfig> = context.dataStore.data.map { prefs ->
        val langSetting = prefs[Keys.APP_LANGUAGE].let { tag -> if (tag == "system") null else tag }
        val formatSetting = runCatching {
            TimeFormat.valueOf(prefs[Keys.TIME_FORMAT] ?: TimeFormat.SYSTEM.name)
        }.getOrDefault(TimeFormat.SYSTEM)


        val finalIs24Hour = when (formatSetting) {
            TimeFormat.H12 -> false
            TimeFormat.H24 -> true
            TimeFormat.SYSTEM -> DateFormat.is24HourFormat(context)
        }

        AppConfig(
            languageTag = langSetting,
            is24HourFormat = finalIs24Hour,
            timeFormatSetting = formatSetting
        )
    }

    suspend fun setTimeFormat(format: TimeFormat) {
        context.dataStore.edit { it[Keys.TIME_FORMAT] = format.name }
    }

    suspend fun setLanguage(languageTag: String?) {
        context.dataStore.edit { it[Keys.APP_LANGUAGE] = languageTag ?: "system" }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PreferenceStorageEntryPoint {
    fun preferenceStorage(): PreferenceStorage
}