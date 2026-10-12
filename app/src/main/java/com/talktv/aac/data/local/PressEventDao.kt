package com.talktv.aac.data.local

import androidx.room.*
import com.talktv.aac.data.model.PressEvent
import kotlinx.coroutines.flow.Flow

data class WordUsageCount(
    val tileWord: String,
    val count: Int
)

@Dao
interface PressEventDao {

    @Insert
    suspend fun insertEvent(event: PressEvent): Long

    @Query("SELECT * FROM press_events ORDER BY timestampMillis DESC LIMIT :limit")
    fun getRecentEvents(limit: Int = 100): Flow<List<PressEvent>>

    @Query("SELECT COUNT(*) FROM press_events")
    fun getTotalPressCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM press_events WHERE wasRewardTriggered = 1")
    fun getSuccessfulRewardCount(): Flow<Int>

    @Query("""
        SELECT tileWord, COUNT(*) as count 
        FROM press_events 
        GROUP BY tileWord 
        ORDER BY count DESC
    """)
    fun getWordUsageFrequencies(): Flow<List<WordUsageCount>>

    @Query("DELETE FROM press_events")
    suspend fun clearAllEvents()
}
