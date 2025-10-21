package com.example.checklearn.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.components.CustomButton
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.model.LoadingState
import com.example.checklearn.navigation.LocalNavigator
import com.example.checklearn.navigation.Routes
import com.example.checklearn.network.HandwriteRecognizer
import com.example.checklearn.ui.theme.BlueMainColor
import com.example.checklearn.ui.theme.MyGray
import com.example.checklearn.viewmodel.CameraViewModel
import com.example.checklearn.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun StatisticScreen(
    cameraViewModel: CameraViewModel,
    taskViewModel: TaskViewModel,
) {
    val navigator = LocalNavigator.current
    val image = cameraViewModel.image.collectAsState()
    val text = cameraViewModel.text.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        image.value?.let { cameraViewModel.updateText(HandwriteRecognizer().recognizer(it))   }
    }
    SideBarMenu {  drawerState ->
    CustomScaffold(title = "Задания",
        navigationIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = { navigator.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }

            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .background(MyGray)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            image.value?.let { Image(bitmap = it.asImageBitmap(), contentDescription = null) }
            Text(
                text = text.value ?: "",
                color = Color.Black,
                fontSize = 20.sp,
            )
            CustomButton(
                modifier = Modifier.padding(8.dp),
                color = ButtonDefaults.buttonColors(BlueMainColor),
                text = "Сгенерировать тест",
                textColor = Color.White
            ) {
                taskViewModel.updateLoadingState(LoadingState.Loading)
                navigator.navigate(Routes.TASKS)
            }
        }
    }
}
}