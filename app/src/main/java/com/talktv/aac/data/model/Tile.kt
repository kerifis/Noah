package com.talktv.aac.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single remote-selectable tile on the AAC grid.
 *
 * @param id Unique identifier (e.g. "core_want", "show_bluey")
 * @param word Label displayed below icon and spoken by TTS
 * @param iconRes Drawable resource name (e.g. "ic_tile_want", "ic_show_bluey")
 * @param category AAC classification (CORE, CARTOON, FOOD, etc.)
 * @param action SPEAK_ONLY, SPEAK_AND_LAUNCH, or OPEN_CATEGORY
 * @param contentUri Deep-link (e.g. YouTube Kids intent uri) or local video uri/identifier
 * @param unlockStage Stage at which this tile becomes accessible (1 to 4)
 * @param isCore True if permanently pinned in the top Core row
 * @param parentCategoryId Category ID this tile belongs to if in a sub-grid
 * @param orderIndex Ordering in the grid row
 * @param customColor Optional override color (e.g. Red for Stop, Green for Want)
 */
@Entity(tableName = "tiles")
data class Tile(
    @PrimaryKey
    val id: String,
    val word: String,
    val iconRes: String,
    val category: TileCategory,
    val action: TileAction,
    val contentUri: String? = null,
    val unlockStage: Int = 1,
    val isCore: Boolean = false,
    val parentCategoryId: String? = null,
    val orderIndex: Int = 0,
    val customColor: Long? = null
)
