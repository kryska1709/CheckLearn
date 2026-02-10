package com.example.checklearn.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.TestRepository
import com.example.checklearn.model.HistoryUIState
import com.example.checklearn.model.TestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val testRepository: TestRepository = TestRepository(),
    private val authViewModel: AuthViewModel,
    private val profileViewModel: ProfileViewModel
): ViewModel() {
    private val _history = MutableStateFlow<List<TestResult>>(emptyList())
    val history = _history.asStateFlow()

    val uiState: StateFlow<HistoryUIState> =
        combine(authViewModel.user, profileViewModel.profile, _history){user,profile,history ->
            when{
                user==null -> HistoryUIState.Unauthorized
                profile==null -> HistoryUIState.NeedProfileInfo
                else -> HistoryUIState.ShowHistory
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryUIState.Unauthorized)

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