package com.example.checklearn.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel() : ViewModel() {

    private val _image = MutableStateFlow<Bitmap?>(null)
    val image = _image.asStateFlow()

    fun saveImage(image: Bitmap){
        _image.value = image
    }
}