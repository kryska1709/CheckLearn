package com.example.checklearn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.TestManager
import com.example.checklearn.model.LoadingState
import com.example.checklearn.model.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(): ViewModel() {
    private val _answer = MutableStateFlow<List<Question>>(listOf())
    val answer = _answer.asStateFlow()

    private  val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState = _loadingState.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<List<Int?>>(emptyList())
    val selectedAnswer = _selectedAnswers.asStateFlow()

    private val _result = MutableStateFlow<Int?>(null)
    val result = _result.asStateFlow()

    fun generateContent(
        text: String
    ){
        viewModelScope.launch {
            try {
                val questions = TestManager().generateTest(text).questions
                _answer.value = questions
                _selectedAnswers.value = List(questions.size){ null }
                _loadingState.value = LoadingState.Success
            } catch(e: Exception){
                Log.e("lox", e.message.toString())
                _loadingState.value = LoadingState.Error
            }
        }
    }

    fun selectAnswer(
        questionIndex: Int,
        optionIndex: Int
    ){
        val list = _selectedAnswers.value.toMutableList()
        list[questionIndex] =  optionIndex
        _selectedAnswers.value = list
    }

    fun finishTest(){
        val questions = _answer.value
        val answers = _selectedAnswers.value
        var correct = 0
        questions.forEachIndexed { index, question ->
            if(answers[index] == question.correctAnswer){
                correct++
            }
        }
    }

    fun updateLoadingState(loadingState: LoadingState){
        viewModelScope.launch {
            _loadingState.value = loadingState
        }
    }
}