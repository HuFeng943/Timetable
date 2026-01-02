package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import com.hufeng943.timetable.R
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.mappers.toCourseWithSlots

@Composable
fun HomeScreen(
    navController: NavHostController, timeTables: List<TimeTable>
) {
    // TODO 可选择课表
    val timeTable: TimeTable? = timeTables.firstOrNull()
    HorizontalPager(
        modifier = Modifier.fillMaxSize(), state = rememberPagerState { 2 }) { page ->
        when (page) {
            0 -> {
                if (timeTable != null) {
                    TimetablePager(
                        timeTable = timeTable, // 传递 TimeTable
                        coursesIdList = timeTable.toCourseWithSlots(),
                        title = stringResource(R.string.home_title_today),
                        navController = navController
                    )
                } else {
                    EmptyPager()
                }
            }

            1 -> MorePager(navController)
        }
    }

}