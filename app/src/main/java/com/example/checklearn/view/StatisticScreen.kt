package com.example.checklearn.view

import android.R
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.network.HandwriteRecognizer
import com.example.checklearn.ui.theme.MyGray
import com.example.checklearn.viewmodel.CameraViewModel

@Composable
fun StatisticScreen(
    cameraViewModel: CameraViewModel,
    onBackClick : () -> Unit
) {
    val image = cameraViewModel.image.collectAsState()
    val text = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        image.value?.let { text.value = HandwriteRecognizer().recognizer(it) }
    }
    CustomScaffold("Статистика", navigationIcon = {
        IconButton(
            onClick = {onBackClick()}
        ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
    }}) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .background(MyGray)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Log.i("ОШИБКА STATISTICsCREEN ^:(", image.value.toString())
            image.value?.let { Image(it.asImageBitmap(), contentDescription = null) }
            Text(
                text = text.value,
                color = Color.Black,
                fontSize = 20.sp,
            )
        }
    }
}