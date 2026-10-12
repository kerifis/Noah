package com.talktv.apple3d.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.talktv.apple3d.theme.Apple3DTheme

enum class TileGrammarCategory {
    PRONOUN,   // Yellow frosted glass
    VERB,      // Green frosted glass
    NOUN,      // Orange / Peach frosted glass
    INTEROCEPTION, // Red / Coral frosted glass
    DESCRIPTOR // Blue frosted glass
}

/**
 * Apple VisionOS & tvOS Inspired 3D Emoji Tile Card.
 *
 * Implements:
 * 1. Apple Continuous Squircle Shape (26dp)
 * 2. Frosted Glassmorphism gradient background preserving Modified Fitzgerald Key
 * 3. 10-Foot UI D-Pad Focus: 1.14x spring scale pop, glowing ambient halo, and crisp rim border
 * 4. High-resolution 3D emoji iconography with depth shadows
 */
@Composable
fun Apple3DTileCard(
    word: String,
    category: TileGrammarCategory,
    emoji3dResId: Int? = null,
    drawableResName: String? = null,
    fallbackEmoji: String = "✨",
    modifier: Modifier = Modifier,
    cardWidth: Dp = 190.dp,
    cardHeight: Dp = 210.dp,
    onClick: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val resolvedResId = remember(drawableResName, emoji3dResId) {
        emoji3dResId ?: drawableResName?.let { name ->
            val id = context.resources.getIdentifier(name, "drawable", context.packageName)
            if (id != 0) id else null
        }
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Smooth spring physics for Apple-like scale pop
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.14f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tileScale"
    )

    // Select Fitzgerald Key frosted background and halo glow
    val (backgroundBrush, haloColor) = when (category) {
        TileGrammarCategory.PRONOUN -> Pair(Apple3DTheme.PronounGlassBrush, Apple3DTheme.PronounGlowColor)
        TileGrammarCategory.VERB -> Pair(Apple3DTheme.VerbGlassBrush, Apple3DTheme.VerbGlowColor)
        TileGrammarCategory.NOUN -> Pair(Apple3DTheme.NounGlassBrush, Apple3DTheme.NounGlowColor)
        TileGrammarCategory.INTEROCEPTION -> Pair(Apple3DTheme.InteroceptionGlassBrush, Apple3DTheme.InteroceptionGlowColor)
        TileGrammarCategory.DESCRIPTOR -> Pair(Apple3DTheme.DescriptorGlassBrush, Apple3DTheme.DescriptorGlowColor)
    }

    Box(
        modifier = modifier
            .width(cardWidth)
            .height(cardHeight)
            .zIndex(if (isFocused) 10f else 1f)
            .scale(scale)
            // Luminous ambient glow when highlighted by D-pad
            .drawBehind {
                if (isFocused) {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            asFrameworkPaint().apply {
                                isAntiAlias = true
                                color = haloColor.copy(alpha = 0.55f).toArgb()
                                maskFilter = android.graphics.BlurMaskFilter(
                                    32f,
                                    android.graphics.BlurMaskFilter.Blur.NORMAL
                                )
                            }
                        }
                        canvas.drawRoundRect(
                            0f, 0f, size.width, size.height,
                            54f, 54f, paint
                        )
                    }
                }
            }
            // Apple Continuous Squircle Shape
            .clip(Apple3DTheme.SquircleTileShape)
            // Glassmorphism Frosted Gradient Background
            .background(backgroundBrush)
            // High-contrast rim border for 10-foot television clarity
            .border(
                border = BorderStroke(
                    width = if (isFocused) 3.5.dp else 1.dp,
                    color = if (isFocused) Color.White else Color.White.copy(alpha = 0.25f)
                ),
                shape = Apple3DTheme.SquircleTileShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .focusable(interactionSource = interactionSource),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // 3D Emoji Render (or high-res fallback)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(105.dp)
                    .padding(bottom = 6.dp)
            ) {
                if (resolvedResId != null) {
                    Image(
                        painter = painterResource(id = resolvedResId),
                        contentDescription = word,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback visual with 3D text styling
                    Text(
                        text = fallbackEmoji,
                        fontSize = 58.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // High-legibility Word Label with VisionOS Typography
            Text(
                text = word,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A1A), // Crisp contrast on frosted pastel glass
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp
            )
        }
    }
}
