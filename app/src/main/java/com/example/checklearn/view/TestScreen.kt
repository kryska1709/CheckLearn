package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.R
import com.example.checklearn.components.CustomButton
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.QuestionItem
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.model.LoadingState
import com.example.checklearn.navigation.LocalNavigator
import com.example.checklearn.navigation.Navigator
import com.example.checklearn.navigation.Routes
import com.example.checklearn.ui.theme.AccentColor
import com.example.checklearn.ui.theme.BlueMainColor
import com.example.checklearn.ui.theme.ContrastBlu
import com.example.checklearn.ui.theme.Gray2
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
    val showDialog = remember { mutableStateOf(false) }
    val navigator = LocalNavigator.current
    val testFinished = taskViewModel.testFinished.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.updateLoadingState(LoadingState.Loading)
        taskViewModel.generateContent(cameraViewModel.text.value.toString())
    }
    SideBarMenu { drawerState ->
        CustomScaffold(
            title = "Тест",
            navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.menu),
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Black
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(MyGray)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .padding(innerPadding),
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
                        val selected = taskViewModel.selectedAnswer.collectAsState()
                        answer.value.forEachIndexed { index, question ->
                            QuestionItem(index+1, question, selected.value.getOrNull(index), testFinished.value){optionIndex ->
                                taskViewModel.selectAnswer(index, optionIndex)
                            }
                        }
                        CustomButton(
                            color = ButtonDefaults.buttonColors(AccentColor),
                            text = if (testFinished.value)"Результаты" else "Завершить",
                            textColor = Color.White
                        ) {
                            if (testFinished.value) {
                                showDialog.value = true
                            } else {
                                taskViewModel.finishTest()
                            }
                        }
                        val result = taskViewModel.result.collectAsState()

                        if(result.value != null && showDialog.value) {
                            val grade = taskViewModel.scoreCalculation(result.value!!,answer.value.size)
                            val gradeColor = when (grade) {
                                5 -> Color(0xFF4CAF50)
                                4 -> Color(0xFF2196F3)
                                3 -> Color(0xFFFFC107)
                                else -> Color(0xFFF44336)
                            }

                            AlertDialog(
                                containerColor = Color.White,
                                onDismissRequest = { showDialog.value = false },
                                confirmButton = {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        CustomButton(
                                            text = "Нет",
                                            color = ButtonDefaults.buttonColors(Gray2),
                                            textColor = Color.White,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            showDialog.value = false
                                        }
                                        CustomButton(
                                            text = "Да",
                                            color = ButtonDefaults.buttonColors(AccentColor),
                                            textColor = Color.White,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            showDialog.value = false
                                            navigator.navigate(Routes.CAMERA)
                                        }
                                    }
                                },
                                title = {
                                    Text(
                                        text = "Ваша оценка : $grade",
                                        color = gradeColor,
                                        fontSize = 20.sp
                                    )
                                },
                                text = {
                                    Column(
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Правильных ответов",
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "${result.value!!} из ${answer.value.size}",
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "Перейти к камере",
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            )
                        }
                    }

                    LoadingState.Error -> {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Произошла ошибка",
                                color = Color.Black
                            )
                            Button(
                                colors = ButtonDefaults.buttonColors(Color.Red.copy(0.7f)),
                                onClick = {
                                    taskViewModel.updateLoadingState(LoadingState.Loading)
                                    taskViewModel.generateContent(cameraViewModel.text.value.toString())
                                }
                            ){
                                Text(
                                    text= "Попробовать ещё раз",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}