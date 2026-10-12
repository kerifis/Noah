package com.talktv.aac.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = FocusGlowCyan,
    onPrimary = Color.Black,
    secondary = FocusGlowYellow,
    onSecondary = Color.Black,
    tertiary = AacGreenVerb,
    background = TvBackground,
    onBackground = Color.White,
    surface = TvSurfaceCard,
    onSurface = Color.White
)

@Composable
fun TalkTvTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = TvTypography,
        content = content
    )
}
