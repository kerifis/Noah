package com.talktv.aac.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.talktv.aac.data.local.TalkTvDatabase
import com.talktv.aac.data.model.Tile
import com.talktv.aac.data.model.TileAction
import com.talktv.aac.data.repository.TalkTvRepository
import com.talktv.aac.player.ContentLauncher
import com.talktv.aac.tts.TtsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
data class HomeUiState(
    val coreTiles: List<Tile> = emptyList(),
    val categoryTiles: List<Tile> = emptyList(),
    val currentPhrase: List<String> = emptyList(),
    val currentStage: Int = 1,
    val idleTimeoutSeconds: Int = 10,
    val caregiverPin: String = "1234",
    val preferInternalPlayer: Boolean = true,
    val isTtsReady: Boolean = false,
    val showPinDialog: Boolean = false
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val approvedVideoTileIds = setOf("show_bluey", "show_cocomelon", "show_cooking")
    private val repository: TalkTvRepository
    private val ttsManager: TtsManager

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var idleResetJob: Job? = null

    init {
        val database = TalkTvDatabase.getInstance(application, viewModelScope)
        repository = TalkTvRepository(
            database.tileDao(),
            database.pressEventDao(),
            database.stageDao(),
            database.settingsDao()
        )

        ttsManager = TtsManager(application) { ready ->
            _uiState.update { it.copy(isTtsReady = ready) }
        }

        viewModelScope.launch {
            repository.settings.collect { settings ->
                settings ?: return@collect
                _uiState.update {
                    it.copy(
                        currentStage = settings.currentStage,
                        idleTimeoutSeconds = settings.idleTimeoutSeconds,
                        caregiverPin = settings.caregiverPin,
                        preferInternalPlayer = settings.useInternalPlayerFallback
                    )
                }
                ttsManager.setSpeechParams(settings.ttsSpeechRate, settings.ttsPitch)
            }
        }

        viewModelScope.launch {
            _uiState.map { it.currentStage }
                .distinctUntilChanged()
                .flatMapLatest(repository::getCoreTiles)
                .collect { tiles -> _uiState.update { it.copy(coreTiles = tiles) } }
        }

        viewModelScope.launch {
            _uiState.map { it.currentStage }
                .distinctUntilChanged()
                .flatMapLatest(repository::getCategoryTiles)
                .collect { tiles -> _uiState.update { it.copy(categoryTiles = tiles) } }
        }
    }

    fun onTileSelected(tile: Tile, onOpenCategory: (String) -> Unit) {
        restartIdleTimer()
        val phrase = _uiState.value.currentPhrase + tile.word

        when (tile.action) {
            TileAction.SPEAK_ONLY -> {
                _uiState.update { it.copy(currentPhrase = phrase) }
                ttsManager.speakWord(tile.word)
                recordPress(tile, phrase, rewarded = false)
            }

            TileAction.OPEN_CATEGORY -> {
                _uiState.update { it.copy(currentPhrase = phrase) }
                ttsManager.speakWord(tile.word)
                recordPress(tile, phrase, rewarded = false)
                onOpenCategory(tile.id)
            }

            TileAction.SPEAK_AND_LAUNCH -> {
                val spokenPhrase = phrase.joinToString(" ")
                idleResetJob?.cancel()
                _uiState.update { it.copy(currentPhrase = emptyList()) }
                ttsManager.speakPhrase(spokenPhrase) {
                    if (tile.id in approvedVideoTileIds) {
                        ContentLauncher.launchReward(
                            context = getApplication(),
                            contentUri = tile.contentUri,
                            title = tile.word,
                            preferInternalPlayer = _uiState.value.preferInternalPlayer
                        )
                    }
                }
                recordPress(tile, phrase, rewarded = tile.id in approvedVideoTileIds)
            }
        }
    }

    fun speakPhrase() {
        val phrase = _uiState.value.currentPhrase
        if (phrase.isNotEmpty()) ttsManager.speakPhrase(phrase.joinToString(" "))
        restartIdleTimer()
    }

    fun clearPhrase() {
        idleResetJob?.cancel()
        _uiState.update { it.copy(currentPhrase = emptyList()) }
    }

    private fun recordPress(tile: Tile, phrase: List<String>, rewarded: Boolean) {
        viewModelScope.launch {
            repository.recordTilePress(tile, phrase, rewarded)
        }
    }

    private fun restartIdleTimer() {
        idleResetJob?.cancel()
        val timeoutSeconds = _uiState.value.idleTimeoutSeconds
        if (timeoutSeconds <= 0) return
        idleResetJob = viewModelScope.launch {
            delay(timeoutSeconds * 1000L)
            _uiState.update { it.copy(currentPhrase = emptyList()) }
        }
    }

    fun openPinDialog() = _uiState.update { it.copy(showPinDialog = true) }

    fun closePinDialog() = _uiState.update { it.copy(showPinDialog = false) }

    override fun onCleared() {
        idleResetJob?.cancel()
        ttsManager.shutdown()
        super.onCleared()
    }
}
