package com.example.checklearn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.data.ProfileRepository
import com.example.checklearn.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile = _profile.asStateFlow()

    fun saveUserProfile(profile: UserProfile){
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    fun getUserProfile(){
        viewModelScope.launch {
            _profile.value = repository.getUserProfile()
        }
    }
}