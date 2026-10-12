package com.talktv.aac.data.model

/**
 * Action triggered when a tile is selected via D-Pad:
 * - SPEAK_ONLY: Appends word to PhraseBar and speaks the word via TTS.
 * - SPEAK_AND_LAUNCH: Completes phrase, speaks full phrase, and immediately launches content.
 * - OPEN_CATEGORY: Navigates into a sub-grid of specific content items for that category.
 */
enum class TileAction {
    SPEAK_ONLY,
    SPEAK_AND_LAUNCH,
    OPEN_CATEGORY
}
