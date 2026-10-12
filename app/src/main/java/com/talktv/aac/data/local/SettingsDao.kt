package com.talktv.aac.data.local

import androidx.room.*
import com.talktv.aac.data.model.AppSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<AppSettings?>

    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsSync(): AppSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: AppSettings)

    @Query("UPDATE app_settings SET currentStage = :stage WHERE id = 1")
    suspend fun updateCurrentStage(stage: Int)

    @Query("UPDATE app_settings SET autoUnlockEnabled = :enabled WHERE id = 1")
    suspend fun updateAutoUnlock(enabled: Boolean)

    @Query("UPDATE app_settings SET idleTimeoutSeconds = :seconds WHERE id = 1")
    suspend fun updateIdleTimeout(seconds: Int)

    @Query("UPDATE app_settings SET ttsSpeechRate = :rate, ttsPitch = :pitch WHERE id = 1")
    suspend fun updateTtsSettings(rate: Float, pitch: Float)
}
