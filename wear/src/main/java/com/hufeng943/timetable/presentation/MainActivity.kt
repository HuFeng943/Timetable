package com.hufeng943.timetable.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.profileinstaller.ProfileInstaller
import com.hufeng943.timetable.data.PreferenceStorageEntryPoint
import com.hufeng943.timetable.presentation.theme.TimetableTheme
import com.hufeng943.timetable.presentation.ui.AppNavHost
import com.hufeng943.timetable.presentation.viewmodel.AppConfigViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appConfigViewModel: AppConfigViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        // 手动获取 PreferenceStorage
        val storage = EntryPointAccessors.fromApplication(
            newBase.applicationContext, PreferenceStorageEntryPoint::class.java
        ).preferenceStorage()

        val config = runBlocking {
            storage.appConfigFlow.first()
        }

        // 应用语言配置
        val context = if (config.languageTag != null) {
            val locale = Locale.forLanguageTag(config.languageTag)
            val configuration = Configuration(newBase.resources.configuration)
            Locale.setDefault(locale)
            configuration.setLocale(locale)
            newBase.createConfigurationContext(configuration)
        } else {
            newBase
        }

        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                ProfileInstaller.writeProfile(this@MainActivity)
                Log.i("ProfileInstaller", "AOT profile 写入成功")
            } catch (e: Exception) {
                Log.w("ProfileInstaller", "AOT 写入失败: ${e.message}", e)
            }
        } // 给没Google Play的设备跑跑 AOT

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appConfigViewModel.localeRefreshEvent.collect { languageTag ->
                    applyLanguageChange(Locale.forLanguageTag(languageTag))
                }
            }
        }

        setContent {
            Log.d("MainActivity", "孩子们，现在Locale是‘${Locale.getDefault()}’")
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
        Log.i("MainActivity", "语言动态更新为: $targetLocale")
    }
}
