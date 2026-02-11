package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.FilledTonalButton
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
import com.hufeng943.timetable.presentation.ui.screens.edit.InternalNavRoutes
import com.hufeng943.timetable.presentation.ui.screens.edit.common.ColorSelectionScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.common.DeleteConfirmScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.common.NameEditScreen
import com.hufeng943.timetable.presentation.ui.screens.loading.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.edit.course.EditCourseAction
import com.hufeng943.timetable.presentation.viewmodel.edit.course.EditCourseViewModel

@Composable
fun EditCourseScreen(
    viewModel: EditCourseViewModel = hiltViewModel()
) {
    val scrollState = rememberScalingLazyListState()
    val state by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val internalNavController = rememberSwipeDismissableNavController()

    when (val uiState = state) {
        is UiState.Success -> {
            val course = uiState.data

            SwipeDismissableNavHost(
                navController = internalNavController, startDestination = InternalNavRoutes.MAIN
            ) {
                composable(InternalNavRoutes.MAIN) {
                    ScreenScaffold(scrollState = scrollState, edgeButton = {
                        EdgeButton(
                            onClick = {
                                viewModel.onAction(EditCourseAction.Upsert)
                                navController.popBackStack()
                            }) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = stringResource(R.string.check)
                            )
                        }
                    }) { contentPadding ->
                        ScalingLazyColumn(
                            state = scrollState, contentPadding = contentPadding
                        ) {
                            item {
                                ListHeader {
                                    Text(
                                        if (course.id == 0L) "添加课程"
                                        else "编辑课程"
                                    )
                                }
                            }

                            item { // 课程名称
                                TitleCard(
                                    onClick = { internalNavController.navigate(InternalNavRoutes.NAME) },
                                    title = {
                                        Text(
                                            "课程名称",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    },
                                ) {
                                    Text(course.name, style = MaterialTheme.typography.labelLarge)
                                }
                            }

                            item { // 上课地点
                                TitleCard(
                                    onClick = { internalNavController.navigate(InternalNavRoutes.LOCATION) },
                                    title = {
                                        Text(
                                            "上课地点",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    },
                                ) {
                                    Text(
                                        course.location ?: "未设置",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }

                            item { // 授课教师
                                TitleCard(
                                    onClick = { internalNavController.navigate(InternalNavRoutes.TEACHER) },
                                    title = {
                                        Text(
                                            "授课教师",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    },
                                ) {
                                    Text(
                                        course.teacher ?: "未设置",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }

                            item { // 颜色
                                TitleCard(
                                    onClick = { internalNavController.navigate(InternalNavRoutes.COLOR) },
                                    title = {
                                        Text(
                                            "课程颜色",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    },
                                ) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .background(
                                                Color(course.color),
                                                MaterialTheme.shapes.medium
                                            )
                                    )
                                }
                            }

                            if (course.id != 0L) {
                                item {
                                    FilledTonalButton(
                                        onClick = { internalNavController.navigate(InternalNavRoutes.DELETE_CONFIRM) },
                                        modifier = Modifier.fillMaxWidth(),
                                        icon = {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = null
                                            )
                                        },
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    ) {
                                        Text("删除此课程")
                                    }
                                }
                            }
                        }
                    }
                }

                composable(InternalNavRoutes.NAME) {
                    NameEditScreen(label = "输入课程名称", initialText = course.name) {
                        viewModel.onAction(EditCourseAction.UpdateName(it))
                        internalNavController.popBackStack()
                    }
                }

                composable(InternalNavRoutes.LOCATION) {
                    NameEditScreen(label = "输入上课地点", initialText = course.location ?: "") {
                        viewModel.onAction(EditCourseAction.UpdateLocation(it))
                        internalNavController.popBackStack()
                    }
                }

                composable(InternalNavRoutes.TEACHER) {
                    NameEditScreen(label = "输入教师姓名", initialText = course.teacher ?: "") {
                        viewModel.onAction(EditCourseAction.UpdateTeacher(it))
                        internalNavController.popBackStack()
                    }
                }

                composable(InternalNavRoutes.COLOR) {
                    ColorSelectionScreen { color ->
                        viewModel.onAction(EditCourseAction.UpdateColor(color))
                        internalNavController.popBackStack()
                    }
                }

                composable(InternalNavRoutes.DELETE_CONFIRM) {
                    DeleteConfirmScreen(detail = "课程 “${course.name}”", onConfirm = {
                        viewModel.onAction(EditCourseAction.Delete)
                        navController.popBackStack()
                    }, onCancel = { internalNavController.popBackStack() })
                }
            }
        }

        else -> LoadingScreen()
    }
}