package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.ScreenScaffold

@Composable
fun LoadingScreen() {
    ScreenScaffold(timeText = {}) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(0.25f) // TODO
            )
        }
    }
}