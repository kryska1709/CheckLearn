package com.example.checklearn.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.R
import com.example.checklearn.model.TestResult
import com.example.checklearn.network.getColorByGrade
import com.example.checklearn.ui.theme.ContrastBlu
import com.example.checklearn.ui.theme.ErrorAnswer
import com.example.checklearn.ui.theme.Gray3
import com.example.checklearn.ui.theme.TrueAnswer

@Composable
fun HistoryItem(
    testResult: TestResult
) {

    val isExpended = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
            .border(
                width = 2.dp,
                color = ContrastBlu,
                shape = RoundedCornerShape(15.dp)
            )
            .clickable { isExpended.value = !isExpended.value }
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${testResult.correctAnswers} / ${testResult.totalQuestions}",
                    color = Color.Black,
                    fontSize = 13.sp
                )
                val grade = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("Ваша оценка: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = getColorByGrade(testResult.grade),
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("${testResult.grade}")
                    }
                }
                Text(
                    text = grade,
                    fontSize = 16.sp
                )
                Text(
                    text = testResult.date,
                    fontSize = 12.sp,
                    color = Gray3
                )
            }
            IconButton(
                onClick = { isExpended.value = !isExpended.value }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_down),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
        AnimatedVisibility(
            visible = isExpended.value,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Ваши ответы: ",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                testResult.questions.forEach {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        val enabled = it.correctAnswer == it.selectedAnswer
                        Text(
                            text = it.question,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = if (enabled) "верно" else "неверно",
                            fontSize = 14.sp,
                            color = if(enabled) TrueAnswer else ErrorAnswer
                        )
                    }
                }
            }
        }
    }
}