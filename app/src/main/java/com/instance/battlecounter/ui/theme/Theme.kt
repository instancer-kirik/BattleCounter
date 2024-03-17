package com.instance.battlecounter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

//private val DarkColorPalette = darkColors(
//    primary = primarydark,
//    primaryVariant = primaryVariantdark,
//    secondary = secondarydark
//)
//
//private val LightColorPalette = lightColors(
//    primary = primarylight,
//    primaryVariant = primaryVariantlight,
//    secondary = secondarylight

    /* Other default colors to override
background = Color.White,
surface = Color.White,
onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/

val Shapes = Shapes() // Customize as needed
// Example colors, adjust according to your app's design
    private val LightColorScheme = lightColorScheme(
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC5),
        // Define other colors for light theme
    )

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC5),
    // Define other colors for dark theme
)
//val RainbowColors = listOf(Color.Cyan, Color.Blue, purple400, Color.Red, Color.Yellow)
@Composable
fun BattleCounterTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,

        shapes = Shapes, // Ensure you have defined Shapes
        content = content
    )
}
