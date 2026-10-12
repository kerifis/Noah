package com.talktv.aac.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Vocabulary Progression Stage definition.
 * Managed as data (Room-seeded/JSON), allowing caregivers or therapists
 * to customize progression without recompilation.
 */
@Entity(tableName = "stages")
data class Stage(
    @PrimaryKey
    val stageNumber: Int,
    val title: String,
    val description: String,
    val goal: String,
    val requiredSuccessfulPresses: Int = 20, // Threshold to suggest or auto-advance
    val isUnlocked: Boolean = false
)
