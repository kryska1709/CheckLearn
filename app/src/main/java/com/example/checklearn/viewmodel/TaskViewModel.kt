package com.example.checklearn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.Question
import com.example.checklearn.data.TestManager
import com.example.checklearn.model.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(): ViewModel() {
    private val _answer = MutableStateFlow<List<Question>>(listOf())
    val answer = _answer.asStateFlow()

    private  val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState = _loadingState.asStateFlow()

    fun generateContent(
        text: String
    ){
        viewModelScope.launch {
            try {
                _answer.value = TestManager().generateTest(text)
                _loadingState.value = LoadingState.Success
            } catch(e: Exception){
                Log.e("lox", e.message.toString())
                _loadingState.value = LoadingState.Error
            }
        }
    }

    fun updateLoadingState(loadingState: LoadingState){
        viewModelScope.launch {
            _loadingState.value = loadingState
        }
    }
}