package com.hufeng943.timetable.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.profileinstaller.ProfileInstaller
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import com.hufeng943.timetable.presentation.theme.TimeTableTheme
import com.hufeng943.timetable.presentation.ui.TimetableScreen
import com.hufeng943.timetable.shared.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO)
        { ProfileInstaller.writeProfile(this@MainActivity)} // 给没Google Play的设备跑跑 AOT
        // 硬编码时间，测试用
        val courses = getSampleTimetable().toDayUiCourses(LocalDate(2025,11,1))

        setContent {
            TimeTableTheme{
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberPagerState { 10 },
                ) { page -> // page 是当前页面的索引 (0 到 9)
                    when(page) {
                        0 -> TimetableScreen(courses = courses, title = "哈吉米南北绿豆！")
                        // TODO 更多页面
                        else -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center // 内容居中
                            ) {
                                BasicText(
                                    text = "Page $page", // 显示当前页码
                                    style = TextStyle(color = Color.White)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// 临时示例数据
private fun getSampleTimetable(): TimeTable {
    return TimeTable(
        allCourses = listOf(
            Course(
                id = 1145,
                name = "物理5 (双周)",
                timeSlots = listOf(
                    TimeSlot(
                        startTime = LocalTime(13, 30),
                        endTime = LocalTime(15, 0),
                        dayOfWeek = DayOfWeek.FRIDAY
                    )
                ),
                recurrence = WeekPattern.EVEN_WEEK, // 修正：改成双周
                teacher = "李老师",
                location = "B202"
            ),
            Course(
                id = 11919,
                name = "数学6 (单周)",
                timeSlots = listOf(
                    TimeSlot(
                        startTime = LocalTime(9, 0),
                        endTime = LocalTime(11, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    ),TimeSlot(
                        startTime = LocalTime(19, 0),
                        endTime = LocalTime(22, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    ),TimeSlot(
                        startTime = LocalTime(13, 30),
                        endTime = LocalTime(15, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    )
                ),
                recurrence = WeekPattern.ODD_WEEK, // 增加：单周课
                teacher = "王老师",
                location = "A101"
            ),
            Course(
                id = 810,
                name = "英语6 (每周)",
                timeSlots = listOf(
                    // 周六的英语课
                    TimeSlot(
                        startTime = LocalTime(15, 30),
                        endTime = LocalTime(17, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    ),                    TimeSlot(
                        startTime = LocalTime(13, 30),
                        endTime = LocalTime(14, 0),
                        dayOfWeek = DayOfWeek.SATURDAY
                    )
                ),
                recurrence = WeekPattern.EVERY_WEEK, // 每周都上
                teacher = "赵老师",
                location = "C303"
            )
        ),
        semesterName = "2025秋季",
        createdAt = kotlin.time.Clock.System.now(),
        semesterStart = LocalDate(2025, 10, 1), // 2025-10-01 是周三
        timeTableId = 114514,
        semesterEnd = null
    )
}