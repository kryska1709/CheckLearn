package com.example.checklearn.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    color: ButtonColors,
    text: String,
    textColor: Color,
    enabled : Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    action: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        colors = color,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        onClick = {action()}
    ) {
        icon?.let {
            icon()
            Spacer(Modifier.width(5.dp))
        }
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp
        )
    }
}