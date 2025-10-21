package com.example.checklearn.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.data.Question
import com.example.checklearn.ui.theme.BlueMainColor

@Composable
fun QuestionItem(
    question: Question
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .background(BlueMainColor)
    ) {
        Column {
            Text(
                text = question.question,
                fontSize = 26.sp,
                color = Color.White
            )
            question.options.forEach {
                Row() {
                    Box(modifier = Modifier.border(width = 1.dp, color = Color.Black, shape = RectangleShape))
                    Text(
                        text = it
                    )
                }
            }
        }
    }
}