package com.example.checklearn.data

import androidx.compose.ui.graphics.Color

fun getColorByGrade(
    grade: Int
): Color {
    return when (grade) {
        5 -> Color(0xFF4CAF50)
        4 -> Color(0xFF2196F3)
        3 -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }
}