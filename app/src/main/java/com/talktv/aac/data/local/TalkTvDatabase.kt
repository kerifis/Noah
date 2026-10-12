package com.talktv.aac.data.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.talktv.aac.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Tile::class, PressEvent::class, Stage::class, AppSettings::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TalkTvDatabase : RoomDatabase() {

    abstract fun tileDao(): TileDao
    abstract fun pressEventDao(): PressEventDao
    abstract fun stageDao(): StageDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: TalkTvDatabase? = null

        fun getInstance(context: Context, coroutineScope: CoroutineScope): TalkTvDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TalkTvDatabase::class.java,
                    "talktv_aac_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(coroutineScope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database)
                    }
                }
            }
        }

        suspend fun populateDatabase(database: TalkTvDatabase) {
            val settingsDao = database.settingsDao()
            val stageDao = database.stageDao()
            val tileDao = database.tileDao()

            settingsDao.insertOrUpdateSettings(InitialDataSeeder.defaultSettings)
            stageDao.insertStages(InitialDataSeeder.initialStages)
            tileDao.insertTiles(InitialDataSeeder.getInitialTiles())
        }
    }
}
