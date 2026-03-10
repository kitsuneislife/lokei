package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary   = Color(0xFFFF9800), // laranja de destaque
    onPrimary = Color.Black,
    background= Color.Black,
    surface   = Color.Black,
    onSurface = Color.White
)

private val LightColorPalette = lightColorScheme(
    primary   = Color(0xFFFF9800),
    onPrimary = Color.Black,
    background= Color.White,
    surface   = Color.White,
    onSurface = Color.Black
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
