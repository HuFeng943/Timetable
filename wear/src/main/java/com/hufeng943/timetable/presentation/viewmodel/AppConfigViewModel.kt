package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.data.FirstDayOfTheWeek
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
        viewModelScope.launch {
            preferenceStorage.setTimeFormat(newFormat)
            _localeRecreateEvent.emit(Unit)
        }
    }

    private val _localeRecreateEvent = MutableSharedFlow<Unit>(replay = 0)
    val localeRecreateEvent = _localeRecreateEvent.asSharedFlow()

    fun updateLanguage(languageTag: String?) {
        viewModelScope.launch {
            preferenceStorage.setLanguage(languageTag)
            _localeRecreateEvent.emit(Unit)
        }
    }

    fun updateFirstDayOfTheWeek(firstDay: FirstDayOfTheWeek) {
        viewModelScope.launch {
            preferenceStorage.setFirstDayOfTheWeek(firstDay)
        }
    }
}