package com.talktv.aac.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talktv.aac.ui.components.PhraseBar
import com.talktv.aac.ui.components.PinPad
import com.talktv.aac.ui.components.TileCard
import com.talktv.aac.ui.theme.FocusGlowCyan
import com.talktv.aac.ui.theme.FocusGlowYellow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            PhraseBar(
                phrase = uiState.currentPhrase,
                onSpeakPhrase = viewModel::speakPhrase,
                onClearPhrase = viewModel::clearPhrase,
                onOpenSettings = viewModel::openPinDialog
            )

            // Scrollable Grid Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 28.dp, end = 28.dp, top = 16.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                // ZONE 1: CORE WORD ROW (Always visible, yellow/green Fitzgerald AAC standard)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(FocusGlowYellow)
                        )
                        Text(
                            text = "CORE WORDS",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            color = Color(0xFFFFD600)
                        )
                        Text(
                            text = "(Stage ${uiState.currentStage} Active)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF8B9CB5)
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                    ) {
                        items(uiState.coreTiles, key = { it.id }) { tile ->
                            TileCard(
                                tile = tile,
                                onTileSelected = { selectedTile ->
                                    viewModel.onTileSelected(selectedTile, onNavigateToCategory)
                                }
                            )
                        }
                    }
                }

                // ZONE 2: CATEGORY ROW (Opens sub-grids of actual show thumbnails)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(FocusGlowCyan)
                        )
                        Text(
                            text = "SHOWS & CATEGORIES",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            color = Color(0xFF00E5FF)
                        )
                        Text(
                            text = "Select to view shows",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF8B9CB5)
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                    ) {
                        items(uiState.categoryTiles, key = { it.id }) { tile ->
                            TileCard(
                                tile = tile,
                                onTileSelected = { selectedTile ->
                                    viewModel.onTileSelected(selectedTile, onNavigateToCategory)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Caregiver PIN Protection Overlay (Avoids native modal popups that interfere with Leanback D-pad)
        AnimatedVisibility(
            visible = uiState.showPinDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xE6060910)),
                contentAlignment = Alignment.Center
            ) {
                PinPad(
                    expectedPin = uiState.caregiverPin,
                    onPinSuccess = {
                        viewModel.closePinDialog()
                        onNavigateToSettings()
                    },
                    onCancel = {
                        viewModel.closePinDialog()
                    }
                )
            }
        }
    }
}
