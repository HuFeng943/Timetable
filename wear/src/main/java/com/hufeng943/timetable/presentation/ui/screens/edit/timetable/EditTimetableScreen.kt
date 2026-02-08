package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.DatePicker
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.screens.edit.common.DeleteConfirmScreen
import com.hufeng943.timetable.presentation.viewmodel.TableAction
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Suppress("AssignedValueIsNeverRead")
@Composable
fun EditTimetableScreen(
    timetable: Timetable? = null, onAction: (TableAction) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    val navController = LocalNavController.current
    val haptic = LocalHapticFeedback.current
    val colorList = listOf(
        0xFFE57373,
        0xFFF06292,
        0xFFBA68C8,
        0xFF9575CD,
        0xFF7986CB,
        0xFF64B5F6,
        0xFF4FC3F7,
        0xFF4DD0E1,
        0xFF4DB6AC,
        0xFF81C784,
        0xFFAED581,
        0xFFFFB74D
    ).map { Color(it) }

    val internalNavController = rememberSwipeDismissableNavController()
    val toDay = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val state = remember(timetable) { EditTimetableState(timetable, toDay) }

    SwipeDismissableNavHost(
        navController = internalNavController, startDestination = InternalNavRoutes.MAIN
    ) {
        composable(InternalNavRoutes.MAIN) {
            AppScaffold {
                ScreenScaffold(scrollState = scrollState, edgeButton = {
                    EdgeButton(
                        onClick = {
                            onAction(TableAction.Upsert(state.snapShot()))
                            navController.popBackStack()
                        }) {
                        Icon(
                            Icons.Default.Check, contentDescription = stringResource(R.string.check)
                        )
                    }
                }) {
                    ScalingLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = scrollState,
                    ) {
                        item {
                            ListHeader {
                                Text(
                                    stringResource(R.string.edit_timetable_add),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        item {// 修改名称
                            TitleCard(
                                onClick = { internalNavController.navigate(InternalNavRoutes.SEMESTER_NAME) },
                                title = {
                                    Text(
                                        stringResource(R.string.edit_timetable_name),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                            ) {
                                Text(
                                    state.semesterName, style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        item {// 开始日期
                            TitleCard(
                                onClick = { internalNavController.navigate(InternalNavRoutes.START_DATE) },
                                onLongClick = { state.updateStartDate() }, // 默认重置为今天
                                subtitle = { if (state.semesterStartDate != toDay) Text("长按设置为当前日期") },
                                title = { Text(stringResource(R.string.edit_timetable_start)) },
                            ) {
                                Text(
                                    state.semesterStartDate.toString(),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        item {// 结束日期
                            TitleCard(
                                onClick = { internalNavController.navigate(InternalNavRoutes.END_DATE) },
                                onLongClick = { state.updateEndDate() },
                                subtitle = { if (state.semesterEndDate != null) Text("长按设置为永不结束") },
                                title = { Text(stringResource(R.string.edit_timetable_end)) },
                            ) {
                                Text(
                                    state.semesterEndDate?.toString() ?: "永不结束",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        item {// 颜色
                            TitleCard(
                                onClick = { internalNavController.navigate(InternalNavRoutes.COLOR) },
                                title = { Text(stringResource(R.string.edit_timetable_color)) },
                            ) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(// 圆形效果
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(
                                            40.dp
                                        )
                                        .background(
                                            state.semesterColor, MaterialTheme.shapes.medium
                                        )
                                )
                            }
                        }

                        if (timetable != null) { // 只有编辑时才显示
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                TitleCard(onClick = {
//                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    internalNavController.navigate(InternalNavRoutes.DELETE_CONFIRM)
                                }, title = {
                                    Text(
                                        "删除此课表", color = MaterialTheme.colorScheme.error
                                    )
                                })
                            }
                        }
                    }
                }
            }
        }

        composable(InternalNavRoutes.START_DATE) {
            DatePicker(
                onDatePicked = { newDate ->
                    state.updateStartDate(newDate.toKotlinLocalDate())
                    internalNavController.popBackStack()
                },
                initialDate = state.semesterStartDate.toJavaLocalDate(),
            )
        }

        composable(InternalNavRoutes.END_DATE) {
            DatePicker(
                onDatePicked = { newDate ->
                    state.updateEndDate(newDate.toKotlinLocalDate())
                    internalNavController.popBackStack()
                },
                initialDate = (state.semesterEndDate ?: state.semesterStartDate).toJavaLocalDate()
            )
        }

        composable(InternalNavRoutes.SEMESTER_NAME) {
            val scrollState = rememberScalingLazyListState()
            // 状态管理
            var textValue by remember { mutableStateOf(state.semesterName) }

            ScreenScaffold(scrollState = scrollState, edgeButton = {
                EdgeButton(onClick = {
                    state.semesterName = textValue
                    internalNavController.popBackStack()
                }) {
                    Icon(
                        Icons.Default.Check, contentDescription = stringResource(R.string.check)
                    )
                }
            }) {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = scrollState
                ) {
                    item {
                        ListHeader {
                            Text(text = "输入课表名称")
                        }
                    }
                    item {
                        BasicTextField(
                            value = textValue,
                            onValueChange = {
                                // 过滤掉换行符
                                textValue = it.replace("\n", "")
                            },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    shape = CircleShape // 圆角两边
                                )
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }

        composable(InternalNavRoutes.COLOR) {
            val scrollStateColor = rememberScalingLazyListState()
            ScreenScaffold(scrollState = scrollStateColor) {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollStateColor,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        ListHeader { Text("选择颜色") }
                    }

                    // 颜色分组 每行 3 个
                    val rows = colorList.chunked(3)
                    items(rows.size) { rowIndex ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            rows[rowIndex].forEach { color ->
                                Box(// 圆形按钮效果
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp)
                                        .size(
                                            48.dp
                                        )
                                        .background(color, CircleShape)
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)// 模拟按钮按下震动
                                            state.semesterColor = color
                                            internalNavController.popBackStack()
                                        })
                            }
                        }
                    }
                }
            }
        }

        composable(InternalNavRoutes.DELETE_CONFIRM) {
            DeleteConfirmScreen(detail = "课表 “${state.semesterName}”", onConfirm = {
                if (timetable != null) onAction(TableAction.Delete(timetable.timetableId))
                navController.popBackStack()
            }, onCancel = {
                internalNavController.popBackStack() // 只是关掉确认页，回到编辑页
            })
        }
    }
}


object InternalNavRoutes {
    const val MAIN = "main"
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val SEMESTER_NAME = "semester_name"
    const val COLOR = "color"
    const val DELETE_CONFIRM = "delete_confirm"
}