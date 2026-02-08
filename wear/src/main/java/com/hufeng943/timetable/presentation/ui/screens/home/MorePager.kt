package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.NavRoutes

@Composable
fun MorePager() {
    val scrollState = rememberScalingLazyListState()
    val navController = LocalNavController.current

    ScreenScaffold(scrollState = scrollState) {
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.more_title))
                }
            }

            // 1. 编辑课表
            item {
                FilledTonalButton(
                    onClick = { navController.navigate(NavRoutes.LIST_TIMETABLE) },
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    },
                    label = {
                        Text(stringResource(R.string.more_menu_edit))
                    }
                )
            }

            // 2. 设置
            item {
                FilledTonalButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    },
                    label = {
                        Text(stringResource(R.string.more_menu_settings))
                    }
                )
            }

            // 3. 关于
            item {
                FilledTonalButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null)
                    },
                    label = {
                        Text(stringResource(R.string.more_menu_about))
                    }
                )
            }
        }
    }
}
