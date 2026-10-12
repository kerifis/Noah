package com.talktv.aac.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Global application and caregiver settings.
 */
@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey
    val id: Int = 1,
    val currentStage: Int = 1,
    val autoUnlockEnabled: Boolean = false,
    val autoUnlockThreshold: Int = 20,
    val caregiverPin: String = "1234",
    val idleTimeoutSeconds: Int = 10,
    val ttsSpeechRate: Float = 0.95f,
    val ttsPitch: Float = 1.0f,
    val useInternalPlayerFallback: Boolean = true,
    val highContrastGlow: Boolean = true,
    val tileScalePercent: Int = 108
)
