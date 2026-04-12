package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.DatePicker
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.displayName
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.ui.components.HandleEditUiState
import com.hufeng943.timetable.presentation.ui.screens.common.ColorSelectionScreen
import com.hufeng943.timetable.presentation.ui.screens.common.DeleteConfirmScreen
import com.hufeng943.timetable.presentation.ui.screens.common.NameEditScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.InternalNavRoutes
import com.hufeng943.timetable.presentation.viewmodel.edit.timetable.EditTimetableAction
import com.hufeng943.timetable.presentation.viewmodel.edit.timetable.EditTimetableViewModel
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate

@Composable
fun EditTimetableScreen(
    viewModel: EditTimetableViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val internalNavController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = internalNavController, startDestination = InternalNavRoutes.MAIN
    ) {
        composable(InternalNavRoutes.MAIN) {
            HandleEditUiState(uiState) { timetable ->
                EditTimetableMainPager(
                    timetable = timetable,
                    onSave = {
                        viewModel.onAction(EditTimetableAction.Upsert)
                        navController.popBackStack()
                    },
                    onNameClick = { internalNavController.navigateSingle(InternalNavRoutes.NAME) },
                    onStartDateClick = { internalNavController.navigateSingle(InternalNavRoutes.START_DATE) },
                    onStartDateLongClick = { viewModel.onAction(EditTimetableAction.UpdateStartDate()) },
                    onEndDateClick = { internalNavController.navigateSingle(InternalNavRoutes.END_DATE) },
                    onEndDateLongClick = { viewModel.onAction(EditTimetableAction.UpdateEndDate()) },
                    onColorClick = { internalNavController.navigateSingle(InternalNavRoutes.COLOR) },
                    onDelete = { internalNavController.navigateSingle(InternalNavRoutes.DELETE_CONFIRM) },
                    startDateIsToday = viewModel.toDay == timetable.semesterStart
                )
            }
        }

        composable(InternalNavRoutes.START_DATE) {
            HandleEditUiState(uiState) { timetable ->
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
            HandleEditUiState(uiState) { timetable ->
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
            HandleEditUiState(uiState) { timetable ->
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
            HandleEditUiState(uiState) { timetable ->
                DeleteConfirmScreen(detail = "课表 “${timetable.displayName}”", onConfirm = {
                    viewModel.onAction(EditTimetableAction.Delete)
                    navController.popBackStack()
                }, onCancel = {
                    internalNavController.popBackStack() // 只是关掉确认页，回到编辑页
                })
            }
        }
    }
}