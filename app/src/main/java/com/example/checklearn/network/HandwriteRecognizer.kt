package com.example.checklearn.network

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HandwriteRecognizer {
    val recogniser = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun recognizer(
        image: Bitmap
    ): String = suspendCoroutine{ continuation ->
        val image = InputImage.fromBitmap(image,0)
        recogniser.process(image)
            .addOnSuccessListener {
                continuation.resume(it.text)
                Log.i("RECOGNIZERRRR","ВСЕ ЗаШиБиСь")
                Log.i("RECOGNIZERRRR",it.text)
            }
            .addOnFailureListener {
                continuation.resume("faillllllllllllllllllllllllllllll")
                Log.i("FAILLL RECOGNIZERRRR","ВСЕ ПЛОХО Я СДООООООООХ")
            }
    }
}