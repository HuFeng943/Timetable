package com.hufeng943.timetable.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.screens.detail.CourseDetailScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.course.CourseListScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.timetable.EditTimetableScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.timetable.TimetableListScreen
import com.hufeng943.timetable.presentation.ui.screens.home.HomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberSwipeDismissableNavController()
    AppScaffold {
        CompositionLocalProvider(LocalNavController provides navController) {
            SwipeDismissableNavHost(
                navController = navController, startDestination = NavRoutes.MAIN
            ) {
                composable(NavRoutes.MAIN) { HomeScreen() }

                composable(NavRoutes.COURSE_DETAIL) { CourseDetailScreen() }

                composable(NavRoutes.LIST_TIMETABLE) { TimetableListScreen() }

                composable(NavRoutes.EDIT_TIMETABLE) { EditTimetableScreen() }

                composable(NavRoutes.LIST_COURSE) { CourseListScreen() }
            }
        }
    }
}