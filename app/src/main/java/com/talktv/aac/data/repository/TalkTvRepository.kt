package com.talktv.aac.data.repository

import com.talktv.aac.data.local.*
import com.talktv.aac.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class TalkTvRepository(
    private val tileDao: TileDao,
    private val pressEventDao: PressEventDao,
    private val stageDao: StageDao,
    private val settingsDao: SettingsDao
) {

    val settings: Flow<AppSettings?> = settingsDao.getSettings()

    val stages: Flow<List<Stage>> = stageDao.getAllStages()

    val allTiles: Flow<List<Tile>> = tileDao.getAllTiles()

    val recentPressEvents: Flow<List<PressEvent>> = pressEventDao.getRecentEvents(100)

    val wordFrequencies: Flow<List<WordUsageCount>> = pressEventDao.getWordUsageFrequencies()

    val totalPresses: Flow<Int> = pressEventDao.getTotalPressCount()

    val successfulRewards: Flow<Int> = pressEventDao.getSuccessfulRewardCount()

    fun getCoreTiles(maxStage: Int): Flow<List<Tile>> {
        return tileDao.getCoreTiles(maxStage)
    }

    fun getCategoryTiles(maxStage: Int): Flow<List<Tile>> {
        return tileDao.getCategoryTiles(maxStage)
    }

    fun getSubGridTiles(categoryId: String, maxStage: Int): Flow<List<Tile>> {
        return tileDao.getTilesByParentCategory(categoryId, maxStage)
    }

    suspend fun recordTilePress(
        tile: Tile,
        phraseAtPress: List<String>,
        wasRewardTriggered: Boolean
    ): PressEvent = withContext(Dispatchers.IO) {
        val event = PressEvent(
            tileId = tile.id,
            tileWord = tile.word,
            timestampMillis = System.currentTimeMillis(),
            phraseAtPress = phraseAtPress,
            wasRewardTriggered = wasRewardTriggered,
            contentUri = tile.contentUri
        )
        pressEventDao.insertEvent(event)

        // Check for automatic stage progression
        if (wasRewardTriggered) {
            checkAutoProgressThreshold()
        }

        event
    }

    private suspend fun checkAutoProgressThreshold() {
        val currentSettings = settingsDao.getSettingsSync() ?: return
        if (!currentSettings.autoUnlockEnabled) return

        val currentStageNum = currentSettings.currentStage
        if (currentStageNum >= 4) return // Max stage reached

        val currentStageObj = stageDao.getStage(currentStageNum) ?: return
        val currentRewardCount = pressEventDao.getSuccessfulRewardCount().firstOrNull() ?: 0

        if (currentRewardCount >= currentStageObj.requiredSuccessfulPresses) {
            // Unlock next stage!
            val nextStageNum = currentStageNum + 1
            stageDao.updateStageUnlock(nextStageNum, true)
            settingsDao.updateCurrentStage(nextStageNum)
        }
    }

    suspend fun setStage(stageNumber: Int) = withContext(Dispatchers.IO) {
        stageDao.unlockStagesUpTo(stageNumber)
        settingsDao.updateCurrentStage(stageNumber)
    }

    suspend fun setAutoUnlock(enabled: Boolean) = withContext(Dispatchers.IO) {
        settingsDao.updateAutoUnlock(enabled)
    }

    suspend fun updateIdleTimeout(seconds: Int) = withContext(Dispatchers.IO) {
        settingsDao.updateIdleTimeout(seconds)
    }

    suspend fun updateTtsSettings(rate: Float, pitch: Float) = withContext(Dispatchers.IO) {
        settingsDao.updateTtsSettings(rate, pitch)
    }

    suspend fun saveTile(tile: Tile) = withContext(Dispatchers.IO) {
        tileDao.insertTile(tile)
    }

    suspend fun deleteTile(tile: Tile) = withContext(Dispatchers.IO) {
        tileDao.deleteTile(tile)
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        pressEventDao.clearAllEvents()
    }
}
