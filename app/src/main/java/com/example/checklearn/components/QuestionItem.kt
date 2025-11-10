package com.example.checklearn.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.data.Question
import com.example.checklearn.ui.theme.BlueMainColor

@Composable
fun QuestionItem(
    number: Int,
    question: Question
) {
    val selectedOption = remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(BlueMainColor, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.Start
    ) {
            Text(
                text = "$number. " + question.question,
                fontSize = 22.sp,
                color = Color.White
            )
            question.options.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                   RadioButton(
                       selected = selectedOption.value == it,
                       onClick = {
                           selectedOption.value = it
                       },
                       colors = RadioButtonDefaults.colors(unselectedColor = Color.White, selectedColor = Color.White)
                   )
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
    }
}