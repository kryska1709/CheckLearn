package com.example.checklearn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.GeminiService
import com.example.checklearn.model.BodyRequest
import com.example.checklearn.model.BodyResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(): ViewModel() {
    private val _answer = MutableStateFlow<BodyRequest?>(null)
    val answer = _answer.asStateFlow()

    fun generateContent(
        bodyResponse: BodyResponse
    ){
        viewModelScope.launch {
            try {
                _answer.value = GeminiService().generateContent(bodyResponse)
            } catch(e: Exception){
                Log.e("lox", e.message.toString())
            }
        }
    }
}