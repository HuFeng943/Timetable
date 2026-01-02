package com.hufeng943.timetable.presentation.ui.screens.loading

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.NavRoutes
import com.hufeng943.timetable.shared.model.TimeTable

@Composable
fun LoadingScreen(timeTables: List<TimeTable>? = null) {
    val navController = LocalNavController.current

    Log.v("navController2", (timeTables==null).toString())
    LaunchedEffect(timeTables) {
        if (timeTables != null) {
            navController.navigate(NavRoutes.MAIN) {
                popUpTo(NavRoutes.LOADING) { inclusive = true }
                Log.v("navController", "加载完成，跳转！")
            }
        }
        }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("课程加载中…")
    }
}