package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.components.HandleEditUiState
import com.hufeng943.timetable.presentation.ui.screens.common.ColorSelectionScreen
import com.hufeng943.timetable.presentation.ui.screens.common.DeleteConfirmScreen
import com.hufeng943.timetable.presentation.ui.screens.common.NameEditScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.InternalNavRoutes
import com.hufeng943.timetable.presentation.viewmodel.edit.course.EditCourseAction
import com.hufeng943.timetable.presentation.viewmodel.edit.course.EditCourseViewModel

@Composable
fun EditCourseScreen(
    viewModel: EditCourseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val internalNavController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = internalNavController, startDestination = InternalNavRoutes.MAIN
    ) {
        composable(InternalNavRoutes.MAIN) {
            HandleEditUiState(uiState) { course ->
                EditCourseMainPager(
                    course = course,
                    onClickSave = {
                        viewModel.onAction(EditCourseAction.Upsert)
                        navController.popBackStack()
                    },
                    onClickName = { internalNavController.navigate(InternalNavRoutes.NAME) },
                    onClickLocation = { internalNavController.navigate(InternalNavRoutes.LOCATION) },
                    onLongClickLocation = { viewModel.onAction(EditCourseAction.UpdateLocation()) },
                    onClickTeacher = { internalNavController.navigate(InternalNavRoutes.TEACHER) },
                    onLongClickTeacher = { viewModel.onAction(EditCourseAction.UpdateTeacher()) },
                    onClickColor = { internalNavController.navigate(InternalNavRoutes.COLOR) },
                    onClickDelete = { internalNavController.navigate(InternalNavRoutes.DELETE_CONFIRM) }
                )
            }
        }


        composable(InternalNavRoutes.NAME) {
            HandleEditUiState(uiState) { course ->
                NameEditScreen(label = "输入课程名称", initialText = course.name) {
                    viewModel.onAction(EditCourseAction.UpdateName(it))
                    internalNavController.popBackStack()
                }
            }
        }

        composable(InternalNavRoutes.LOCATION) {
            HandleEditUiState(uiState) { course ->
                NameEditScreen(label = "输入上课地点", initialText = course.location ?: "") {
                    viewModel.onAction(EditCourseAction.UpdateLocation(it))
                    internalNavController.popBackStack()
                }
            }
        }

        composable(InternalNavRoutes.TEACHER) {
            HandleEditUiState(uiState) { course ->
                NameEditScreen(label = "输入教师姓名", initialText = course.teacher ?: "") {
                    viewModel.onAction(EditCourseAction.UpdateTeacher(it))
                    internalNavController.popBackStack()
                }
            }
        }

        composable(InternalNavRoutes.COLOR) {
            ColorSelectionScreen { color ->
                viewModel.onAction(EditCourseAction.UpdateColor(color))
                internalNavController.popBackStack()
            }
        }

        composable(InternalNavRoutes.DELETE_CONFIRM) {
            HandleEditUiState(uiState) { course ->
                DeleteConfirmScreen(detail = "课程 “${course.name}”", onConfirm = {
                    viewModel.onAction(EditCourseAction.Delete)
                    navController.popBackStack()
                }, onCancel = { internalNavController.popBackStack() })
            }
        }
    }
}
