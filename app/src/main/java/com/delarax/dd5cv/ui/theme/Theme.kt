package com.delarax.dd5cv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.delarax.dd5cv.ui.theme.shapes.Shapes

private val DarkColorPalette = darkColors(
    primary = DarkBlue900,
    primaryVariant = DarkBlue700,
    onPrimary = Color.White,
    secondary = DarkGrey500,
    secondaryVariant = DarkGrey700,
    onSecondary = Color.White
)

private val LightColorPalette = lightColors(
    primary = DarkBlue500,
    primaryVariant = DarkBlue700,
    onPrimary = Color.White,
    secondary = DarkGrey500,
    secondaryVariant = DarkGrey700,
    onSecondary = Color.White

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun Dd5cvTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}