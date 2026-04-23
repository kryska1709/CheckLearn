package com.example.checklearn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.ProfileRepository
import com.example.checklearn.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val authViewModel: AuthViewModel
) : ViewModel() {
    val profile: StateFlow<UserProfile?> = authViewModel.user.flatMapLatest {
        if (it == null){
            flowOf(null)
        } else {
            repository.getUserProfile(it.uid)
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),null
    )

    fun saveUserProfile(profile: UserProfile){
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }
}