package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val OrangePrimary = Color(0xFFFF8F00)
private val OrangePrimaryDark = Color(0xFFFFB74D)
private val OledBlack = Color(0xFF0B0B0B)
private val DarkSurface = Color(0xFF151515)
private val DarkSurfaceVariant = Color(0xFF1F1F1F)

private val DarkColorPalette = darkColorScheme(
    primary = OrangePrimaryDark,
    onPrimary = Color(0xFF1D1200),
    background = OledBlack,
    onBackground = Color(0xFFF3F3F3),
    surface = DarkSurface,
    onSurface = Color(0xFFF3F3F3),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFD0D0D0),
    error = Color(0xFFFF6B6B),
    onError = Color(0xFF2B0000)
)

private val LightColorPalette = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    background = Color(0xFFF7F7F7),
    onBackground = Color(0xFF111111),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = Color(0xFF555555),
    error = Color(0xFFB3261E),
    onError = Color.White
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography(),
        shapes = Shapes(
            small = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
            medium = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            large = androidx.compose.foundation.shape.RoundedCornerShape(22.dp)
        ),
        content = content
    )
}
