package com.example.checklearn.network

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class HandwriteRecognizer {
    val recogniser = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun recognizer(
        image: Bitmap
    ): String{
        val image = InputImage.fromBitmap(image,0)
        var resultText = ""
        val result = recogniser.process(image)
            .addOnSuccessListener { resultText = it.text }
            .addOnFailureListener { resultText = "faillllllllllllllllllllllllllllll" }
        return resultText
    }
}