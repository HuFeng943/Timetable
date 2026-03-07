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

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    // Flow -> StateFlow
    val is24Hour = preferenceStorage.is24HourFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun updateFormat(newFormat: TimeFormat) {
        viewModelScope.launch { preferenceStorage.setTimeFormat(newFormat) }
    }
}