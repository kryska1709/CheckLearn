package com.example.checklearn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.TestManager
import com.example.checklearn.data.TestRepository
import com.example.checklearn.model.LoadingState
import com.example.checklearn.model.Question
import com.example.checklearn.model.QuestionResult
import com.example.checklearn.model.TestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TestRepository = TestRepository()
): ViewModel() {
    private val _answer = MutableStateFlow<List<Question>>(listOf())
    val answer = _answer.asStateFlow()

    private  val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val loadingState = _loadingState.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<List<Int?>>(emptyList())
    val selectedAnswer = _selectedAnswers.asStateFlow()

    private val _result = MutableStateFlow<Int?>(null)
    val result = _result.asStateFlow()

    private val _testFinished = MutableStateFlow<Boolean>(false)
    val testFinished = _testFinished.asStateFlow()
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
        _result.value = correct
        _testFinished.value = true
    }

    fun updateLoadingState(loadingState: LoadingState){
        viewModelScope.launch {
            _loadingState.value = loadingState
        }
    }

    fun scoreCalculation(
        correctAnswer: Int,
        allAnswer: Int
    ) : Int{
        val result = (correctAnswer.toFloat()/allAnswer*100).toInt()
        return when{
            result < 50 -> 2
            result in 50..<70 -> 3
            result in 70..85 -> 4
            else -> 5
        }
    }

    fun saveTest(){
        viewModelScope.launch {
            val questions = _answer.value
            val selected = _selectedAnswers.value
            val correct = _result.value ?: return@launch
            val testResult = TestResult(
                totalQuestions = questions.size,
                correctAnswers = correct,
                grade = scoreCalculation(correct, questions.size),
                questions = questions.mapIndexed { index, question ->
                    QuestionResult(
                        question = question.question,
                        selectedAnswer = selected[index],
                        correctAnswer = question.correctAnswer
                    )
                }
            )
            try {
                repository.saveTest(testResult)
            } catch (e: Exception){
                Log.e("error history",e.toString())
            }
        }
    }
}