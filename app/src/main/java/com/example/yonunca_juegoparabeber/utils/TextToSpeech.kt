package com.example.yonunca_juegoparabeber.utils.firebase

import android.speech.tts.TextToSpeech

fun TextToSpeech.speakOut(text: String) {
    speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
}