package com.hufeng943.timetable.presentation.ui.common

import android.util.Log
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("未提供 NavController！")
}

fun NavController.navigateSingle(
    route: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    Log.d("navigateSingle", "route: $route，${currentBackStackEntry?.destination?.route == route}")
    if (currentBackStackEntry?.destination?.route == route) return

    navigate(route) {
        launchSingleTop = true
        restoreState = true
        builder()
    }
}