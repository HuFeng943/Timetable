package com.hufeng943.timetable.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.profileinstaller.ProfileInstaller
import androidx.room.Room
import com.hufeng943.timetable.presentation.theme.TimeTableTheme
import com.hufeng943.timetable.presentation.ui.AppNavHost
import com.hufeng943.timetable.presentation.viewmodel.TimeTableViewModel
import com.hufeng943.timetable.presentation.viewmodel.TimeTableViewModelFactory
import com.hufeng943.timetable.shared.data.database.AppDatabase
import com.hufeng943.timetable.shared.data.repository.TimeTableRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "timetable-db"
        ).build()
    }

    private val repository: TimeTableRepositoryImpl by lazy {
        TimeTableRepositoryImpl(appDatabase.timeTableDao())
    }

    // 3️⃣ ViewModel 用官方推荐方式绑定生命周期
    private val viewModel: TimeTableViewModel by viewModels {
        TimeTableViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO)
        {
            try {
                ProfileInstaller.writeProfile(this@MainActivity)
                Log.d("ProfileInstaller", "AOT profile 写入成功")
            } catch (e: Exception) {
                Log.w("ProfileInstaller", "AOT 写入失败: ${e.message}", e)
            }
        } // 给没Google Play的设备跑跑 AOT

        setContent {
            TimeTableTheme {
                AppNavHost(viewModel)
            }
        }
    }
}

