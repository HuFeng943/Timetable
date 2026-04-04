package com.hufeng943.timetable.presentation.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.data.PreferenceStorage
import com.hufeng943.timetable.data.TimeFormat
import com.hufeng943.timetable.presentation.ui.common.AppConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    private val _localeRefreshEvent = MutableSharedFlow<String>(replay = 0)
    val localeRefreshEvent = _localeRefreshEvent.asSharedFlow()

    fun updateLanguage(languageTag: String?) {
        viewModelScope.launch {
            preferenceStorage.setLanguage(languageTag)
            // 发送通知
            _localeRefreshEvent.emit(
                languageTag ?: Resources.getSystem().configuration.locales[0].toLanguageTag()
            )
        }
    }
}