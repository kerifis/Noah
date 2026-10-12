package com.talktv.aac.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.talktv.aac.R
import com.talktv.aac.ui.components.PhraseBarButton
import com.talktv.aac.ui.components.tvFocusGlow
import com.talktv.aac.ui.theme.FocusGlowCyan
import com.talktv.aac.ui.theme.FocusGlowYellow

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabs = listOf("Vocabulary Stages", "Usage Analytics", "Speech & Timer", "Tile Manager")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
        ) {
            // Header with Back Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                PhraseBarButton(
                    iconRes = R.drawable.ic_back,
                    contentDescription = "Back to TalkTV Grid",
                    glowColor = FocusGlowCyan,
                    enabled = true,
                    onClick = onBack
                )

                Column {
                    Text(
                        text = "Caregiver & Therapist Settings",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Customize vocabulary stages, review usage data, and configure voice output",
                        fontSize = 14.sp,
                        color = Color(0xFF8B9CB5)
                    )
                }
            }

            // Tab Navigation Row (Focusable with remote D-pad)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    SettingsTabItem(
                        title = tabTitle,
                        isSelected = uiState.selectedTab == index,
                        onClick = { viewModel.selectTab(index) }
                    )
                }
            }

            // Tab Content
            Box(modifier = Modifier.fillMaxSize()) {
                when (uiState.selectedTab) {
                    0 -> {
                        StageManagementView(
                            currentStage = uiState.currentStage,
                            autoUnlockEnabled = uiState.autoUnlockEnabled,
                            successfulRewards = uiState.successfulRewards,
                            stages = uiState.stages,
                            onSelectStage = { viewModel.setStage(it) },
                            onToggleAutoUnlock = { viewModel.setAutoUnlock(it) }
                        )
                    }

                    1 -> {
                        CaregiverAnalyticsView(
                            totalPresses = uiState.totalPresses,
                            successfulRewards = uiState.successfulRewards,
                            wordFrequencies = uiState.wordFrequencies,
                            recentEvents = uiState.recentPressEvents
                        )
                    }

                    2 -> {
                        SpeechAndTimerSettingsView(
                            speechRate = uiState.speechRate,
                            speechPitch = uiState.speechPitch,
                            idleTimeoutSeconds = uiState.idleTimeoutSeconds,
                            onRateChange = { viewModel.updateSpeechRate(it) },
                            onPitchChange = { viewModel.updateSpeechPitch(it) },
                            onTimeoutChange = { viewModel.updateIdleTimeout(it) },
                            onTestVoice = { viewModel.testVoice() }
                        )
                    }

                    3 -> {
                        TileManagerView(
                            allTiles = uiState.allTiles,
                            onDeleteTile = { viewModel.deleteTile(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsTabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .tvFocusGlow(
                isFocused = isFocused,
                glowColor = FocusGlowCyan,
                cornerRadius = 12.dp,
                focusedScale = 1.05f
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isSelected -> FocusGlowCyan
                    isFocused -> Color(0xFF263750)
                    else -> Color(0xFF162031)
                }
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.Black else Color.White
        )
    }
}

@Composable
fun SpeechAndTimerSettingsView(
    speechRate: Float,
    speechPitch: Float,
    idleTimeoutSeconds: Int,
    onRateChange: (Float) -> Unit,
    onPitchChange: (Float) -> Unit,
    onTimeoutChange: (Int) -> Unit,
    onTestVoice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF141C2B))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Speech Rate
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TTS Speech Rate",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = String.format("%.2fx", speechRate),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = FocusGlowCyan
                )
            }
            Slider(
                value = speechRate,
                onValueChange = onRateChange,
                valueRange = 0.5f..1.5f,
                colors = SliderDefaults.colors(
                    thumbColor = FocusGlowCyan,
                    activeTrackColor = FocusGlowCyan
                )
            )
        }

        // Speech Pitch
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TTS Speech Pitch",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = String.format("%.2fx", speechPitch),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = FocusGlowYellow
                )
            }
            Slider(
                value = speechPitch,
                onValueChange = onPitchChange,
                valueRange = 0.6f..1.4f,
                colors = SliderDefaults.colors(
                    thumbColor = FocusGlowYellow,
                    activeTrackColor = FocusGlowYellow
                )
            )
        }

        // Idle Timeout Duration
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Phrase Idle Reset Timeout",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "$idleTimeoutSeconds seconds",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E676)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                listOf(5, 10, 15, 30).forEach { seconds ->
                    TimeoutOptionChip(
                        seconds = seconds,
                        isSelected = idleTimeoutSeconds == seconds,
                        onClick = { onTimeoutChange(seconds) }
                    )
                }
            }
        }

        // Voice Test Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SettingsActionButton(
                text = "▶ Test Voice Output",
                accentColor = FocusGlowCyan,
                onClick = onTestVoice
            )
        }
    }
}

@Composable
fun TimeoutOptionChip(seconds: Int, isSelected: Boolean, onClick: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .tvFocusGlow(isFocused, FocusGlowCyan, cornerRadius = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFF00E676) else Color(0xFF1E2B40))
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = "${seconds}s",
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.Black else Color.White
        )
    }
}

@Composable
fun SettingsActionButton(
    text: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .tvFocusGlow(isFocused, accentColor, cornerRadius = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isFocused) accentColor else Color(0xFF1E2F48))
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isFocused) Color.Black else Color.White
        )
    }
}

@Composable
fun TileManagerView(
    allTiles: List<com.talktv.aac.data.model.Tile>,
    onDeleteTile: (com.talktv.aac.data.model.Tile) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF141C2B))
            .padding(20.dp)
    ) {
        Text(
            text = "ACTIVE VOCABULARY TILES (${allTiles.size})",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = FocusGlowYellow,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        androidx.compose.foundation.lazy.LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(allTiles) { tile ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1C273C))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = tile.word.uppercase(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Stage ${tile.unlockStage}",
                                fontSize = 12.sp,
                                color = Color(0xFF8B9CB5)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = tile.category.displayName,
                                fontSize = 12.sp,
                                color = tile.category.composeColor
                            )
                        }
                        if (tile.contentUri != null) {
                            Text(
                                text = "URI: ${tile.contentUri}",
                                fontSize = 11.sp,
                                color = Color(0xFF6B7E96),
                                maxLines = 1
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = tile.action.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (tile.action == com.talktv.aac.data.model.TileAction.SPEAK_AND_LAUNCH) Color(0xFF00E676) else FocusGlowCyan
                        )
                        if (tile.id.startsWith("custom_")) {
                            SettingsActionButton(
                                text = "Delete",
                                accentColor = Color(0xFFEF5350),
                                onClick = { onDeleteTile(tile) }
                            )
                        }
                    }
                }
            }
        }
    }
}
