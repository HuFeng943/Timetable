package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.HorizontalPageIndicator

@Composable
fun HomeScreen() {
    val pagerState = rememberPagerState(pageCount = { 2 })
    var isDatePickerOpen by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = !isDatePickerOpen
        ) { page ->
            when (page) {
                0 -> TimetablePager(onOpenStateChanged = { isDatePickerOpen = it })
                1 -> MorePager()
            }
        }
        // 页面指示器
        AnimatedVisibility(
            visible = !isDatePickerOpen, // 当时间选择器关闭时显示
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp)
        ) {
            HorizontalPageIndicator(
                pagerState = pagerState
            )
        }
    }
}