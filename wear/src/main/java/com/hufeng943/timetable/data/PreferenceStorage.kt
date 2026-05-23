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
import kotlinx.datetime.DayOfWeek
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
        val FIRST_DAY_OF_THE_WEEK = stringPreferencesKey("first_day_of_the_week")
    }

    val appConfigFlow: Flow<AppConfig> = context.dataStore.data.map { prefs ->
        val langSetting = prefs[Keys.APP_LANGUAGE].let { tag -> if (tag == "system") null else tag }
        val formatSetting = runCatching {
            TimeFormat.valueOf(prefs[Keys.TIME_FORMAT] ?: TimeFormat.SYSTEM.name)
        }.getOrDefault(TimeFormat.SYSTEM)
        val firstDaySetting = runCatching {
            FirstDayOfTheWeek.valueOf(
                prefs[Keys.FIRST_DAY_OF_THE_WEEK] ?: FirstDayOfTheWeek.SYSTEM.name
            )
        }.getOrDefault(FirstDayOfTheWeek.SYSTEM)
        // 如果设置了跟随系统，则根据系统区域设置确定每周的第一天
        val effectiveFirstDay: DayOfWeek = if (firstDaySetting == FirstDayOfTheWeek.SYSTEM) {
            val locale = context.resources.configuration.locales[0]

            // 获取系统日历中每周的第一天
            val calendar = java.util.Calendar.getInstance(java.util.Locale.getDefault())
            val systemFirstDay = calendar.firstDayOfWeek

            // 常量转换
            when (systemFirstDay) {
                java.util.Calendar.MONDAY -> DayOfWeek.MONDAY
                java.util.Calendar.SUNDAY -> DayOfWeek.SUNDAY
                java.util.Calendar.SATURDAY -> DayOfWeek.SATURDAY
                else -> DayOfWeek.MONDAY
            }
        } else {
            firstDaySetting.dayOfWeek ?: DayOfWeek.MONDAY
        }

        val finalIs24Hour = when (formatSetting) {
            TimeFormat.H12 -> false
            TimeFormat.H24 -> true
            TimeFormat.SYSTEM -> DateFormat.is24HourFormat(context)
        }

        AppConfig(
            languageTag = langSetting,
            is24HourFormat = finalIs24Hour,
            timeFormatSetting = formatSetting,
            firstDayOfTheWeekSetting = firstDaySetting,
            effectiveFirstDayOfTheWeek = effectiveFirstDay
        )
    }

    suspend fun setTimeFormat(format: TimeFormat) {
        context.dataStore.edit { it[Keys.TIME_FORMAT] = format.name }
    }

    suspend fun setLanguage(languageTag: String?) {
        context.dataStore.edit { it[Keys.APP_LANGUAGE] = languageTag ?: "system" }
    }

    suspend fun setFirstDayOfTheWeek(firstDay: FirstDayOfTheWeek) {
        context.dataStore.edit { it[Keys.FIRST_DAY_OF_THE_WEEK] = firstDay.name }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PreferenceStorageEntryPoint {
    fun preferenceStorage(): PreferenceStorage
}