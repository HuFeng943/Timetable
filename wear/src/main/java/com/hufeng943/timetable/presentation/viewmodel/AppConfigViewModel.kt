package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.data.PreferenceStorage
import com.hufeng943.timetable.data.TimeFormat
import com.hufeng943.timetable.presentation.theme.AppConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

@HiltViewModel
class AppConfigViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    // Flow -> StateFlow
    val appConfig = preferenceStorage.appConfigFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        initialValue = AppConfig()
    )

    fun updateFormat(newFormat: TimeFormat) {
        viewModelScope.launch { preferenceStorage.setTimeFormat(newFormat) }
    }


    fun updateLanguage(locale: Locale?) {
        viewModelScope.launch {
            preferenceStorage.setLanguage(locale?.toLanguageTag() ?: "system")
        }
    }
}