package com.talktv.aac.data.local

import com.talktv.aac.data.model.*

object InitialDataSeeder {

    val defaultSettings = AppSettings(
        id = 1,
        currentStage = 1,
        autoUnlockEnabled = true,
        autoUnlockThreshold = 15,
        caregiverPin = "1234",
        idleTimeoutSeconds = 10,
        ttsSpeechRate = 0.95f,
        ttsPitch = 1.0f,
        useInternalPlayerFallback = true
    )

    val initialStages = listOf(
        Stage(
            stageNumber = 1,
            title = "Stage 1: Single-Tile Requests",
            description = "Direct requesting: want, stop, and favorite show tiles.",
            goal = "Single-tile request -> immediate rewarded playback",
            requiredSuccessfulPresses = 15,
            isUnlocked = true
        ),
        Stage(
            stageNumber = 2,
            title = "Stage 2: Two-Tile Combinations",
            description = "Build sentences: 'I want', 'more', 'again'.",
            goal = "Two-tile phrases leading to intentional content launch",
            requiredSuccessfulPresses = 25,
            isUnlocked = false
        ),
        Stage(
            stageNumber = 3,
            title = "Stage 3: Feelings & Choices",
            description = "Expanded core words: like, no, help, feel.",
            goal = "Requesting + basic emotional expression and self-advocacy",
            requiredSuccessfulPresses = 40,
            isUnlocked = false
        ),
        Stage(
            stageNumber = 4,
            title = "Stage 4: Full Communication Board",
            description = "Approaches full WordPower board with People, Places, Describing rows.",
            goal = "Comprehensive core + fringe vocabulary",
            requiredSuccessfulPresses = 60,
            isUnlocked = false
        )
    )

    fun getInitialTiles(): List<Tile> {
        val tiles = mutableListOf<Tile>()

        // ==========================================
        // CORE ROW (Always visible at top of Home Grid)
        // ==========================================

        // Stage 1 Core Tiles
        tiles.add(
            Tile(
                id = "core_want",
                word = "want",
                iconRes = "ic_3d_want",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 1,
                isCore = true,
                orderIndex = 1,
                customColor = 0xFF4CAF50 // Green (Verb/Action)
            )
        )
        tiles.add(
            Tile(
                id = "core_stop",
                word = "stop",
                iconRes = "ic_3d_stop",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 1,
                isCore = true,
                orderIndex = 2,
                customColor = 0xFFE53935 // Red (Negation/Stop)
            )
        )

        // Stage 2 Core Tiles
        tiles.add(
            Tile(
                id = "core_i",
                word = "I",
                iconRes = "ic_3d_i",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 2,
                isCore = true,
                orderIndex = 0, // Appears first when unlocked
                customColor = 0xFFFFCA28 // Yellow (Pronoun)
            )
        )
        tiles.add(
            Tile(
                id = "core_more",
                word = "more",
                iconRes = "ic_3d_more",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 2,
                isCore = true,
                orderIndex = 3,
                customColor = 0xFF4CAF50
            )
        )
        tiles.add(
            Tile(
                id = "core_again",
                word = "again",
                iconRes = "ic_3d_again",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 2,
                isCore = true,
                orderIndex = 4,
                customColor = 0xFF4CAF50
            )
        )

        // Stage 3 Core Tiles
        tiles.add(
            Tile(
                id = "core_like",
                word = "like",
                iconRes = "ic_3d_like",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 3,
                isCore = true,
                orderIndex = 5,
                customColor = 0xFFE91E63
            )
        )
        tiles.add(
            Tile(
                id = "core_no",
                word = "no",
                iconRes = "ic_3d_no",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 3,
                isCore = true,
                orderIndex = 6,
                customColor = 0xFFEF5350
            )
        )
        tiles.add(
            Tile(
                id = "core_help",
                word = "help",
                iconRes = "ic_3d_help",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 3,
                isCore = true,
                orderIndex = 7,
                customColor = 0xFFFF9800
            )
        )
        tiles.add(
            Tile(
                id = "core_feel",
                word = "feel",
                iconRes = "ic_3d_feel",
                category = TileCategory.CORE,
                action = TileAction.SPEAK_ONLY,
                unlockStage = 3,
                isCore = true,
                orderIndex = 8,
                customColor = 0xFF29B6F6
            )
        )

        // ==========================================
        // CATEGORY TILES (Row below Core in Home Grid)
        // ==========================================
        tiles.add(
            Tile(
                id = "cat_cartoons",
                word = "Cartoons",
                iconRes = "ic_cat_cartoons",
                category = TileCategory.CARTOON,
                action = TileAction.OPEN_CATEGORY,
                unlockStage = 1,
                isCore = false,
                orderIndex = 1
            )
        )
        tiles.add(
            Tile(
                id = "cat_food",
                word = "Food Shows",
                iconRes = "ic_cat_food",
                category = TileCategory.FOOD,
                action = TileAction.OPEN_CATEGORY,
                unlockStage = 1,
                isCore = false,
                orderIndex = 2
            )
        )
        tiles.add(
            Tile(
                id = "cat_music",
                word = "Music",
                iconRes = "ic_cat_music",
                category = TileCategory.MUSIC,
                action = TileAction.OPEN_CATEGORY,
                unlockStage = 1,
                isCore = false,
                orderIndex = 3
            )
        )
        tiles.add(
            Tile(
                id = "cat_favorites",
                word = "Favorites",
                iconRes = "ic_cat_favorites",
                category = TileCategory.FAVORITE,
                action = TileAction.OPEN_CATEGORY,
                unlockStage = 1,
                isCore = false,
                orderIndex = 4
            )
        )

        // Stage 4 Categories
        tiles.add(
            Tile(
                id = "cat_people",
                word = "People",
                iconRes = "ic_cat_people",
                category = TileCategory.PEOPLE,
                action = TileAction.OPEN_CATEGORY,
                unlockStage = 4,
                isCore = false,
                orderIndex = 5
            )
        )
        tiles.add(
            Tile(
                id = "cat_places",
                word = "Places",
                iconRes = "ic_cat_places",
                category = TileCategory.PLACES,
                action = TileAction.OPEN_CATEGORY,
                unlockStage = 4,
                isCore = false,
                orderIndex = 6
            )
        )
        tiles.add(
            Tile(
                id = "cat_describe",
                word = "Describe",
                iconRes = "ic_cat_describe",
                category = TileCategory.DESCRIBE,
                action = TileAction.OPEN_CATEGORY,
                unlockStage = 4,
                isCore = false,
                orderIndex = 7
            )
        )

        // ==========================================
        // SUB-GRID CONTENT SHOW TILES (Fringe Vocabulary & Real Video Rewards)
        // ==========================================

        // Cartoons Sub-Grid
        tiles.add(
            Tile(
                id = "show_bluey",
                word = "Bluey",
                iconRes = "ic_show_bluey",
                category = TileCategory.CARTOON,
                action = TileAction.SPEAK_AND_LAUNCH,
                contentUri = "https://www.youtube.com/results?search_query=bluey+full+episodes",
                unlockStage = 1,
                parentCategoryId = "cat_cartoons",
                orderIndex = 1
            )
        )
        tiles.add(
            Tile(
                id = "show_cocomelon",
                word = "CoComelon",
                iconRes = "ic_show_cocomelon",
                category = TileCategory.CARTOON,
                action = TileAction.SPEAK_AND_LAUNCH,
                contentUri = "https://www.youtube.com/results?search_query=cocomelon+nursery+rhymes",
                unlockStage = 1,
                parentCategoryId = "cat_cartoons",
                orderIndex = 2
            )
        )

        // Food Shows Sub-Grid
        tiles.add(
            Tile(
                id = "show_cooking",
                word = "Cooking Show",
                iconRes = "ic_show_cooking",
                category = TileCategory.FOOD,
                action = TileAction.SPEAK_AND_LAUNCH,
                contentUri = "https://www.youtube.com/results?search_query=kid+pancake+cooking+show",
                unlockStage = 1,
                parentCategoryId = "cat_food",
                orderIndex = 1
            )
        )

        // Music Sub-Grid
        tiles.add(
            Tile(
                id = "show_nursery",
                word = "Nursery Songs",
                iconRes = "ic_show_nursery",
                category = TileCategory.MUSIC,
                action = TileAction.SPEAK_AND_LAUNCH,
                contentUri = "https://www.youtube.com/results?search_query=super+simple+songs",
                unlockStage = 1,
                parentCategoryId = "cat_music",
                orderIndex = 1
            )
        )

        // Favorites Sub-Grid
        tiles.add(
            Tile(
                id = "show_animals",
                word = "Animals",
                iconRes = "ic_show_animals",
                category = TileCategory.FAVORITE,
                action = TileAction.SPEAK_AND_LAUNCH,
                contentUri = "https://www.youtube.com/results?search_query=national+geographic+kids+animals",
                unlockStage = 1,
                parentCategoryId = "cat_favorites",
                orderIndex = 1
            )
        )
        tiles.add(
            Tile(
                id = "show_trains",
                word = "Trains",
                iconRes = "ic_show_trains",
                category = TileCategory.FAVORITE,
                action = TileAction.SPEAK_AND_LAUNCH,
                contentUri = "https://www.youtube.com/results?search_query=steam+trains+for+children",
                unlockStage = 2,
                parentCategoryId = "cat_favorites",
                orderIndex = 2
            )
        )

        // Stage 4 People Sub-Grid
        val peopleWords = listOf("Mom", "Dad", "Teacher", "Friend")
        peopleWords.forEachIndexed { idx, word ->
            tiles.add(
                Tile(
                    id = "people_${word.lowercase()}",
                    word = word,
                    iconRes = "ic_cat_people",
                    category = TileCategory.PEOPLE,
                    action = TileAction.SPEAK_ONLY,
                    unlockStage = 4,
                    parentCategoryId = "cat_people",
                    orderIndex = idx + 1
                )
            )
        }

        // Stage 4 Places Sub-Grid
        val placesWords = listOf("Home", "Park", "School", "Outside")
        placesWords.forEachIndexed { idx, word ->
            tiles.add(
                Tile(
                    id = "places_${word.lowercase()}",
                    word = word,
                    iconRes = "ic_cat_places",
                    category = TileCategory.PLACES,
                    action = TileAction.SPEAK_ONLY,
                    unlockStage = 4,
                    parentCategoryId = "cat_places",
                    orderIndex = idx + 1
                )
            )
        }

        // Stage 4 Describe Sub-Grid
        val describeWords = listOf("Happy", "Fun", "Loud", "Quiet", "Big", "Silly")
        describeWords.forEachIndexed { idx, word ->
            tiles.add(
                Tile(
                    id = "describe_${word.lowercase()}",
                    word = word,
                    iconRes = "ic_cat_describe",
                    category = TileCategory.DESCRIBE,
                    action = TileAction.SPEAK_ONLY,
                    unlockStage = 4,
                    parentCategoryId = "cat_describe",
                    orderIndex = idx + 1
                )
            )
        }

        return tiles
    }
}
