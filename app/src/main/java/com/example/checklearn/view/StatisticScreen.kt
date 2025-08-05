package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.ui.theme.MyGray

@Composable
fun StatisticScreen(
    onBackClick : () -> Unit
) {
    CustomScaffold("Статистика", navigationIcon = {
        IconButton(
            onClick = {onBackClick()}
        ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
    }}) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .background(MyGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "STOOONKS",
                fontSize = 50.sp,
            )
        }
    }
}