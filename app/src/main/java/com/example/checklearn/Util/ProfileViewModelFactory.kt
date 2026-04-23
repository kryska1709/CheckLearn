package com.example.checklearn.Util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.checklearn.viewmodel.AuthViewModel
import com.example.checklearn.viewmodel.ProfileViewModel

class ProfileViewModelFactory (
    private val authViewModel: AuthViewModel,
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(authViewModel = authViewModel) as T
            }
            throw IllegalArgumentException("pepets")
        }
    }