package com.talktv.aac.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.talktv.aac.ui.theme.FocusGlowCyan

/**
 * Modifier extension that applies a vibrant, animated TV focus glow and scale effect.
 * Essential for 10-foot viewing distance on Android TV so a child always knows where the cursor is.
 */
@Composable
fun Modifier.tvFocusGlow(
    isFocused: Boolean,
    glowColor: Color = FocusGlowCyan,
    cornerRadius: Dp = 18.dp,
    focusedScale: Float = 1.08f,
    borderWidth: Dp = 4.5.dp
): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isFocused) focusedScale else 1.0f,
        animationSpec = tween(durationMillis = 180),
        label = "scale"
    )

    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isFocused) borderWidth else 1.5.dp,
        animationSpec = tween(durationMillis = 180),
        label = "borderWidth"
    )

    return this
        .scale(scale)
        .then(
            if (isFocused) {
                Modifier
                    .drawBehind {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().asFrameworkPaint().apply {
                                color = glowColor.toArgb()
                                setShadowLayer(
                                    32.dp.toPx(),
                                    0f,
                                    0f,
                                    glowColor.copy(alpha = 0.85f).toArgb()
                                )
                            }
                            canvas.nativeCanvas.drawRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height,
                                cornerRadius.toPx(),
                                cornerRadius.toPx(),
                                paint
                            )
                        }
                    }
                    .border(
                        BorderStroke(animatedBorderWidth, glowColor),
                        shape = RoundedCornerShape(cornerRadius)
                    )
            } else {
                Modifier.border(
                    BorderStroke(1.5.dp, Color(0xFF23324D)),
                    shape = RoundedCornerShape(cornerRadius)
                )
            }
        )
}
