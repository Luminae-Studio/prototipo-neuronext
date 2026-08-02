package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DeepPurple,
    onPrimary = OffWhite,
    primaryContainer = LavenderClara,
    onPrimaryContainer = DeepPurple,
    secondary = LavenderEscura,
    onSecondary = OffWhite,
    secondaryContainer = LavenderBase,
    onSecondaryContainer = DeepPurple,
    tertiary = GoldenYellow,
    onTertiary = NearBlackCanvas,
    tertiaryContainer = GoldenYellowLight,
    onTertiaryContainer = NearBlackCanvas,
    background = OffWhite,
    onBackground = DarkGrayText,
    surface = LilacBackground,
    onSurface = DarkGrayText,
    surfaceVariant = LavenderClara,
    onSurfaceVariant = DeepPurple,
    outline = LightGray
)

private val DarkColorScheme = darkColorScheme(
    primary = LavenderBase,
    onPrimary = NearBlackCanvas,
    primaryContainer = DeepPurple,
    onPrimaryContainer = LilacBackground,
    secondary = LavenderClara,
    onSecondary = NearBlackCanvas,
    secondaryContainer = LavenderEscura,
    onSecondaryContainer = LilacBackground,
    tertiary = GoldenYellow,
    onTertiary = NearBlackCanvas,
    tertiaryContainer = DeepPurpleDark,
    onTertiaryContainer = GoldenYellowLight,
    background = NearBlackCanvas,
    onBackground = LilacBackground,
    surface = DeepPurpleDark,
    onSurface = LilacBackground,
    surfaceVariant = DeepPurple,
    onSurfaceVariant = LavenderBase,
    outline = LavenderEscura
)

@Composable
fun NeuroNextTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
