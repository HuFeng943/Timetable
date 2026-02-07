package com.hufeng943.timetable.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.screens.detail.CourseDetailScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.EditTimetable
import com.hufeng943.timetable.presentation.ui.screens.edit.TimetableList
import com.hufeng943.timetable.presentation.ui.screens.home.HomeScreen
import com.hufeng943.timetable.presentation.ui.screens.loading.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.TimetableViewModel
import com.hufeng943.timetable.shared.model.Timetable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId

@Composable
fun AppNavHost(viewModel: TimetableViewModel = hiltViewModel()) {
    val navController = rememberSwipeDismissableNavController()

    // 订阅 Flow -> Compose state
    val timetables by viewModel.timetables.collectAsState()
    CompositionLocalProvider(LocalNavController provides navController) {
        SwipeDismissableNavHost(
            navController = navController, startDestination = NavRoutes.LOADING
        ) {

            composable(NavRoutes.LOADING) {
                LoadingScreen(timetables)
            }
            composable(NavRoutes.ERROR) {
                Log.v("navController", NavRoutes.ERROR)
                TODO()// TODO 单独一个异常界面 可以传递的不同的相关的错误信息
            }
            composable(NavRoutes.MAIN) {
                Log.v("navController1", (timetables == null).toString())
                DataStateGuard(timetables) { tables ->
                    HomeScreen(tables)
                }
            }
            composable(NavRoutes.COURSE_DETAIL) { backStackEntry ->
                DataStateGuard(timetables) { tables ->
                    val courseId = backStackEntry.longArg("courseId")
                    val timeSlotId = backStackEntry.longArg("timeSlotId")
                    if (courseId != null && timeSlotId != null) {
                        val timetable: Timetable? = tables.firstOrNull()
                        CourseDetailScreen(
                            timetable, CourseWithSlotId(courseId, timeSlotId)
                        )
                    } else {
                        navController.navigate(NavRoutes.ERROR)
                    }
                }
            }
            composable(NavRoutes.LIST_TIMETABLE) {
                DataStateGuard(timetables) { tables ->
                    TimetableList(
                        tables, onAction = viewModel::onAction
                    )
                }
            }
            composable(NavRoutes.EDIT_TIMETABLE) {
                DataStateGuard(timetables) {
                    EditTimetable(
                        onAction = viewModel::onAction
                    )
                }

            }
        }
    }
}

// 对一堆getString()的封装
fun NavBackStackEntry.longArg(key: String): Long? = arguments?.getString(key)?.toLongOrNull()

// 封装了重复多次的判断
@Composable
fun <T> DataStateGuard(
    data: T?,
    loadingContent: @Composable () -> Unit = { LoadingScreen() },
    content: @Composable (T) -> Unit
) {
    when {
        data == null -> loadingContent()
        else -> content(data)
    }
}