package com.example.parcial2.ui.theme

import androidx.compose.material3.* // Asegúrate de usar Material3
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Importar los colores
import com.example.parcial2.ui.theme.Orange80
import com.example.parcial2.ui.theme.OrangeGrey80
import com.example.parcial2.ui.theme.Black80
import com.example.parcial2.ui.theme.Orange40
import com.example.parcial2.ui.theme.OrangeGrey40
import com.example.parcial2.ui.theme.Black40

// Definir el esquema de colores claro
private val LightColorScheme = lightColorScheme(
    primary = Orange80,
    secondary = OrangeGrey80,
    background = Color.White,
    surface = OrangeGrey40,
    onPrimary = Black80,
    onSecondary = Black80,
    onBackground = Black40,
    onSurface = Black80
)

// Definir el esquema de colores oscuro (opcional)
private val DarkColorScheme = darkColorScheme(
    primary = Orange40,
    secondary = OrangeGrey40,
    background = Black40,
    surface = Black80,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// Definir las formas
val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

// Definir la tipografía
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)

// Función para aplicar el tema
@Composable
fun AppTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = Shapes, // Usamos Shapes
        content = content
    )
}
