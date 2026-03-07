package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.data.PreferenceStorage
import com.hufeng943.timetable.data.TimeFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    // Flow -> StateFlow
    val is24Hour = preferenceStorage.is24HourFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        initialValue = true
    )

    val timeFormatSetting = preferenceStorage.timeFormatSettingFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        initialValue = TimeFormat.SYSTEM
    )

    fun updateFormat(newFormat: TimeFormat) {
        viewModelScope.launch { preferenceStorage.setTimeFormat(newFormat) }
    }

    val appLanguage = preferenceStorage.appLanguageFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        initialValue = "system"
    )

    val currentLocale = preferenceStorage.currentLocaleFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        initialValue = Locale.getDefault()
    )

    fun updateLanguage(locale: Locale?) {
        viewModelScope.launch {
            preferenceStorage.setLanguage(locale?.toLanguageTag() ?: "system")
        }
    }
}