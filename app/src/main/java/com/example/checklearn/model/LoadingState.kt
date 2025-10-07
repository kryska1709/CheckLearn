package com.example.checklearn.model

sealed class LoadingState {
    object Idle: LoadingState()
    object Loading: LoadingState()
    object Success: LoadingState()
    object Error: LoadingState()
}