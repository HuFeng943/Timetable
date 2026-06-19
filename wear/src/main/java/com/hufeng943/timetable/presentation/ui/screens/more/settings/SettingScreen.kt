package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.presentation.ui.common.LocalAppConfig
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.viewmodel.AppConfigViewModel

@Composable
fun SettingScreen(
    appConfigViewModel: AppConfigViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    val internalNavController = rememberSwipeDismissableNavController()
    val config = LocalAppConfig.current

    SwipeDismissableNavHost(
        navController = internalNavController, startDestination = InternalNavRoutes.MAIN
    ) {
        composable(InternalNavRoutes.MAIN) {
            SettingPager(
                config = config,
                onLanguageSelectClick = { internalNavController.navigateSingle(InternalNavRoutes.LANGUAGE_SELECT) },
                onTimeFormatSelectClick = { internalNavController.navigateSingle(InternalNavRoutes.TIME_FORMAT_SELECT) },
                onFirstDaySelectClick = { internalNavController.navigateSingle(InternalNavRoutes.FIRST_DAY_SELECT) },
                onDynamicColorToggle = { enabled ->
                    appConfigViewModel.updateDynamicColorEnabled(enabled)
                }
            )
        }

        composable(InternalNavRoutes.LANGUAGE_SELECT) {
            LanguageSelectPager(
                config = config, onLanguageSelect = { languageTag ->
                    appConfigViewModel.updateLanguage(languageTag)
                })
        }

        composable(InternalNavRoutes.TIME_FORMAT_SELECT) {
            TimeFormatSelectPager(
                config = config, onTimeFormatSelect = { timeFormat ->
                    appConfigViewModel.updateFormat(timeFormat)
                })
        }

        composable(InternalNavRoutes.FIRST_DAY_SELECT) {
            FirstDaySelectPager(config) { firstDay ->
                appConfigViewModel.updateFirstDayOfTheWeek(firstDay)
            }
        }
    }
}