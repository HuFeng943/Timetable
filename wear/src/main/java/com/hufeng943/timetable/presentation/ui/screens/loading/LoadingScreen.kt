package com.hufeng943.timetable.presentation.ui.screens.loading

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.shared.model.Timetable

@Composable
fun LoadingScreen(timetables: List<Timetable>? = null, onLoaded: () -> Unit) {
    Log.v("navController2", (timetables == null).toString())
    LaunchedEffect(timetables) {
        if (timetables != null) onLoaded()
    }
    ScreenScaffold(timeText = {}) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("课程加载中…")
        }
    }
}