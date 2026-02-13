package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
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
import com.hufeng943.timetable.presentation.ui.components.ColorPickerCard
import com.hufeng943.timetable.presentation.ui.components.DeleteButton
import com.hufeng943.timetable.presentation.ui.screens.common.ColorSelectionScreen
import com.hufeng943.timetable.presentation.ui.screens.common.DeleteConfirmScreen
import com.hufeng943.timetable.presentation.ui.screens.common.ErrorScreen
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.ui.screens.common.NameEditScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.InternalNavRoutes
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.edit.timetable.EditTimetableAction
import com.hufeng943.timetable.presentation.viewmodel.edit.timetable.EditTimetableViewModel
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun EditTimetableScreen(
    viewModel: EditTimetableViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val internalNavController = rememberSwipeDismissableNavController()

    @Composable
    fun WithSuccessContent(content: @Composable (timetable: Timetable) -> Unit) {
        when (val state = uiState) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Error -> ErrorScreen(state.throwable)
            is UiState.Empty -> ErrorScreen(AppError.UnexpectedEmpty())
            is UiState.Success -> content(state.data)
        }
    }

    SwipeDismissableNavHost(
        navController = internalNavController, startDestination = InternalNavRoutes.MAIN
    ) {
        composable(InternalNavRoutes.MAIN) {
            val scrollState = rememberScalingLazyListState()
            WithSuccessContent { timetable ->
                val toDay = Clock.System.todayIn(TimeZone.currentSystemDefault())

                ScreenScaffold(scrollState = scrollState, edgeButton = {
                    EdgeButton(
                        onClick = {
                            viewModel.onAction(EditTimetableAction.Upsert)
                            navController.popBackStack()
                        }) {
                        Icon(
                            Icons.Default.Check, contentDescription = stringResource(R.string.check)
                        )
                    }
                }) { contentPadding ->
                    ScalingLazyColumn(
                        state = scrollState, contentPadding = contentPadding
                    ) {
                        item {
                            ListHeader {
                                Text(
                                    if (timetable.timetableId == 0L) stringResource(R.string.edit_timetable_add)
                                    else "编辑课表"
                                )
                            }
                        }

                        item {// 修改名称
                            TitleCard(
                                onClick = { internalNavController.navigate(InternalNavRoutes.NAME) },
                                title = {
                                    Text(
                                        stringResource(R.string.edit_timetable_name),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                            ) {
                                Text(
                                    timetable.semesterName,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        item {// 开始日期
                            TitleCard(
                                onClick = { internalNavController.navigate(InternalNavRoutes.START_DATE) },
                                onLongClick = {
                                    viewModel.onAction(
                                        EditTimetableAction.UpdateStartDate(toDay)
                                    )
                                }, // 默认重置为今天
                                subtitle = { if (timetable.semesterStart != toDay) Text("长按设置为当前日期") },
                                title = { Text(stringResource(R.string.edit_timetable_start)) },
                            ) {
                                Text(
                                    timetable.semesterStart.toString(),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        item {// 结束日期
                            TitleCard(
                                onClick = { internalNavController.navigate(InternalNavRoutes.END_DATE) },
                                onLongClick = { viewModel.onAction(EditTimetableAction.UpdateEndDate()) },
                                subtitle = { if (timetable.semesterEnd != null) Text("长按设置为永不结束") },
                                title = { Text(stringResource(R.string.edit_timetable_end)) },
                            ) {
                                Text(
                                    timetable.semesterEnd?.toString() ?: "永不结束",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        item {// 颜色
                            ColorPickerCard(
                                label = stringResource(R.string.edit_timetable_color),
                                color = timetable.color,
                                onClick = { internalNavController.navigate(InternalNavRoutes.COLOR) })
                        }

                        if (timetable.timetableId != 0L) { // 只有编辑时才显示
                            item {
                                DeleteButton(
                                    label = "删除此课表",
                                    onClick = { internalNavController.navigate(InternalNavRoutes.DELETE_CONFIRM) })
                            }
                        }
                    }
                }
            }
        }


        composable(InternalNavRoutes.START_DATE) {
            WithSuccessContent { timetable ->
                DatePicker(
                    onDatePicked = { newDate ->
                        viewModel.onAction(
                            EditTimetableAction.UpdateStartDate(
                                newDate.toKotlinLocalDate()
                            )
                        )
                        internalNavController.popBackStack()
                    },
                    initialDate = timetable.semesterStart.toJavaLocalDate(),
                )
            }
        }


        composable(InternalNavRoutes.END_DATE) {
            WithSuccessContent { timetable ->
                DatePicker(
                    onDatePicked = { newDate ->
                        viewModel.onAction(
                            EditTimetableAction.UpdateEndDate(
                                newDate.toKotlinLocalDate()
                            )
                        )
                        internalNavController.popBackStack()
                    },
                    initialDate = (timetable.semesterEnd
                        ?: timetable.semesterStart).toJavaLocalDate()
                )
            }
        }


        composable(InternalNavRoutes.NAME) {
            WithSuccessContent { timetable ->
                NameEditScreen(
                    label = "输入课表名称", initialText = timetable.semesterName
                ) { newValue ->
                    viewModel.onAction(EditTimetableAction.UpdateName(newValue))
                    internalNavController.popBackStack() // 保存后退出
                }
            }


        }

        composable(InternalNavRoutes.COLOR) {
            ColorSelectionScreen { color ->
                viewModel.onAction(EditTimetableAction.UpdateColor(color))
                internalNavController.popBackStack()
            }
        }

        composable(InternalNavRoutes.DELETE_CONFIRM) {
            WithSuccessContent { timetable ->
                DeleteConfirmScreen(detail = "课表 “${timetable.semesterName}”", onConfirm = {
                    viewModel.onAction(EditTimetableAction.Delete)
                    navController.popBackStack()
                }, onCancel = {
                    internalNavController.popBackStack() // 只是关掉确认页，回到编辑页
                })
            }
        }
    }
}