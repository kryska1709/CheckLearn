package com.example.checklearn.components

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.checklearn.ui.theme.MyGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = title,
        navigationIcon = {navigationIcon()},
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        modifier = Modifier.background(MyGray).then(modifier)
    )
}
