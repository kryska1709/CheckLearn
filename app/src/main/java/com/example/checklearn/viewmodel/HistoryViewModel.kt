package com.example.checklearn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.TestRepository
import com.example.checklearn.model.HistoryUIState
import com.example.checklearn.model.TestResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    private val testRepository: TestRepository = TestRepository(),
    private val authViewModel: AuthViewModel,
    private val profileViewModel: ProfileViewModel
): ViewModel() {
    val history:StateFlow<List<TestResult>> = testRepository.getTestHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),emptyList())

    val uiState: StateFlow<HistoryUIState> =
        combine(authViewModel.user, profileViewModel.profile, history){user,profile,history ->
            when{
                user==null -> HistoryUIState.Unauthorized
                profile==null -> HistoryUIState.NeedProfileInfo
                else -> HistoryUIState.ShowHistory
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryUIState.Unauthorized)

}