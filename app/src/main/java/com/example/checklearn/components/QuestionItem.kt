package com.example.checklearn.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.model.Question
import com.example.checklearn.ui.theme.BlueMainColor

@Composable
fun QuestionItem(
    index: Int,
    question: Question,
    selected: Int?,
    onSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(BlueMainColor, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.Start
    ) {
            Text(
                text = "${index}. " + question.question,
                fontSize = 22.sp,
                color = Color.White
            )
            question.options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable{onSelect(index)},
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                   RadioButton(
                       selected = selected == index,
                       onClick = {
                           onSelect(index)
                       },
                       colors = RadioButtonDefaults.colors(unselectedColor = Color.White, selectedColor = Color.White)
                   )
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
    }
}