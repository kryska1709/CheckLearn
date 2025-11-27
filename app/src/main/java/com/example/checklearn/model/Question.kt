package com.example.checklearn.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val question: String,
    val options: List<String>,
    @SerialName("correct_answer")
    val correctAnswer: Int
)