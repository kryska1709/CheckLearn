package com.example.checklearn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.Question
import com.example.checklearn.data.TestManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(): ViewModel() {
    private val _answer = MutableStateFlow<List<Question>>(listOf())
    val answer = _answer.asStateFlow()

    fun generateContent(
        text: String
    ){
        viewModelScope.launch {
            try {
                _answer.value = TestManager().generateTest(text)
            } catch(e: Exception){
                Log.e("lox", e.message.toString())
            }
        }
    }
}