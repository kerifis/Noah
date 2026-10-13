package com.talktv.apple3d.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.talktv.apple3d.components.Apple3DSubtitleBar
import com.talktv.apple3d.components.Apple3DTileCard
import com.talktv.apple3d.components.TileGrammarCategory
import com.talktv.apple3d.theme.Apple3DTheme

data class TileModel(
    val id: String,
    val word: String,
    val category: TileGrammarCategory,
    val fallbackEmoji: String,
    val row: Int,
    val col: Int,
    val drawableResName: String? = null,
    val contentUri: String? = null
)

/**
 * 10-Foot Television Home Screen with Apple 3D Design Language and
 * Spatially Locked Motor-Plan Grid.
 */
@Composable
fun Apple3DHomeScreen(
    onTileSelected: (TileModel) -> Unit = {},
    onLaunchReward: (String) -> Unit = {}
) {
    var activePhrase by remember { mutableStateOf(listOf<String>()) }
    var predictedSentence by remember { mutableStateOf("") }

    // CLINICAL MOTOR PLANNING: Locked spatial coordinates matching WordPower 60 / Alberta IPP
    val tiles = remember {
        listOf(
            // Column 1-2: Core Pronouns & People (Yellow Frosted Glass with Apple Memojis)
            TileModel("i", "I", TileGrammarCategory.PRONOUN, "👦", 0, 0, "memoji_noah_3d_v2"),
            TileModel("you", "you", TileGrammarCategory.PRONOUN, "👤", 1, 0, "memoji_orange_hair_man"),

            // Column 3-4: Core Verbs (Emerald Green Frosted Glass with 3D Textured Hands)
            TileModel("want", "want", TileGrammarCategory.VERB, "🤲", 0, 1, "apple_3d_want_hands"),
            TileModel("more", "more", TileGrammarCategory.VERB, "➕", 0, 2),
            TileModel("stop", "stop", TileGrammarCategory.VERB, "🛑", 1, 1),
            TileModel("like", "like", TileGrammarCategory.VERB, "❤️", 1, 2),

            // Column 5-6: High-Interest Nouns & Reinforcers (Warm Orange Peach Frosted Glass with 3D Textured Assets)
            TileModel("pancakes", "pancakes", TileGrammarCategory.NOUN, "🥞", 0, 3, "apple_3d_pancakes", "https://youtube.com/cooking_pancakes"),
            TileModel("monkey", "monkey", TileGrammarCategory.NOUN, "🐵", 0, 4, "apple_3d_monkey", "https://youtube.com/monkey_clips"),
            TileModel("bluey", "Bluey", TileGrammarCategory.NOUN, "🐕", 1, 3, null, "https://youtube.com/bluey"),
            TileModel("Mom", "Mom", TileGrammarCategory.PRONOUN, "👩", 1, 4, "memoji_auburn_woman"),

            // Column 7-8: Alberta IPP Interoception Support (Coral Red Frosted Glass)
            TileModel("bathroom", "bathroom", TileGrammarCategory.INTEROCEPTION, "🚻", 0, 5),
            TileModel("water", "water", TileGrammarCategory.INTEROCEPTION, "💧", 1, 5),
            TileModel("blanket", "blanket", TileGrammarCategory.INTEROCEPTION, "🛋️", 0, 6),
            TileModel("help", "help", TileGrammarCategory.INTEROCEPTION, "🛟", 1, 6)
        )
    }

    val initialFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        initialFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Apple3DTheme.BackgroundDark)
            .padding(horizontal = 48.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // TOP: VisionOS Frosted Glass AI Closed-Caption Subtitle Bar
        Apple3DSubtitleBar(
            activePhrase = activePhrase,
            predictedSentence = predictedSentence,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // CENTER: Spatially Locked 3D Tile Grid (Motor Planning Intact)
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(tiles) { tile ->
                val isFirstTile = tile.id == "i"
                Apple3DTileCard(
                    word = tile.word,
                    category = tile.category,
                    drawableResName = tile.drawableResName,
                    fallbackEmoji = tile.fallbackEmoji,
                    modifier = if (isFirstTile) Modifier.focusRequester(initialFocusRequester) else Modifier,
                    onClick = {
                        val newPhrase = activePhrase + tile.word
                        activePhrase = newPhrase

                        // AI Subtitle Predictor expansion
                        predictedSentence = predictSubtitle(newPhrase)

                        // If tile carries a video reward (e.g. pancakes/animals/bluey), launch reward!
                        tile.contentUri?.let { uri ->
                            onLaunchReward(uri)
                        }

                        onTileSelected(tile)
                    }
                )
            }
        }
    }
}

/**
 * Natural language subtitle expansion rule engine for autistic child.
 */
private fun predictSubtitle(phrase: List<String>): String {
    val joined = phrase.joinToString(" ").lowercase()
    return when {
        joined.contains("want") && joined.contains("pancakes") ->
            "I want to have pancakes for breakfast, please."
        joined.contains("want") && joined.contains("monkey") ->
            "I want to watch monkey videos and funny animal clips, please!"
        joined.contains("monkey") ->
            "Look at the cute 3D monkey!"
        joined.contains("want") && joined.contains("bluey") ->
            "I would really love to watch Bluey on TV now."
        joined.contains("bathroom") ->
            "I need to use the washroom right away."
        joined.contains("water") ->
            "I am thirsty and need a glass of water."
        joined.contains("blanket") ->
            "I need my heavy blanket to feel calm and regulated."
        joined.contains("help") ->
            "I need help right now, please assist me."
        joined.contains("stop") ->
            "I want to stop and take a quiet break."
        phrase.size == 1 && phrase[0].equals("I", ignoreCase = true) ->
            "I am expressing what I want..."
        else -> phrase.joinToString(" ")
    }
}
