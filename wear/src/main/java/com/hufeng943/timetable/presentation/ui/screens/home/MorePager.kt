package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.NavRoutes
import com.hufeng943.timetable.presentation.ui.common.LocalAppConfig
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.viewmodel.AppConfigViewModel

@Composable
fun MorePager(
    appConfigViewModel: AppConfigViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    val scrollState = rememberScalingLazyListState()
    val navController = LocalNavController.current
    val config = LocalAppConfig.current

    ScreenScaffold(scrollState = scrollState) { contentPadding ->
        ScalingLazyColumn(
            state = scrollState, modifier = Modifier.fillMaxSize(), contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.more_title))
                }
            }

            item {// 临时测试用
                // 获取当前语言标签（用于逻辑判断）
                val currentLangTag = config.languageTag  // null 表示跟随系统

                FilledTonalButton(
                    onClick = {
                        // 根据当前 languageTag 决定下一个 languageTag
                        val nextTag = when {
                            currentLangTag == null -> "zh-CN"           // 跟随系统 → 简体中文
                            currentLangTag.startsWith("zh") -> "en"      // 中文 → 英文
                            currentLangTag.startsWith("en") -> null      // 英文 → 跟随系统
                            else -> "zh-CN"                              // 其他 → 简体中文
                        }
                        // ViewModel 的 updateLanguage 应接受 String? 类型
                        appConfigViewModel.updateLanguage(nextTag)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        val labelText = when {
                            currentLangTag == null -> "切换到中文"
                            currentLangTag.startsWith("zh") -> "Switch to English"
                            currentLangTag.startsWith("en") -> "使用系统语言"
                            else -> "重置语言"
                        }
                        Text(labelText)
                    },
                    secondaryLabel = {
                        val statusText = when {
                            currentLangTag == null -> "跟随系统"
                            currentLangTag.startsWith("zh") -> "简体中文"
                            currentLangTag.startsWith("en") -> "English"
                            else -> currentLangTag
                        }
                        Text("当前: $statusText")
                    }
                )
            }

            // 1. 编辑课表
            item {
                FilledTonalButton(
                    onClick = { navController.navigateSingle(NavRoutes.LIST_TIMETABLE) },
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    },
                    label = {
                        Text(stringResource(R.string.more_menu_edit))
                    })
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
                    })
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
                    })
            }
        }
    }
}
