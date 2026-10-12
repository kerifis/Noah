package com.talktv.aac.tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class TtsManager(
    context: Context,
    private val onInitComplete: ((Boolean) -> Unit)? = null
) : TextToSpeech.OnInitListener {

    private val tag = "TtsManager"
    private var textToSpeech: TextToSpeech? = null
    var isReady: Boolean = false
        private set

    private var speechRate: Float = 0.95f
    private var speechPitch: Float = 1.0f

    init {
        textToSpeech = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w(tag, "US Locale not supported or missing data, falling back to default")
                textToSpeech?.language = Locale.getDefault()
            }
            textToSpeech?.setSpeechRate(speechRate)
            textToSpeech?.setPitch(speechPitch)
            isReady = true
            Log.d(tag, "TextToSpeech initialized successfully")
            onInitComplete?.invoke(true)
        } else {
            Log.e(tag, "TextToSpeech initialization failed with status: $status")
            isReady = false
            onInitComplete?.invoke(false)
        }
    }

    fun setSpeechParams(rate: Float, pitch: Float) {
        this.speechRate = rate
        this.speechPitch = pitch
        textToSpeech?.setSpeechRate(rate)
        textToSpeech?.setPitch(pitch)
    }

    /**
     * Speaks a single tile word immediately, interrupting previous single words.
     */
    fun speakWord(word: String, onDone: (() -> Unit)? = null) {
        speakInternal(word, TextToSpeech.QUEUE_FLUSH, onDone)
    }

    /**
     * Speaks the completed phrase clearly before launching the reward.
     */
    fun speakPhrase(phrase: String, onDone: (() -> Unit)? = null) {
        speakInternal(phrase, TextToSpeech.QUEUE_FLUSH, onDone)
    }

    private fun speakInternal(text: String, queueMode: Int, onDone: (() -> Unit)? = null) {
        if (!isReady || textToSpeech == null) {
            Log.w(tag, "TTS not ready to speak: $text")
            onDone?.invoke()
            return
        }

        val utteranceId = "utterance_${System.currentTimeMillis()}"

        if (onDone != null) {
            textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(id: String?) {}

                override fun onDone(id: String?) {
                    if (id == utteranceId) {
                        onDone()
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onError(id: String?) {
                    if (id == utteranceId) {
                        onDone()
                    }
                }

                override fun onError(id: String?, errorCode: Int) {
                    if (id == utteranceId) {
                        onDone()
                    }
                }
            })
        }

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        textToSpeech?.speak(text, queueMode, params, utteranceId)
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isReady = false
    }
}
