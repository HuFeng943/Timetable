package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.Text

@Composable
fun EmptyPager() {//TODO

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "没有课表！！！")
    }
}