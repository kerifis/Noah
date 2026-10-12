package com.talktv.apple3d.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talktv.apple3d.theme.Apple3DTheme

/**
 * Apple VisionOS / tvOS Frosted Glass Closed-Caption Subtitle Bar.
 * Expands telegraphic remote inputs into natural predictive sentences.
 */
@Composable
fun Apple3DSubtitleBar(
    activePhrase: List<String>,
    predictedSentence: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .clip(Apple3DTheme.SquircleBarShape)
            // Frosted dark translucent glass
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xB31E293B), // Translucent slate
                        Color(0xD90F172A)  // Deep midnight glass
                    )
                )
            )
            .border(
                border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.20f)),
                shape = Apple3DTheme.SquircleBarShape
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (activePhrase.isEmpty() && predictedSentence.isBlank()) {
            Text(
                text = "Use Remote D-Pad to Select Words...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.45f)
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Closed-Caption Badge
                Box(
                    modifier = Modifier
                        .clip(Apple3DTheme.SquirclePillShape)
                        .background(Color(0xFF00E5FF).copy(alpha = 0.20f))
                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f), Apple3DTheme.SquirclePillShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "AI SUBTITLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E5FF),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // The predicted natural utterance
                Text(
                    text = if (predictedSentence.isNotBlank()) predictedSentence else activePhrase.joinToString(" "),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
