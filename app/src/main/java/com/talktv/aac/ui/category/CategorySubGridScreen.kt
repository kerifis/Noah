package com.talktv.aac.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talktv.aac.R
import com.talktv.aac.data.local.TalkTvDatabase
import com.talktv.aac.data.model.Tile
import com.talktv.aac.data.model.TileAction
import com.talktv.aac.player.ContentLauncher
import com.talktv.aac.tts.TtsManager
import com.talktv.aac.ui.components.PhraseBar
import com.talktv.aac.ui.components.PhraseBarButton
import com.talktv.aac.ui.components.TileCard
import com.talktv.aac.ui.theme.FocusGlowCyan
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun CategorySubGridScreen(
    categoryId: String,
    initialPhrase: List<String>,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val database = remember { TalkTvDatabase.getInstance(context, coroutineScope) }
    val ttsManager = remember { TtsManager(context) }

    var phrase by remember { mutableStateOf(initialPhrase) }
    var tiles by remember { mutableStateOf<List<Tile>>(emptyList()) }
    var categoryTitle by remember { mutableStateOf("Category Shows") }

    LaunchedEffect(categoryId) {
        val settings = database.settingsDao().getSettingsSync()
        val stage = settings?.currentStage ?: 1

        val parentTile = database.tileDao().getTileById(categoryId)
        if (parentTile != null) {
            categoryTitle = parentTile.word
        }

        database.tileDao().getTilesByParentCategory(categoryId, stage).collect { subTiles ->
            tiles = subTiles
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    fun handleTileSelected(tile: Tile) {
        val updatedPhrase = phrase + tile.word
        phrase = updatedPhrase

        when (tile.action) {
            TileAction.SPEAK_ONLY -> {
                ttsManager.speakWord(tile.word)
                coroutineScope.launch {
                    database.pressEventDao().insertEvent(
                        com.talktv.aac.data.model.PressEvent(
                            tileId = tile.id,
                            tileWord = tile.word,
                            timestampMillis = System.currentTimeMillis(),
                            phraseAtPress = updatedPhrase,
                            wasRewardTriggered = false
                        )
                    )
                }
            }

            TileAction.SPEAK_AND_LAUNCH -> {
                val fullPhrase = updatedPhrase.joinToString(" ")
                ttsManager.speakPhrase(fullPhrase) {
                    coroutineScope.launch {
                        val settings = database.settingsDao().getSettingsSync()
                        val preferInternal = settings?.useInternalPlayerFallback ?: true
                        ContentLauncher.launchReward(
                            context = context,
                            contentUri = tile.contentUri,
                            title = tile.word,
                            preferInternalPlayer = preferInternal
                        )
                    }
                }

                coroutineScope.launch {
                    database.pressEventDao().insertEvent(
                        com.talktv.aac.data.model.PressEvent(
                            tileId = tile.id,
                            tileWord = tile.word,
                            timestampMillis = System.currentTimeMillis(),
                            phraseAtPress = updatedPhrase,
                            wasRewardTriggered = true,
                            contentUri = tile.contentUri
                        )
                    )
                }

                phrase = emptyList()
            }

            TileAction.OPEN_CATEGORY -> {
                ttsManager.speakWord(tile.word)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Pinned Phrase Bar at top
            PhraseBar(
                phrase = phrase,
                onSpeakPhrase = {
                    if (phrase.isNotEmpty()) {
                        ttsManager.speakPhrase(phrase.joinToString(" "))
                    }
                },
                onClearPhrase = { phrase = emptyList() },
                onOpenSettings = onBackToHome
            )

            // Header Row with Back Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 16.dp, bottom = 12.dp)
            ) {
                PhraseBarButton(
                    iconRes = R.drawable.ic_back,
                    contentDescription = "Back to Home",
                    glowColor = FocusGlowCyan,
                    enabled = true,
                    onClick = onBackToHome
                )

                Text(
                    text = "$categoryTitle Shows",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = "Select a show to speak phrase and watch reward",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8B9CB5)
                )
            }

            // Sub-grid of Show Tiles
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 200.dp),
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tiles, key = { it.id }) { tile ->
                    TileCard(
                        tile = tile,
                        onTileSelected = { handleTileSelected(it) }
                    )
                }
            }
        }
    }
}
