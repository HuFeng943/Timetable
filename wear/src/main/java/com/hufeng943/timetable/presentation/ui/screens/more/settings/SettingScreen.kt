package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ScreenScaffold
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import kotlinx.coroutines.launch

@Composable
fun SettingScreen() {
    val scrollState = rememberScalingLazyListState()
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()

    ScreenScaffold(
        scrollState = scrollState, edgeButton = {
            EdgeButton(onClick = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.check)
                )
            }
        }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null, state = scrollState, contentPadding = contentPadding
        ) {

        }
    }
}