package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.viewmodel.AppConfigViewModel

@Composable
fun SettingScreen(
    appConfigViewModel: AppConfigViewModel = hiltViewModel()
) {
    val internalNavController = rememberSwipeDismissableNavController()
    val config by appConfigViewModel.appConfig.collectAsState()

    SwipeDismissableNavHost(
        navController = internalNavController, startDestination = InternalNavRoutes.MAIN
    ) {
        composable(InternalNavRoutes.MAIN) {
            SettingPager()
        }

        composable(InternalNavRoutes.LANGUAGE_SELECT) {
            LanguageSelectPager(
                config = config,
                onLanguageSelect = { languageTag ->
                    appConfigViewModel.updateLanguage(languageTag)
                }
            )
        }
    }
}
