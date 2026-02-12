package com.hufeng943.timetable.presentation.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.screens.common.ErrorScreen
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.detail.CourseDetailViewModel

@Composable
fun CourseDetailScreen(
    viewModel: CourseDetailViewModel = hiltViewModel()
) {
    val uiDetailState by viewModel.detailState.collectAsStateWithLifecycle()

    when (val state = uiDetailState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen(state.throwable)
        is UiState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "未找到课程数据", color = Color.Red)
            }
        }

        is UiState.Success -> {
            val pagerState = rememberPagerState { 2 }

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> DetailsPager(state.data.currentCourseUi)
                    1 -> CourseListPager(state.data.listCourseUi)
                }
            }
        }
    }
}