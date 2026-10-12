package com.talktv.aac.data.model

import androidx.compose.ui.graphics.Color

/**
 * Tile categories following AAC standard conventions (e.g. Fitzgerald Key / Goossens').
 * Color-coding helps children generalize vocabulary across AAC systems:
 * - CORE / PRONOUN: Yellow
 * - VERB / ACTION: Green
 * - DESCRIPTOR: Blue
 * - NOUN / CATEGORY: Orange
 * - SOCIAL: Purple
 * - STOP / NEGATION: Red
 */
enum class TileCategory(
    val displayName: String,
    val primaryColor: Long,
    val backgroundColor: Long,
    val iconResourceName: String
) {
    CORE(
        displayName = "Core Words",
        primaryColor = 0xFFFFCA28, // Gold / Yellow
        backgroundColor = 0xFF2A2410,
        iconResourceName = "ic_tile_want"
    ),
    CARTOON(
        displayName = "Cartoons",
        primaryColor = 0xFFFFA726, // Orange
        backgroundColor = 0xFF301E0A,
        iconResourceName = "ic_cat_cartoons"
    ),
    FOOD(
        displayName = "Food Shows",
        primaryColor = 0xFFFF9800, // Deep Orange
        backgroundColor = 0xFF331E08,
        iconResourceName = "ic_cat_food"
    ),
    MUSIC(
        displayName = "Music & Songs",
        primaryColor = 0xFFAB47BC, // Purple
        backgroundColor = 0xFF24102C,
        iconResourceName = "ic_cat_music"
    ),
    FAVORITE(
        displayName = "Favorites",
        primaryColor = 0xFFFFD600, // Bright Gold
        backgroundColor = 0xFF302808,
        iconResourceName = "ic_cat_favorites"
    ),
    PEOPLE(
        displayName = "People",
        primaryColor = 0xFFFFEE58, // Light Yellow
        backgroundColor = 0xFF2B280E,
        iconResourceName = "ic_cat_people"
    ),
    PLACES(
        displayName = "Places",
        primaryColor = 0xFFFFB74D, // Soft Orange
        backgroundColor = 0xFF311F0D,
        iconResourceName = "ic_cat_places"
    ),
    DESCRIBE(
        displayName = "Describe",
        primaryColor = 0xFF29B6F6, // Sky Blue
        backgroundColor = 0xFF0D2336,
        iconResourceName = "ic_cat_describe"
    );

    val composeColor: Color
        get() = Color(primaryColor)

    val composeBgColor: Color
        get() = Color(backgroundColor)
}
