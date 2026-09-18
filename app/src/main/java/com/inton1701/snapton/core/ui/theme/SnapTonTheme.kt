package com.inton1701.snapton.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = SoftSurface,
    onPrimaryContainer = DeepNavy,
    secondary = ActiveBlue,
    onSecondary = Color.White,
    background = AppBackground,
    onBackground = DeepNavy,
    surface = Color.White,
    onSurface = DeepNavy,
    surfaceVariant = SoftSurface,
    onSurfaceVariant = MutedText,
    outline = Hairline,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9EC1FF),
    onPrimary = Color(0xFF002E66),
    secondary = Color(0xFFB8CEEF),
    background = Color(0xFF07152B),
    onBackground = Color(0xFFE9F0FB),
    surface = Color(0xFF0C1D39),
    onSurface = Color(0xFFE9F0FB),
    surfaceVariant = Color(0xFF193154),
    onSurfaceVariant = Color(0xFFC1CEE0),
    outline = Color(0xFF7085A3),
)

@Composable
fun SnapTonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = SnapTonTypography,
        shapes = SnapTonShapes,
        content = content,
    )
}
