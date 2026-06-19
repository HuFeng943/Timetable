package com.hufeng943.timetable.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.MaterialTheme
import com.materialkolor.PaletteStyle

/**
 * 局部动态色彩主题容器
 * * 当传入的 [seedColor] 有效时，会自动基于该颜色生成专属的 Wear OS M3 调色板并应用到内部 Content 中；
 * 当颜色为 [Color.Unspecified] 时，则无缝回退并沿用系统/全局的当前的 [MaterialTheme]。
 */
@Composable
fun DynamicSubTheme(
    seedColor: Color,
    isDark: Boolean = false,
    isAmoled: Boolean = false,
    style: PaletteStyle = PaletteStyle.Vibrant,
    content: @Composable () -> Unit
) {
    val currentColorScheme = if (seedColor != Color.Unspecified) {
        rememberDynamicColorScheme(
            seedColor = seedColor,
            isDark = isDark,
            isAmoled = isAmoled,
            style = style
        )
    } else {
        MaterialTheme.colorScheme
    }

    MaterialTheme(colorScheme = currentColorScheme, content = content)
}