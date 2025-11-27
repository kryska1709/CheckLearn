package com.example.checklearn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checklearn.model.AuthState
import com.example.checklearn.network.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val repository = AuthRepository()

    private val _authState = MutableStateFlow(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _user = MutableStateFlow(repository.getCurrentUser())
    val user = _user.asStateFlow()

    fun updateUser(user: FirebaseUser?) {
        viewModelScope.launch {
            _user.value = user
        }
    }
}