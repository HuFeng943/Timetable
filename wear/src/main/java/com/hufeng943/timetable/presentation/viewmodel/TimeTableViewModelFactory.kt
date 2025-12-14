package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hufeng943.timetable.shared.data.repository.TimeTableRepository

class TimeTableViewModelFactory(
    private val repository: TimeTableRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeTableViewModel::class.java)) {
            return TimeTableViewModel(repository) as T
        }
        throw IllegalArgumentException("未知的 ViewModel 类: ${modelClass.name}")
    }
}