package dev.linmg.sudokufreecodecamp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val lightColorPalette= lightColors(
    primary = primaryGreen,
    secondary = textColorLight,
    surface = lightGrey,
    primaryVariant = gridLineColorLight,
    onPrimary = accentAmber,
    onSurface = accentAmber
)

private val darkColorPalette= darkColors(
    primary = primaryCharcoal,
    secondary = textColorDark,
    surface = lightGreyAlpha,
    primaryVariant = gridLineColorLight,
    onPrimary = accentAmber,
    onSurface = accentAmber
)

@Composable
fun GraphSudokuTheme(
    darkTheme:Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if(darkTheme) darkColorPalette else lightColorPalette,
        typography = typography,
        shapes = shapes,
        content=content
    )
}
