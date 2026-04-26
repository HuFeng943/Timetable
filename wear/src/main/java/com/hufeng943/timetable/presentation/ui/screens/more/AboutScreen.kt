package com.hufeng943.timetable.presentation.ui.screens.more

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ScreenScaffold

@Composable
fun AboutScreen() {
    val scrollState = rememberScalingLazyListState()
    val context = LocalContext.current
    val versionName = remember {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }
    ScreenScaffold(scrollState = scrollState) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null,
            state = scrollState,
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            versionName//TODO
        }
    }
}