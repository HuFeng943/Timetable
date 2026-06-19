package com.hufeng943.timetable.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme
import com.materialkolor.Contrast
import com.materialkolor.MaterialKolors
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.ktx.DynamicScheme
import com.materialkolor.scheme.DynamicScheme

/**
 * Create and remember a custom [ColorScheme] based on the provided colors.
 *
 * If a color is not provided, then the color palette will be generated from the [style] and [seedColor].
 *
 * @param[seedColor] The color to base the scheme on.
 * @param[isDark] Whether the scheme should be dark or light.
 * @param[isAmoled] Whether the dark scheme is used with Amoled screen (Pure dark).
 * @param[primary] The primary color of the scheme.
 * @param[secondary] The secondary color of the scheme.
 * @param[tertiary] The tertiary color of the scheme.
 * @param[neutral] The neutral color of the scheme.
 * @param[neutralVariant] The neutral variant color of the scheme.
 * @param[error] The error color of the scheme.
 * @param[style] The style of the scheme.
 * @param[contrastLevel] The contrast level of the scheme.
 * @param[specVersion] The version of the color scheme.
 * @param[platform] The platform of the color scheme.
 * @param[modifyColorScheme] A lambda to modify the created [ColorScheme].
 */
@Composable
public fun rememberDynamicColorScheme(
    seedColor: Color,
    isDark: Boolean,
    isAmoled: Boolean = false,
    primary: Color? = null,
    secondary: Color? = null,
    tertiary: Color? = null,
    neutral: Color? = null,
    neutralVariant: Color? = null,
    error: Color? = null,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    contrastLevel: Double = Contrast.Default.value,
    specVersion: ColorSpec.SpecVersion = ColorSpec.SpecVersion.SPEC_2025,
    platform: DynamicScheme.Platform = DynamicScheme.Platform.WATCH,
    modifyColorScheme: ((ColorScheme) -> ColorScheme)? = null,
): ColorScheme =
    remember(
        seedColor,
        isDark,
        isAmoled,
        primary,
        secondary,
        tertiary,
        neutral,
        neutralVariant,
        error,
        style,
        contrastLevel,
        specVersion,
        platform,
        modifyColorScheme,
    ) {
        dynamicColorScheme(
            seedColor = seedColor,
            isDark = isDark,
            isAmoled = isAmoled,
            primary = primary,
            secondary = secondary,
            tertiary = tertiary,
            neutral = neutral,
            neutralVariant = neutralVariant,
            error = error,
            style = style,
            specVersion = specVersion,
            platform = platform,
            contrastLevel = contrastLevel,
            modifyColorScheme = modifyColorScheme,
        )
    }

/**
 * Create a custom [ColorScheme] based on the provided colors.
 *
 * If a color is not provided, then the color palette will be generated from the [style] and [seedColor].
 *
 * @param[seedColor] The color to base the scheme on.
 * @param[isDark] Whether the scheme should be dark or light.
 * @param[isAmoled] Whether the dark scheme is used with Amoled screen (Pure dark).
 * @param[primary] The primary color of the scheme.
 * @param[secondary] The secondary color of the scheme.
 * @param[tertiary] The tertiary color of the scheme.
 * @param[neutral] The neutral color of the scheme.
 * @param[neutralVariant] The neutral variant color of the scheme.
 * @param[error] The error color of the scheme.
 * @param[style] The style of the scheme.
 * @param[contrastLevel] The contrast level of the scheme.
 * @param[specVersion] The version of the color scheme.
 * @param[platform] The platform of the color scheme.
 * @param[modifyColorScheme] A lambda to modify the created [ColorScheme].
 */
public fun dynamicColorScheme(
    seedColor: Color,
    isDark: Boolean,
    isAmoled: Boolean = false,
    primary: Color? = null,
    secondary: Color? = null,
    tertiary: Color? = null,
    neutral: Color? = null,
    neutralVariant: Color? = null,
    error: Color? = null,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    contrastLevel: Double = Contrast.Default.value,
    specVersion: ColorSpec.SpecVersion = ColorSpec.SpecVersion.Default,
    platform: DynamicScheme.Platform = DynamicScheme.Platform.Default,
    modifyColorScheme: ((ColorScheme) -> ColorScheme)? = null,
): ColorScheme {
    val scheme = DynamicScheme(
        seedColor = seedColor,
        isDark = isDark,
        primary = primary,
        secondary = secondary,
        tertiary = tertiary,
        neutral = neutral,
        neutralVariant = neutralVariant,
        error = error,
        style = style,
        contrastLevel = contrastLevel,
        specVersion = specVersion,
        platform = platform,
    )

    return scheme.toColorScheme(isAmoled, modifyColorScheme)
}

/**
 * Create the actual [ColorScheme] based on the given [DynamicScheme].
 */
public fun DynamicScheme.toColorScheme(
    isAmoled: Boolean = false,
    modifyColorScheme: ((ColorScheme) -> ColorScheme)? = null,
): ColorScheme {
    val colors = MaterialKolors(scheme = this, isAmoled)

    return ColorScheme(
        primary = colors.primary(),
        primaryDim = colors.primaryFixedDim(),
        primaryContainer = colors.primaryContainer(),
        onPrimary = colors.onPrimary(),
        onPrimaryContainer = colors.onPrimaryContainer(),
        secondary = colors.secondary(),
        secondaryDim = colors.secondaryFixedDim(),
        secondaryContainer = colors.secondaryContainer(),
        onSecondary = colors.onSecondary(),
        onSecondaryContainer = colors.onSecondaryContainer(),
        tertiary = colors.tertiary(),
        tertiaryDim = colors.tertiaryFixedDim(),
        tertiaryContainer = colors.tertiaryContainer(),
        onTertiary = colors.onTertiary(),
        onTertiaryContainer = colors.onTertiaryContainer(),
        surfaceContainerLow = colors.surfaceContainerLow(),
        surfaceContainer = colors.surfaceContainer(),
        surfaceContainerHigh = colors.surfaceContainerHigh(),
        onSurface = colors.onSurface(),
        onSurfaceVariant = colors.onSurfaceVariant(),
        outline = colors.outline(),
        outlineVariant = colors.outlineVariant(),
        background = colors.background(),
        onBackground = colors.onBackground(),
        error = colors.error(),
        errorDim = colors.error(),
        errorContainer = colors.errorContainer(),
        onError = colors.onError(),
        onErrorContainer = colors.onErrorContainer(),
    ).let { modifyColorScheme?.invoke(it) ?: it }
}