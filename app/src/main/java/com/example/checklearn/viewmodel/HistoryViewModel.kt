package com.example.checklearn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.TestRepository
import com.example.checklearn.model.TestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val testRepository: TestRepository = TestRepository()
): ViewModel() {
    private val _history = MutableStateFlow<List<TestResult>>(emptyList())
    val history = _history.asStateFlow()

    init {
        getHistory()
    }
    fun getHistory(){
        viewModelScope.launch {
            try {
                _history.value = testRepository.getTestsHistory()
            } catch (e: Exception){
                Log.e("ErorrGetHistory", e.message.toString())
            }
        }
    }
}