package ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.platform.Font

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

// NOTE: actual font resource files should exist in res/font; here we just reference names
private val Inter = FontFamily(
    Font(resId = R.font.inter_regular, weight = FontWeight.Normal),
    Font(resId = R.font.inter_medium,  weight = FontWeight.Medium),
    Font(resId = R.font.inter_bold,    weight = FontWeight.Bold),
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography(defaultFontFamily = Inter),
        shapes = Shapes(),
        content = content
    )
}
