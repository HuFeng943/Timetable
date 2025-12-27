package com.hufeng943.timetable.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import com.hufeng943.timetable.presentation.ui.pagers.MorePager
import com.hufeng943.timetable.presentation.ui.pagers.TimetablePager
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import com.hufeng943.timetable.shared.ui.mappers.toCourseWithSlots

@Composable
fun HomeScreen(
    navController: NavHostController,
    timeTables: List<TimeTable>
) {
    // 假设你只关心第一个课表
    val timeTable: TimeTable? = timeTables?.firstOrNull()
    // 生成今天的课程 ID 列表（可以根据你的逻辑改）
    val todayCoursesIdList: List<CourseWithSlotId>? = timeTable?.toCourseWithSlots()
    Log.v("todayCoursesIdList1", todayCoursesIdList.toString())

    HorizontalPager(
        modifier = Modifier.fillMaxSize(), state = rememberPagerState { 2 }) { page ->
        when (page) {
            0 -> TimetablePager(
                timeTable = timeTable, // 传递 TimeTable
                coursesIdList = todayCoursesIdList, title = "今日程", navController = navController
            )
            1 -> MorePager()
        }
    }

}