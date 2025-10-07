package com.example.checklearn.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel() : ViewModel() {

    private val _image = MutableStateFlow<Bitmap?>(null)
    val image = _image.asStateFlow()

    private val _text = MutableStateFlow<String?>(null)
    val text = _text.asStateFlow()

    fun saveImage(image: Bitmap){
        _image.value = image
        Log.i("бульбуль", image.toString())
    }

    fun updateText(text: String){
        viewModelScope.launch {
            _text.value = text
        }
    }
}