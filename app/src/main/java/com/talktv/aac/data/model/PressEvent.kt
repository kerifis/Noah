package com.talktv.aac.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Log entry created every time a remote tile selection occurs.
 * Used for caregiver analytics, measuring vocabulary growth, streak tracking,
 * and evaluating automatic stage progression thresholds.
 */
@Entity(tableName = "press_events")
data class PressEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tileId: String,
    val tileWord: String,
    val timestampMillis: Long,
    val phraseAtPress: List<String>,
    val wasRewardTriggered: Boolean = false,
    val contentUri: String? = null
)
