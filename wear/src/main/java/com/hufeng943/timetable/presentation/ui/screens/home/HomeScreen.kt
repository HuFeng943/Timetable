package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.HorizontalPageIndicator
import com.hufeng943.timetable.R
import com.hufeng943.timetable.shared.model.Timetable
import com.hufeng943.timetable.shared.ui.mappers.toCourseWithSlots

@Composable
fun HomeScreen(timetables: List<Timetable>) {
    // TODO 可选择课表
    val timetable: Timetable? = timetables.firstOrNull()
    val pagerState = rememberPagerState(pageCount = { 2 })
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(), state = pagerState
        ) { page ->
            when (page) {
                0 -> {
                    if (timetable != null) {
                        TimetablePager(
                            timetable = timetable, // 传递 Timetable
                            coursesIdList = timetable.toCourseWithSlots(),
                            title = stringResource(R.string.home_title)
                        )
                    } else {
                        EmptyPager()
                    }
                }

                1 -> MorePager()

            }
        }
        // 页面指示器
        HorizontalPageIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp)
        )
    }
}