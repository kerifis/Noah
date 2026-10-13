package com.talktv.aac.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talktv.aac.data.model.Tile
import com.talktv.aac.ui.theme.FocusGlowCyan
import com.talktv.aac.ui.theme.FocusGlowYellow

@Composable
fun TileCard(
    tile: Tile,
    onTileSelected: (Tile) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    // Resolve category or custom color
    val accentColor = remember(tile) {
        if (tile.customColor != null) {
            Color(tile.customColor)
        } else {
            tile.category.composeColor
        }
    }

    val glowColor = remember(tile, isFocused) {
        if (tile.isCore) FocusGlowYellow else FocusGlowCyan
    }

    // Determine drawable resource ID
    val drawableResId = remember(tile.iconRes) {
        val upgradedIcon = mapOf(
            "ic_tile_i" to "memoji_noah_3d_v2",
            "ic_3d_i" to "memoji_noah_3d_v2",
            "ic_tile_want" to "ic_3d_want",
            "ic_tile_more" to "ic_3d_more",
            "ic_tile_stop" to "ic_3d_stop",
            "ic_tile_again" to "ic_3d_again",
            "ic_tile_like" to "ic_3d_like",
            "ic_tile_no" to "ic_3d_no",
            "ic_tile_help" to "ic_3d_help",
            "ic_tile_feel" to "ic_3d_feel"
        )[tile.iconRes] ?: tile.iconRes
        getResId(context, upgradedIcon, "drawable")
    }

    Box(
        modifier = modifier
            .width(200.dp)
            .height(185.dp)
            .tvFocusGlow(
                isFocused = isFocused,
                glowColor = glowColor,
                cornerRadius = 20.dp,
                focusedScale = 1.08f
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isFocused) Color(0xFF1D293F) else Color(0xFF131B2A)
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onTileSelected(tile)
            }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Category Color Accent Strip (AAC visual key)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(accentColor)
            )

            // ARASAAC Vector Icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (drawableResId != 0) {
                    Image(
                        painter = painterResource(id = drawableResId),
                        contentDescription = tile.word,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback visual
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(accentColor.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tile.word.take(2).uppercase(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = accentColor
                        )
                    }
                }
            }

            // High-Contrast Word Label
            Text(
                text = tile.word.uppercase(),
                color = if (isFocused) Color(0xFF00E5FF) else Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

/**
 * Resolves a dynamic resource name to Android resource ID.
 */
private fun getResId(context: Context, resName: String, defType: String): Int {
    return try {
        context.resources.getIdentifier(resName, defType, context.packageName)
    } catch (_: Exception) {
        0
    }
}
