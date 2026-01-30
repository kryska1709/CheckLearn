package com.example.checklearn.model

import kotlinx.serialization.Serializable

@Serializable
data class TestResult(
    val date: String = "",
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val grade: Int = 0,
    val questions: List<QuestionResult> = emptyList(),
)
@Serializable
data class QuestionResult(
    val question: String = "",
    val selectedAnswer: Int? = null,
    val  correctAnswer: Int = 0
)