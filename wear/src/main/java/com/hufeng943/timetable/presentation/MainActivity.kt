package com.hufeng943.timetable.presentation

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.profileinstaller.ProfileInstaller
import com.hufeng943.timetable.presentation.theme.TimetableTheme
import com.hufeng943.timetable.presentation.ui.AppNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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

        setContent {
            TimetableTheme {
                AppNavHost()
            }
        }
    }
}

