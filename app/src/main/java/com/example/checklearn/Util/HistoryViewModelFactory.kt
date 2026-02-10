package com.example.checklearn.Util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.checklearn.viewmodel.AuthViewModel
import com.example.checklearn.viewmodel.HistoryViewModel
import com.example.checklearn.viewmodel.ProfileViewModel

class HistoryViewModelFactory(
    private val authViewModel: AuthViewModel,
    private val profileViewModel: ProfileViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(authViewModel = authViewModel, profileViewModel = profileViewModel) as T
        }
        throw IllegalArgumentException("pepets")
    }
}