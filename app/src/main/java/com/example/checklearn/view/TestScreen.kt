package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.QuestionItem
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.model.LoadingState
import com.example.checklearn.ui.theme.BlueMainColor
import com.example.checklearn.ui.theme.MyGray
import com.example.checklearn.viewmodel.CameraViewModel
import com.example.checklearn.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun TestScreen(
    taskViewModel: TaskViewModel,
    cameraViewModel: CameraViewModel
) {

    val loadingState = taskViewModel.loadingState.collectAsState()
    val scope = rememberCoroutineScope()
    val answer = taskViewModel.answer.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.generateContent(cameraViewModel.text.value.toString())
    }
    SideBarMenu { drawerState ->
        CustomScaffold(
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(MyGray)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (loadingState.value) {
                    LoadingState.Idle -> {}
                    LoadingState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = BlueMainColor)
                        }
                    }

                    LoadingState.Success -> {
                        answer.value.forEach { question ->
                            QuestionItem(question)
                        }
                    }

                    LoadingState.Error -> TODO()
                }
            }
        }
    }
}