package com.talktv.aac.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talktv.aac.data.model.Stage
import com.talktv.aac.ui.components.tvFocusGlow
import com.talktv.aac.ui.theme.FocusGlowCyan
import com.talktv.aac.ui.theme.FocusGlowYellow

@Composable
fun StageManagementView(
    currentStage: Int,
    autoUnlockEnabled: Boolean,
    successfulRewards: Int,
    stages: List<Stage>,
    onSelectStage: (Int) -> Unit,
    onToggleAutoUnlock: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Auto-Unlock Control Card
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF141C2B))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Automatic Stage Progression",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Automatically unlocks next stage when child reaches successful rewarded communication threshold.",
                        fontSize = 14.sp,
                        color = Color(0xFF8B9CB5),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Switch(
                    checked = autoUnlockEnabled,
                    onCheckedChange = { onToggleAutoUnlock(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = FocusGlowCyan,
                        checkedTrackColor = Color(0xFF0F3A4E),
                        uncheckedThumbColor = Color(0xFF6B7E96),
                        uncheckedTrackColor = Color(0xFF1D283C)
                    )
                )
            }
        }

        // Section Title
        item {
            Text(
                text = "VOCABULARY PROGRESSION STAGES",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = FocusGlowYellow,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // List of Stages (1 to 4)
        items(stages) { stage ->
            val isActive = stage.stageNumber == currentStage
            StageCardItem(
                stage = stage,
                isActive = isActive,
                successfulRewards = successfulRewards,
                onSelect = { onSelectStage(stage.stageNumber) }
            )
        }
    }
}

@Composable
fun StageCardItem(
    stage: Stage,
    isActive: Boolean,
    successfulRewards: Int,
    onSelect: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .tvFocusGlow(
                isFocused = isFocused,
                glowColor = if (isActive) FocusGlowYellow else FocusGlowCyan,
                cornerRadius = 16.dp,
                focusedScale = 1.02f
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    isActive -> Color(0xFF1E2C1E)
                    isFocused -> Color(0xFF1F2B40)
                    else -> Color(0xFF141C2B)
                }
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onSelect()
            }
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stage.title,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isActive) Color(0xFF00E676) else Color.White
                    )

                    if (isActive) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF00E676))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ACTIVE ON TV",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }

                Text(
                    text = "Press remote OK to switch",
                    fontSize = 13.sp,
                    color = Color(0xFF8B9CB5)
                )
            }

            Text(
                text = stage.description,
                fontSize = 15.sp,
                color = Color(0xFFCFD9E5),
                modifier = Modifier.padding(top = 6.dp)
            )

            Text(
                text = "Goal: ${stage.goal}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = FocusGlowYellow,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Progress towards threshold
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mastery Progress: $successfulRewards / ${stage.requiredSuccessfulPresses} rewarded phrases",
                    fontSize = 13.sp,
                    color = Color(0xFF8B9CB5)
                )

                val progressFraction = (successfulRewards.toFloat() / stage.requiredSuccessfulPresses).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF23324C))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .fillMaxHeight()
                            .background(if (isActive) Color(0xFF00E676) else FocusGlowCyan)
                    )
                }
            }
        }
    }
}
