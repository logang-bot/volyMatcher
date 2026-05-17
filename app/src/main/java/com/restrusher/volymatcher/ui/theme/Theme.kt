package com.restrusher.volymatcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Color.White,
    primaryContainer = AccentDeep,
    onPrimaryContainer = Color.White,
    secondary = Lime,
    onSecondary = Ink,
    secondaryContainer = Sky,
    onSecondaryContainer = Ink,
    tertiary = Clay,
    onTertiary = Paper,
    background = Cream,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Cream,
    onSurfaceVariant = InkSoft,
    outline = InkFaint,
    outlineVariant = Line,
    // inverseSurface / inverseOnSurface are used for hero cards
    // (dark bg with light text in light mode)
    inverseSurface = Ink,
    inverseOnSurface = Paper,
)

private val DarkColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = Color.White,
    primaryContainer = AccentDeep,
    onPrimaryContainer = Color.White,
    secondary = Lime,
    onSecondary = Ink,
    secondaryContainer = Sky,
    onSecondaryContainer = Ink,
    tertiary = Clay,
    onTertiary = DarkSurface,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkBackground,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    inverseSurface = DarkHeroBg,
    inverseOnSurface = DarkOnSurface,
)

@Composable
fun VolyMatcherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
