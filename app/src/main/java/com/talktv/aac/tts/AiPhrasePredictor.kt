package com.talktv.aac.tts

import com.talktv.aac.data.model.AiPredictionResult

/**
 * Intelligent Assisted-AI AAC Subtitle Phrase Predictor.
 *
 * Expands sparse 1-to-2 tile D-pad remote selections from a child into
 * complete, natural, and expressive conversational subtitles displayed on the TV.
 * Built directly around WordPower™ 60 Basic (PRC-Saltillo) communicative syntax.
 */
object AiPhrasePredictor {

    fun predictSubtitle(selectedWords: List<String>, activeFocusTile: String? = null): AiPredictionResult {
        if (selectedWords.isEmpty()) {
            return if (!activeFocusTile.isNullOrBlank()) {
                predictFromSingleTile(activeFocusTile)
            } else {
                AiPredictionResult(
                    fullSubtitle = "Use TV remote to choose what you want...",
                    confidence = 0.5f,
                    communicativeIntent = "Prompt",
                    suggestedNextTiles = listOf("want", "I", "stop", "feel", "watch", "eat"),
                    alternativePhrases = emptyList()
                )
            }
        }

        val normalized = selectedWords.map { it.lowercase().trim() }
        val lastWord = normalized.last()
        val textJoined = normalized.joinToString(" ")

        // 1. Direct Content Show Matches
        if (normalized.any { it.contains("bluey") }) {
            return AiPredictionResult(
                fullSubtitle = "I want to watch Bluey on TV please!",
                confidence = 0.98f,
                communicativeIntent = "Content Request",
                suggestedNextTiles = listOf("more", "again", "stop"),
                alternativePhrases = listOf(
                    "Can we put on Bluey?",
                    "I want to watch more Bluey episodes!"
                )
            )
        }

        if (normalized.any { it.contains("cocomelon") }) {
            return AiPredictionResult(
                fullSubtitle = "I want to watch CoComelon songs on TV please!",
                confidence = 0.98f,
                communicativeIntent = "Content Request",
                suggestedNextTiles = listOf("more", "again", "stop"),
                alternativePhrases = listOf(
                    "Can we listen to CoComelon nursery rhymes?",
                    "Play CoComelon again please!"
                )
            )
        }

        if (normalized.any { it.contains("cooking") || it.contains("pancake") }) {
            return AiPredictionResult(
                fullSubtitle = "I want to watch the cooking show making yummy food!",
                confidence = 0.96f,
                communicativeIntent = "Content Request",
                suggestedNextTiles = listOf("eat", "more", "like"),
                alternativePhrases = listOf(
                    "Can we watch pancakes being made?",
                    "I want to see the baking show!"
                )
            )
        }

        if (normalized.any { it.contains("train") || it.contains("trains") }) {
            return AiPredictionResult(
                fullSubtitle = "I want to watch big steam trains on the TV please!",
                confidence = 0.97f,
                communicativeIntent = "Content Request",
                suggestedNextTiles = listOf("more", "again", "fast"),
                alternativePhrases = listOf(
                    "Let's watch train engines roll on the tracks!",
                    "Play more trains please!"
                )
            )
        }

        if (normalized.any { it.contains("animal") || it.contains("animals") }) {
            return AiPredictionResult(
                fullSubtitle = "I want to see wild animals and nature adventures!",
                confidence = 0.95f,
                communicativeIntent = "Content Request",
                suggestedNextTiles = listOf("more", "cool", "like"),
                alternativePhrases = listOf(
                    "Can we watch animals on TV?",
                    "Show me more safari animals please!"
                )
            )
        }

        if (normalized.any { it.contains("nursery") || it.contains("music") || it.contains("song") }) {
            return AiPredictionResult(
                fullSubtitle = "I want to listen and sing along to kids songs please!",
                confidence = 0.95f,
                communicativeIntent = "Music Request",
                suggestedNextTiles = listOf("more", "again", "play"),
                alternativePhrases = listOf(
                    "Can we play twinkle nursery songs?",
                    "I want more music videos!"
                )
            )
        }

        // 2. WordPower 60 Core Verb & Pronoun Constructions
        if (textJoined.contains("want") || textJoined.contains("i want")) {
            return AiPredictionResult(
                fullSubtitle = "I want to choose something good on TV please.",
                confidence = 0.92f,
                communicativeIntent = "Core Request",
                suggestedNextTiles = listOf("Bluey", "Cartoons", "CoComelon", "Cooking Show", "more", "eat"),
                alternativePhrases = listOf(
                    "Can I have this please?",
                    "I want more of this."
                )
            )
        }

        if (textJoined.contains("stop") || textJoined.contains("no")) {
            return AiPredictionResult(
                fullSubtitle = "I want to stop now, I am all done watching TV.",
                confidence = 0.96f,
                communicativeIntent = "Rejection / Cessation",
                suggestedNextTiles = listOf("all done", "off", "go", "rest"),
                alternativePhrases = listOf(
                    "Please turn off this video.",
                    "No thank you, I want to do something else."
                )
            )
        }

        if (textJoined.contains("feel") || textJoined.contains("happy") || textJoined.contains("sad") || textJoined.contains("tired")) {
            val feelingWord = when {
                textJoined.contains("tired") -> "tired and ready to relax"
                textJoined.contains("sad") -> "sad and need some comfort"
                textJoined.contains("happy") -> "happy watching this"
                textJoined.contains("hurt") -> "hurt and need a helper"
                else -> "comfortable right now"
            }
            return AiPredictionResult(
                fullSubtitle = "I feel $feelingWord with you.",
                confidence = 0.93f,
                communicativeIntent = "Emotional Expression",
                suggestedNextTiles = listOf("help", "rest", "hug", "watch"),
                alternativePhrases = listOf(
                    "I need a quiet break.",
                    "Watching this makes me feel better."
                )
            )
        }

        if (textJoined.contains("help")) {
            return AiPredictionResult(
                fullSubtitle = "Can you please help me with the TV remote?",
                confidence = 0.97f,
                communicativeIntent = "Assistance Request",
                suggestedNextTiles = listOf("open", "find", "turn", "volume"),
                alternativePhrases = listOf(
                    "I need help finding my favorite show.",
                    "Please help me fix the screen."
                )
            )
        }

        if (textJoined.contains("eat") || textJoined.contains("drink")) {
            val item = if (textJoined.contains("drink")) "a drink of water" else "a snack to eat"
            return AiPredictionResult(
                fullSubtitle = "I would like $item while watching TV please.",
                confidence = 0.94f,
                communicativeIntent = "Physical Need",
                suggestedNextTiles = listOf("snack", "water", "more", "please"),
                alternativePhrases = listOf(
                    "Can I have a snack please?",
                    "I am thirsty and need a drink."
                )
            )
        }

        if (textJoined.contains("more") || textJoined.contains("again")) {
            return AiPredictionResult(
                fullSubtitle = "I want to watch that one more time please!",
                confidence = 0.94f,
                communicativeIntent = "Repetition Request",
                suggestedNextTiles = listOf("again", "Bluey", "CoComelon", "play"),
                alternativePhrases = listOf(
                    "Play it again from the beginning!",
                    "More of this video please."
                )
            )
        }

        if (textJoined.contains("like")) {
            return AiPredictionResult(
                fullSubtitle = "I really like this show, it is so fun!",
                confidence = 0.91f,
                communicativeIntent = "Social Sharing",
                suggestedNextTiles = listOf("more", "good", "happy", "watch"),
                alternativePhrases = listOf(
                    "This is my favorite!",
                    "I love watching this with you."
                )
            )
        }

        // Default Intelligent Fallback
        val wordsFormatted = selectedWords.joinToString(" ")
        return AiPredictionResult(
            fullSubtitle = "I am saying: \"$wordsFormatted\" on the TV screen.",
            confidence = 0.85f,
            communicativeIntent = "Expressive Statement",
            suggestedNextTiles = listOf("want", "more", "stop", "please"),
            alternativePhrases = listOf("I want $wordsFormatted", "Look at $wordsFormatted")
        )
    }

    private fun predictFromSingleTile(tileWord: String): AiPredictionResult {
        return predictSubtitle(listOf(tileWord))
    }
}
