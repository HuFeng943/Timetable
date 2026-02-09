package com.hufeng943.timetable.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavBackStackEntry
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.screens.detail.CourseDetailScreen
import com.hufeng943.timetable.presentation.ui.screens.home.HomeScreen

// 对一堆getString()的封装
private fun NavBackStackEntry.longArg(key: String): Long? =
    arguments?.getString(key)?.toLongOrNull()

@Composable
fun AppNavHost() {
    val navController = rememberSwipeDismissableNavController()
    AppScaffold {
        CompositionLocalProvider(LocalNavController provides navController) {
            SwipeDismissableNavHost(
                navController = navController, startDestination = NavRoutes.MAIN
            ) {
                composable(NavRoutes.MAIN) { HomeScreen() }

                composable(NavRoutes.COURSE_DETAIL) {
                    CourseDetailScreen()
                }

                composable(NavRoutes.LIST_TIMETABLE) {
//                    TimetableListScreen(TODO
//                        timetables!!
//                    )
                }


                composable(NavRoutes.EDIT_TIMETABLE) { backStackEntry ->
//                    val timetableId = backStackEntry.longArg("timetableId")TODO
//                    val timetable = timetables!!.find { it.timetableId == timetableId }
//                    EditTimetableScreen(
//                        timetable, onAction = viewModel::onAction
//                    )
                }
            }
        }
    }
}