package com.example.checklearn.viewmodel

import androidx.lifecycle.ViewModel
import com.example.checklearn.model.AuthState
import com.example.checklearn.network.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {
    private val repository = AuthRepository()
    private val _authState = MutableStateFlow(AuthState.Idle)
    val authState = _authState.asStateFlow()
}