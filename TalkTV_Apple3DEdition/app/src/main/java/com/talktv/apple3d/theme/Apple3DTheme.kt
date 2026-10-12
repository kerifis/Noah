package com.talktv.apple3d.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * VisionOS & Apple tvOS Inspired Design Tokens for TalkTV.
 * Preserves the Modified Fitzgerald Key with luxurious frosted glass gradients.
 */
object Apple3DTheme {
    // Cinema Dark Backgrounds
    val BackgroundDark = Color(0xFF0A0E17)
    val BackgroundSurface = Color(0xFF121826)
    val AmbientGlow = Color(0x3300E5FF)

    // Glassmorphism Frosted Borders
    val GlassBorderSubtle = Color(0x33FFFFFF)
    val GlassBorderFocus = Color(0xFFFFFFFF)

    // Modified Fitzgerald Key - Frosted Glass Gradients (Pronouns: Yellow)
    val PronounGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xDDFFF59D), // Soft buttery gold top
            Color(0xCCFBC02D)  // Deep amber glass bottom
        )
    )
    val PronounGlowColor = Color(0xFFFFD54F)

    // Modified Fitzgerald Key - Frosted Glass Gradients (Verbs: Green)
    val VerbGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xDDA5D6A7), // Fresh mint top
            Color(0xCC388E3C)  // Rich emerald glass bottom
        )
    )
    val VerbGlowColor = Color(0xFF66BB6A)

    // Modified Fitzgerald Key - Frosted Glass Gradients (Nouns / Shows: Orange & Peach)
    val NounGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xDDFFCC80), // Apricot cream top
            Color(0xCCF57C00)  // Warm peach tangerine glass bottom
        )
    )
    val NounGlowColor = Color(0xFFFFA726)

    // Interoception & Care Needs (Rose Crimson)
    val InteroceptionGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xDDFFCDD2), // Soft coral top
            Color(0xCCD32F2F)  // Deep crimson glass bottom
        )
    )
    val InteroceptionGlowColor = Color(0xFFEF5350)

    // Descriptors & Prepositions (Periwinkle Blue)
    val DescriptorGlassBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xDDB3E5FC), // Frosted glacier top
            Color(0xCC0288D1)  // Ocean glass bottom
        )
    )
    val DescriptorGlowColor = Color(0xFF29B6F6)

    // Apple Squircle Shapes
    val SquircleTileShape = RoundedCornerShape(26.dp)
    val SquircleBarShape = RoundedCornerShape(20.dp)
    val SquirclePillShape = RoundedCornerShape(32.dp)
}
