package com.talktv.aac.data.local

import androidx.room.*
import com.talktv.aac.data.model.Tile
import kotlinx.coroutines.flow.Flow

@Dao
interface TileDao {

    @Query("SELECT * FROM tiles WHERE isCore = 1 AND unlockStage <= :maxStage ORDER BY orderIndex ASC")
    fun getCoreTiles(maxStage: Int): Flow<List<Tile>>

    @Query("SELECT * FROM tiles WHERE isCore = 0 AND parentCategoryId IS NULL AND unlockStage <= :maxStage ORDER BY orderIndex ASC")
    fun getCategoryTiles(maxStage: Int): Flow<List<Tile>>

    @Query("SELECT * FROM tiles WHERE parentCategoryId = :categoryId AND unlockStage <= :maxStage ORDER BY orderIndex ASC")
    fun getTilesByParentCategory(categoryId: String, maxStage: Int): Flow<List<Tile>>

    @Query("SELECT * FROM tiles ORDER BY unlockStage ASC, orderIndex ASC")
    fun getAllTiles(): Flow<List<Tile>>

    @Query("SELECT * FROM tiles WHERE id = :id LIMIT 1")
    suspend fun getTileById(id: String): Tile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiles(tiles: List<Tile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTile(tile: Tile)

    @Update
    suspend fun updateTile(tile: Tile)

    @Delete
    suspend fun deleteTile(tile: Tile)

    @Query("DELETE FROM tiles WHERE id = :id")
    suspend fun deleteTileById(id: String)
}
