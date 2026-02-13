package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.HorizontalPageIndicator

@Composable
fun HomeScreen() {
    val pagerState = rememberPagerState(pageCount = { 2 })
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(), state = pagerState
        ) { page ->
            when (page) {
                0 -> TimetablePager()
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