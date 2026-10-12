package com.talktv.aac.data.model

/**
 * AI-Assisted Subtitle Phrase Prediction result.
 *
 * @param fullSubtitle The expanded, natural, grammatically complete sentence
 * @param confidence Score from 0.0 to 1.0
 * @param communicativeIntent Request, Feeling, Rejection, Social, Directive
 * @param suggestedNextTiles Suggested tiles from the WordPower board to highlight on screen
 * @param alternativePhrases Other conversational ways to complete the thought
 */
data class AiPredictionResult(
    val fullSubtitle: String,
    val confidence: Float,
    val communicativeIntent: String,
    val suggestedNextTiles: List<String>,
    val alternativePhrases: List<String> = emptyList()
)
