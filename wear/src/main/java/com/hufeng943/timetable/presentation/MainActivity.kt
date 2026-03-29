package com.hufeng943.timetable.presentation

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.profileinstaller.ProfileInstaller
import com.hufeng943.timetable.presentation.theme.TimetableTheme
import com.hufeng943.timetable.presentation.ui.AppNavHost
import com.hufeng943.timetable.presentation.viewmodel.AppConfigViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appConfigViewModel: AppConfigViewModel by viewModels()
    private var refreshTrigger by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                ProfileInstaller.writeProfile(this@MainActivity)
                Log.d("ProfileInstaller", "AOT profile 写入成功")
            } catch (e: Exception) {
                Log.w("ProfileInstaller", "AOT 写入失败: ${e.message}", e)
            }
        } // 给没Google Play的设备跑跑 AOT

        lifecycleScope.launch {// 启动时语言同步
            appConfigViewModel.appConfig.drop(1) // 跳过初始值
                .first()
                .let { config ->
                    applyLanguageChange(config.locale)
                }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appConfigViewModel.localeRefreshEvent.collect { targetLocale ->
                    applyLanguageChange(targetLocale)
                }
            }
        }

        setContent {
            refreshTrigger
            TimetableTheme {
                AppNavHost()
            }
        }
    }

    private fun applyLanguageChange(targetLocale: Locale?) {
        val config = resources.configuration
        config.setLocale(targetLocale)
        resources.updateConfiguration(config, resources.displayMetrics)
        applicationContext.resources.updateConfiguration(config, resources.displayMetrics)
        refreshTrigger++ // 触发全局重组
        Log.d("AppConfig", "语言更新为: $targetLocale")
    }
}
