package com.example.checklearn.model

sealed class StudentResultState {
    data object Idle: StudentResultState()
    data object Loading: StudentResultState()
    data class Success(
        val result: List<StudentResult>
    ): StudentResultState()
    data class Error(
        val message: String
    ): StudentResultState()
}