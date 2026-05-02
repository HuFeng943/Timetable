package com.hufeng943.timetable.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.TimeText
import androidx.wear.compose.material3.TimeTextDefaults.rememberTimeSource
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.common.LocalAppConfig
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.screens.detail.CourseDetailScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.course.CourseListScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.course.EditCourseScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.timeslot.EditTimeSlotScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.timeslot.TimeSlotListScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.timetable.EditTimetableScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.timetable.TimetableListScreen
import com.hufeng943.timetable.presentation.ui.screens.home.HomeScreen
import com.hufeng943.timetable.presentation.ui.screens.more.about.AboutLibrariesScreen
import com.hufeng943.timetable.presentation.ui.screens.more.about.AboutScreen
import com.hufeng943.timetable.presentation.ui.screens.more.settings.SettingScreen
import com.hufeng943.timetable.presentation.viewmodel.AppConfigViewModel

@Composable
fun AppNavHost(appConfigViewModel: AppConfigViewModel = hiltViewModel()) {
    val navController = rememberSwipeDismissableNavController()
    val config by appConfigViewModel.appConfig.collectAsState()
    AppScaffold(timeText = {
        TimeText(
            timeSource = if (config.is24HourFormat) rememberTimeSource("HH:mm") else rememberTimeSource(
                "h:mm"
            )
        )
    }) {
        CompositionLocalProvider(
            LocalNavController provides navController, LocalAppConfig provides config
        ) {
            SwipeDismissableNavHost(
                navController = navController, startDestination = NavRoutes.MAIN
            ) {
                composable(NavRoutes.MAIN) { HomeScreen() }

                composable(NavRoutes.COURSE_DETAIL) { CourseDetailScreen() }

                composable(NavRoutes.LIST_TIMETABLE) { TimetableListScreen() }

                composable(NavRoutes.EDIT_TIMETABLE) { EditTimetableScreen() }

                composable(NavRoutes.LIST_COURSE) { CourseListScreen() }

                composable(NavRoutes.EDIT_COURSE) { EditCourseScreen() }

                composable(NavRoutes.LIST_TIMESLOT) { TimeSlotListScreen() }

                composable(NavRoutes.EDIT_TIMESLOT) { EditTimeSlotScreen() }

                composable(NavRoutes.MORE_ABOUT) { AboutScreen() }

                composable(NavRoutes.MORE_ABOUT_LIBRARIES) { AboutLibrariesScreen() }

                composable(NavRoutes.MORE_SETTINGS) { SettingScreen() }
            }
        }
    }
}