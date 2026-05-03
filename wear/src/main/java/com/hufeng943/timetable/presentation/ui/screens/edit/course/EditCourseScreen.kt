package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.displayName
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.ui.common.popSafe
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
                    onSave = {
                        viewModel.onAction(EditCourseAction.Upsert)
                        navController.popSafe()
                    },
                    onNameClick = { internalNavController.navigateSingle(InternalNavRoutes.NAME) },
                    onLocationClick = { internalNavController.navigateSingle(InternalNavRoutes.LOCATION) },
                    onLocationLongClick = { viewModel.onAction(EditCourseAction.UpdateLocation()) },
                    onTeacherClick = { internalNavController.navigateSingle(InternalNavRoutes.TEACHER) },
                    onTeacherLongClick = { viewModel.onAction(EditCourseAction.UpdateTeacher()) },
                    onColorClick = { internalNavController.navigateSingle(InternalNavRoutes.COLOR) },
                    onColorLongClick = { viewModel.onAction(EditCourseAction.UpdateColor()) },
                    onDelete = { internalNavController.navigateSingle(InternalNavRoutes.DELETE_CONFIRM) })
            }
        }


        composable(InternalNavRoutes.NAME) {
            HandleEditUiState(uiState) { course ->
                NameEditScreen(
                    label = stringResource(R.string.edit_course_name_hint),
                    initialText = course.name
                ) {
                    viewModel.onAction(EditCourseAction.UpdateName(it))
                    internalNavController.popSafe()
                }
            }
        }

        composable(InternalNavRoutes.LOCATION) {
            HandleEditUiState(uiState) { course ->
                NameEditScreen(
                    label = stringResource(R.string.edit_course_location_hint),
                    initialText = course.location ?: ""
                ) {
                    viewModel.onAction(EditCourseAction.UpdateLocation(it))
                    internalNavController.popSafe()
                }
            }
        }

        composable(InternalNavRoutes.TEACHER) {
            HandleEditUiState(uiState) { course ->
                NameEditScreen(
                    label = stringResource(R.string.edit_course_teacher_hint),
                    initialText = course.teacher ?: ""
                ) {
                    viewModel.onAction(EditCourseAction.UpdateTeacher(it))
                    internalNavController.popSafe()
                }
            }
        }

        composable(InternalNavRoutes.COLOR) {
            ColorSelectionScreen { color ->
                viewModel.onAction(EditCourseAction.UpdateColor(color))
                internalNavController.popSafe()
            }
        }

        composable(InternalNavRoutes.DELETE_CONFIRM) {
            HandleEditUiState(uiState) { course ->
                DeleteConfirmScreen(
                    detail = stringResource(
                        R.string.edit_course_display_name, course.displayName
                    ), onConfirm = {
                    viewModel.onAction(EditCourseAction.Delete)
                        navController.popSafe()
                    }, onCancel = { internalNavController.popSafe() })
            }
        }
    }
}
