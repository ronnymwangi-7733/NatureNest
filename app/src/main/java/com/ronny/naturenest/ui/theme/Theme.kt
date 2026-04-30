package com.ronny.naturenest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// NatureNest Warm Natural Color Palette
val Blush = Color(0xFFF4A7B9)
val BlushLight = Color(0xFFFDE8EE)
val BlushDark = Color(0xFFD4708A)

val Sage = Color(0xFF7AAE8E)
val SageLight = Color(0xFFE4F2EA)
val SageDark = Color(0xFF4D8A65)

val Peach = Color(0xFFFFB98A)
val PeachLight = Color(0xFFFFF0E4)
val PeachDark = Color(0xFFD4854E)

val Cream = Color(0xFFFFF8F0)
val CreamDark = Color(0xFFF5EAD8)

val WarmBrown = Color(0xFF8B5E52)
val DeepBrown = Color(0xFF5C3D35)

val LavenderSoft = Color(0xFFCDB8E8)
val LavenderLight = Color(0xFFF2EBF9)

val TextPrimary = Color(0xFF3D2B27)
val TextSecondary = Color(0xFF7A5C57)
val TextHint = Color(0xFFB09490)

val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceCream = Color(0xFFFFF8F4)
val SurfaceCard = Color(0xFFFFFBF8)

val ErrorRed = Color(0xFFE57373)
val SuccessGreen = Color(0xFF81C784)

private val LightColorScheme = lightColorScheme(
    primary = Blush,
    onPrimary = Color.White,
    primaryContainer = BlushLight,
    onPrimaryContainer = DeepBrown,

    secondary = Sage,
    onSecondary = Color.White,
    secondaryContainer = SageLight,
    onSecondaryContainer = SageDark,

    tertiary = Peach,
    onTertiary = Color.White,
    tertiaryContainer = PeachLight,
    onTertiaryContainer = PeachDark,

    background = SurfaceCream,
    onBackground = TextPrimary,

    surface = SurfaceCard,
    onSurface = TextPrimary,

    surfaceVariant = CreamDark,
    onSurfaceVariant = TextSecondary,

    error = ErrorRed,
    outline = TextHint
)

@Composable
fun NatureNestTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = NatureNestTypography,
        content = content
    )
}