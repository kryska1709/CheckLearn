package com.example.checklearn.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.checklearn.ui.theme.BlueMainColor
import com.example.checklearn.ui.theme.MyGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    title: String? = null,
    navigationIcon: @Composable () -> Unit,
    content : @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    title?.let {
                        Text(
                            text = it,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                    }
                },
                navigationIcon = {navigationIcon()},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MyGray)
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}