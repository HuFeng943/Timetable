package com.hufeng943.timetable.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.screens.detail.CourseDetailScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.timetable.EditTimetableScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.timetable.TimetableListScreen
import com.hufeng943.timetable.presentation.ui.screens.home.HomeScreen
import com.hufeng943.timetable.presentation.ui.screens.loading.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.TimetableViewModel
import com.hufeng943.timetable.shared.model.Timetable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId

// 对一堆getString()的封装
private fun NavBackStackEntry.longArg(key: String): Long? =
    arguments?.getString(key)?.toLongOrNull()

@Composable
fun AppNavHost(viewModel: TimetableViewModel = hiltViewModel()) {
    val navController = rememberSwipeDismissableNavController()
    // 订阅 Flow -> Compose state
    val timetables by viewModel.timetables.collectAsState()

    AppScaffold {
        when (timetables) {
            null -> LoadingScreen()
            else -> CompositionLocalProvider(LocalNavController provides navController) {
                SwipeDismissableNavHost(
                    navController = navController, startDestination = NavRoutes.MAIN
                ) {
                    composable(NavRoutes.MAIN) {
                        HomeScreen(timetables!!)
                    }

                    composable(NavRoutes.COURSE_DETAIL) { backStackEntry ->
                        val courseId = backStackEntry.longArg("courseId")
                        val timeSlotId = backStackEntry.longArg("timeSlotId")
                        if (courseId != null && timeSlotId != null) {// TODO timetableID
                            val timetable: Timetable? = timetables!!.firstOrNull()
                            CourseDetailScreen(
                                timetable!!, CourseWithSlotId(courseId, timeSlotId)
                            )
                        }
                    }

                    composable(NavRoutes.LIST_TIMETABLE) {
                        TimetableListScreen(
                            timetables!!
                        )
                    }


                    composable(NavRoutes.EDIT_TIMETABLE) { backStackEntry ->
                        val timetableId = backStackEntry.longArg("timetableId")
                        val timetable = timetables!!.find { it.timetableId == timetableId }
                        EditTimetableScreen(
                            timetable, onAction = viewModel::onAction
                        )
                    }
                }
            }
        }

    }
}