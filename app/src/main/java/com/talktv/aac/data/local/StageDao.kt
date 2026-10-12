package com.talktv.aac.data.local

import androidx.room.*
import com.talktv.aac.data.model.Stage
import kotlinx.coroutines.flow.Flow

@Dao
interface StageDao {

    @Query("SELECT * FROM stages ORDER BY stageNumber ASC")
    fun getAllStages(): Flow<List<Stage>>

    @Query("SELECT * FROM stages WHERE stageNumber = :stageNumber LIMIT 1")
    suspend fun getStage(stageNumber: Int): Stage?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStages(stages: List<Stage>)

    @Query("UPDATE stages SET isUnlocked = :unlocked WHERE stageNumber = :stageNumber")
    suspend fun updateStageUnlock(stageNumber: Int, unlocked: Boolean)

    @Query("UPDATE stages SET isUnlocked = 1 WHERE stageNumber <= :currentStage")
    suspend fun unlockStagesUpTo(currentStage: Int)
}
