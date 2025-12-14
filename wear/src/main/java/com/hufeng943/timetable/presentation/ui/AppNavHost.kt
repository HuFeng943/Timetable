package com.hufeng943.timetable.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.pagers.CourseDetailPager
import com.hufeng943.timetable.presentation.ui.screens.HomeScreen
import com.hufeng943.timetable.presentation.ui.screens.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.TimeTableViewModel
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import com.hufeng943.timetable.shared.ui.mappers.toCourseWithSlots

@Composable
fun AppNavHost(viewModel: TimeTableViewModel) {
    val navController = rememberSwipeDismissableNavController()

    // 订阅 Flow -> Compose state
    val timeTables by viewModel.timeTables.collectAsState()

    // 假设你只关心第一个课表
    val timeTable: TimeTable? = timeTables?.firstOrNull()

    // 生成今天的课程 ID 列表（可以根据你的逻辑改）
    val todayCoursesIdList: List<CourseWithSlotId>? = timeTable?.toCourseWithSlots()
    Log.v("todayCoursesIdList1", todayCoursesIdList.toString())

    SwipeDismissableNavHost(
        navController = navController, startDestination = "loading"
    ) {
        composable("loading") {
            LoadingScreen()
            if (timeTables != null) {
                navController.navigate("main") {
                    popUpTo("loading") { inclusive = true }
                    Log.v("navController", "加载完成，跳转！")
                }
            }

        }
        composable("main") {
            if (timeTable != null && todayCoursesIdList != null) {
                HomeScreen(navController, timeTable, todayCoursesIdList)
            } else {
                // TODO 单独一个异常界面
                LoadingScreen()
            }
        }
        composable("course_detail/{courseId}/{timeSlotId}") { backStackEntry ->
            val courseId = backStackEntry.longArg("courseId")
            val timeSlotId = backStackEntry.longArg("timeSlotId")

            if (timeTable != null && courseId != null && timeSlotId != null) {
                CourseDetailPager(
                    timeTable, CourseWithSlotId(courseId, timeSlotId)
                )
            } else {
                // TODO 单独一个异常界面
                LoadingScreen()
            }

        }
    }
}

// 对一堆getString()的封装
fun NavBackStackEntry.longArg(key: String): Long? = arguments?.getString(key)?.toLongOrNull()