package com.hufeng943.timetable.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalLocale
import androidx.core.content.edit
import androidx.core.content.pm.PackageInfoCompat
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
            val prefs = getSharedPreferences("android.content.Context.MODE_PRIVATE", MODE_PRIVATE)
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val currentVersion = PackageInfoCompat.getLongVersionCode(packageInfo)

            val lastWrittenVersion = prefs.getLong("last_aot_version", -1L)

            if (lastWrittenVersion < currentVersion) {
                try {
                    ProfileInstaller.writeProfile(this@MainActivity)
                    prefs.edit { putLong("last_aot_version", currentVersion) }
                    Log.i("ProfileInstaller", "AOT Profile 已为版本 $currentVersion 更新")
                } catch (e: Exception) {
                    Log.w("ProfileInstaller", "AOT 写入失败: ${e.message}")
                }
            } else {
                Log.i(
                    "ProfileInstaller", "$lastWrittenVersion!<$currentVersion 跳过 ProfileInstaller"
                )
            }
        } // 给没Google Play的设备跑跑 AOT

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appConfigViewModel.localeRecreateEvent.collect {
                    recreate()
                }
            }
        }

        setContent {
            Log.d("MainActivity", "孩子们，现在Locale是‘${LocalLocale.current.platformLocale}’")
            TimetableTheme {
                AppNavHost()
            }
        }
    }
}
