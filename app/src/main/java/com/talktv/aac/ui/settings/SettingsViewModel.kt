package com.talktv.aac.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.talktv.aac.data.local.PressEventDao
import com.talktv.aac.data.local.TalkTvDatabase
import com.talktv.aac.data.local.WordUsageCount
import com.talktv.aac.data.model.*
import com.talktv.aac.data.repository.TalkTvRepository
import com.talktv.aac.tts.TtsManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsUiState(
    val currentStage: Int = 1,
    val autoUnlockEnabled: Boolean = false,
    val idleTimeoutSeconds: Int = 10,
    val speechRate: Float = 0.95f,
    val speechPitch: Float = 1.0f,
    val useInternalPlayerFallback: Boolean = true,
    val stages: List<Stage> = emptyList(),
    val allTiles: List<Tile> = emptyList(),
    val recentPressEvents: List<PressEvent> = emptyList(),
    val wordFrequencies: List<WordUsageCount> = emptyList(),
    val totalPresses: Int = 0,
    val successfulRewards: Int = 0,
    val selectedTab: Int = 0 // 0: Stages, 1: Analytics, 2: Tiles, 3: Voice & Timer
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TalkTvRepository
    private val ttsManager: TtsManager

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        val database = TalkTvDatabase.getInstance(application, viewModelScope)
        repository = TalkTvRepository(
            database.tileDao(),
            database.pressEventDao(),
            database.stageDao(),
            database.settingsDao()
        )

        ttsManager = TtsManager(application)

        // Observe settings
        viewModelScope.launch {
            repository.settings.collect { settings ->
                if (settings != null) {
                    _uiState.update {
                        it.copy(
                            currentStage = settings.currentStage,
                            autoUnlockEnabled = settings.autoUnlockEnabled,
                            idleTimeoutSeconds = settings.idleTimeoutSeconds,
                            speechRate = settings.ttsSpeechRate,
                            speechPitch = settings.ttsPitch,
                            useInternalPlayerFallback = settings.useInternalPlayerFallback
                        )
                    }
                    ttsManager.setSpeechParams(settings.ttsSpeechRate, settings.ttsPitch)
                }
            }
        }

        // Observe stages
        viewModelScope.launch {
            repository.stages.collect { stages ->
                _uiState.update { it.copy(stages = stages) }
            }
        }

        // Observe all tiles
        viewModelScope.launch {
            repository.allTiles.collect { tiles ->
                _uiState.update { it.copy(allTiles = tiles) }
            }
        }

        // Observe analytics
        viewModelScope.launch {
            repository.recentPressEvents.collect { events ->
                _uiState.update { it.copy(recentPressEvents = events) }
            }
        }

        viewModelScope.launch {
            repository.wordFrequencies.collect { frequencies ->
                _uiState.update { it.copy(wordFrequencies = frequencies) }
            }
        }

        viewModelScope.launch {
            repository.totalPresses.collect { count ->
                _uiState.update { it.copy(totalPresses = count) }
            }
        }

        viewModelScope.launch {
            repository.successfulRewards.collect { count ->
                _uiState.update { it.copy(successfulRewards = count) }
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun setStage(stageNumber: Int) {
        viewModelScope.launch {
            repository.setStage(stageNumber)
        }
    }

    fun setAutoUnlock(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAutoUnlock(enabled)
        }
    }

    fun updateIdleTimeout(seconds: Int) {
        viewModelScope.launch {
            repository.updateIdleTimeout(seconds)
        }
    }

    fun updateSpeechRate(rate: Float) {
        val newRate = rate.coerceIn(0.5f, 2.0f)
        viewModelScope.launch {
            repository.updateTtsSettings(newRate, _uiState.value.speechPitch)
        }
    }

    fun updateSpeechPitch(pitch: Float) {
        val newPitch = pitch.coerceIn(0.5f, 2.0f)
        viewModelScope.launch {
            repository.updateTtsSettings(_uiState.value.speechRate, newPitch)
        }
    }

    fun testVoice() {
        ttsManager.speakPhrase("Hello, this is TalkTV speech test.")
    }

    fun addCustomTile(word: String, category: TileCategory, contentUri: String) {
        val newTile = Tile(
            id = "custom_${System.currentTimeMillis()}",
            word = word,
            iconRes = "ic_cat_favorites",
            category = category,
            action = if (contentUri.isNotBlank()) TileAction.SPEAK_AND_LAUNCH else TileAction.SPEAK_ONLY,
            contentUri = contentUri.ifBlank { null },
            unlockStage = _uiState.value.currentStage,
            parentCategoryId = "cat_favorites",
            orderIndex = 99
        )
        viewModelScope.launch {
            repository.saveTile(newTile)
        }
    }

    fun deleteTile(tile: Tile) {
        viewModelScope.launch {
            repository.deleteTile(tile)
        }
    }

    fun clearAnalytics() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
