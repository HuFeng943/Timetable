package com.hufeng943.timetable.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<UiState<T>>.toSafeStateFlow(
    scope: CoroutineScope
): StateFlow<UiState<T>> = this.catch { e ->
    emit(UiState.Error(e))
}.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
    initialValue = UiState.Loading
)