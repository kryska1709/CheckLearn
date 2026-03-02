package com.example.checklearn.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checklearn.ui.theme.AccentCorrect
import com.example.checklearn.ui.theme.BlueMainColor
import com.example.checklearn.ui.theme.ContrastBlu

@Composable
fun CustomTextField(
    value: String,
    label: String,
    placeholder:String = "",
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = AccentCorrect
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = BlueMainColor
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ContrastBlu,
            unfocusedTextColor = BlueMainColor,
            focusedBorderColor = ContrastBlu,
            unfocusedBorderColor = BlueMainColor
        )
    )
}