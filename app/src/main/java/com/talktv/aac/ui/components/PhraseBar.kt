package com.talktv.aac.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talktv.aac.R
import com.talktv.aac.ui.theme.*

@Composable
fun PhraseBar(
    phrase: List<String>,
    onSpeakPhrase: () -> Unit,
    onClearPhrase: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(Color(0xFF0F1626))
            .padding(horizontal = 24.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // App Logo / Title Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_talktv_logo),
                contentDescription = "TalkTV Logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "TalkTV",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF00E5FF)
                )
                Text(
                    text = "AAC REMOTE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD600)
                )
            }
        }

        // Phrase Display Box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF182338))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (phrase.isEmpty()) {
                Text(
                    text = "Use remote D-pad to select words...",
                    color = Color(0xFF7A8FA6),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(phrase) { word ->
                        androidx.compose.animation.AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInHorizontally()
                        ) {
                            PhraseWordChip(word = word)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Action Buttons: Speak, Clear, Settings
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Speak Full Phrase Button
            PhraseBarButton(
                iconRes = R.drawable.ic_play,
                contentDescription = "Speak Phrase",
                glowColor = FocusGlowCyan,
                enabled = phrase.isNotEmpty(),
                onClick = onSpeakPhrase
            )

            // Clear Phrase Button
            PhraseBarButton(
                iconRes = R.drawable.ic_trash,
                contentDescription = "Clear Phrase",
                glowColor = AacRedStop,
                enabled = phrase.isNotEmpty(),
                onClick = onClearPhrase
            )

            // Caregiver Settings Screen Button
            PhraseBarButton(
                iconRes = R.drawable.ic_settings,
                contentDescription = "Settings",
                glowColor = FocusGlowYellow,
                enabled = true,
                onClick = onOpenSettings
            )
        }
    }
}

@Composable
fun PhraseWordChip(word: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF263753))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = word.uppercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun PhraseBarButton(
    iconRes: Int,
    contentDescription: String,
    glowColor: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(54.dp)
            .tvFocusGlow(
                isFocused = isFocused && enabled,
                glowColor = glowColor,
                cornerRadius = 14.dp,
                focusedScale = 1.12f
            )
            .clip(RoundedCornerShape(14.dp))
            .background(
                when {
                    !enabled -> Color(0xFF161E2E)
                    isFocused -> Color(0xFF2B3A54)
                    else -> Color(0xFF1E2B42)
                }
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(enabled = enabled, interactionSource = interactionSource)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = if (enabled) glowColor else Color(0xFF4A5A72),
            modifier = Modifier.size(26.dp)
        )
    }
}
