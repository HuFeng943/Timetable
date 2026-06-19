package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Language
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SwitchButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.hufeng943.timetable.data.FirstDayOfTheWeek
import com.hufeng943.timetable.data.TimeFormat
import com.hufeng943.timetable.presentation.ui.common.AppConfig
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import kotlinx.coroutines.launch

@Composable
fun SettingPager(
    config: AppConfig,
    onLanguageSelectClick: () -> Unit,
    onTimeFormatSelectClick: () -> Unit,
    onFirstDaySelectClick: () -> Unit,
    onDynamicColorToggle: (Boolean) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // 获取当前语言显示文本
    val currentLanguageLabel = remember(config.languageTag) {
        val languageValues = context.resources.getStringArray(R.array.language_values).toList()
        val languageLabels = context.resources.getStringArray(R.array.language_labels).toList()
        val index = languageValues.indexOfFirst { raw ->
            val tag = if (raw == "@null") null else raw
            tag == config.languageTag
        }
        if (index >= 0) languageLabels[index] else languageLabels[0]
    }

    // 获取当前时间格式显示文本
    val currentTimeFormatLabel = when (config.timeFormatSetting) {
        TimeFormat.SYSTEM -> stringResource(R.string.settings_time_format_system)
        TimeFormat.H12 -> stringResource(R.string.settings_time_format_12h)
        TimeFormat.H24 -> stringResource(R.string.settings_time_format_24h)
    }

    // 获取当前每周第一天显示文本
    val currentFirstDayLabel = when (config.firstDayOfTheWeekSetting) {
        FirstDayOfTheWeek.SYSTEM -> stringResource(R.string.settings_first_day_system)
        FirstDayOfTheWeek.MONDAY -> stringResource(R.string.settings_first_day_monday)
        FirstDayOfTheWeek.SUNDAY -> stringResource(R.string.settings_first_day_sunday)
        FirstDayOfTheWeek.SATURDAY -> stringResource(R.string.settings_first_day_saturday)
    }

    ScreenScaffold(
        scrollState = scrollState, edgeButton = {
            EdgeButton(onClick = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Icon(
                    Icons.Rounded.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.back_to_top)
                )
            }
        }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null, state = scrollState, contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.more_menu_settings))
                }
            }

            // 语言设置
            item {
                SettingItemCard(
                    icon = Icons.Rounded.Language,
                    title = stringResource(R.string.settings_language),
                    value = currentLanguageLabel,
                    onClick = onLanguageSelectClick
                )
            }

            // 时间格式设置
            item {
                SettingItemCard(
                    icon = Icons.Rounded.AccessTime,
                    title = stringResource(R.string.settings_time_format),
                    value = currentTimeFormatLabel,
                    onClick = onTimeFormatSelectClick
                )
            }

            // 每周第一天设置
            item {
                SettingItemCard(
                    icon = Icons.Rounded.DateRange,
                    title = stringResource(R.string.settings_first_day),
                    value = currentFirstDayLabel,
                    onClick = onFirstDaySelectClick
                )
            }

            // 动态色彩设置
            item {
                SwitchButton(
                    checked = config.isDynamicColorEnabled,
                    onCheckedChange = onDynamicColorToggle,
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(Icons.Rounded.ColorLens, contentDescription = null)
                    },
                    label = {
                        Text(stringResource(R.string.settings_dynamic_color))
                    },
                    secondaryLabel = {
                        Text(
                            stringResource(R.string.settings_dynamic_color_summary),
                            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingItemCard(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    TitleCard(onClick = onClick, title = {
        Row {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(title)
        }
    }) {
        Text(value)
    }
}