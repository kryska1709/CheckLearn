package com.example.checklearn.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.checklearn.R
import com.example.checklearn.components.CardTeacher
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.CustomTextField
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.model.StudentResultState
import com.example.checklearn.ui.theme.ContrastBlu
import com.example.checklearn.ui.theme.MyGray
import com.example.checklearn.viewmodel.ProfileViewModel
import com.example.checklearn.viewmodel.TeacherViewModel
import kotlinx.coroutines.launch

@Composable
fun TeacherScreen(
    teacherViewModel: TeacherViewModel,
    profileViewModel: ProfileViewModel
) {
    val scope = rememberCoroutineScope()
    val resultState = teacherViewModel.resultState.collectAsState()
    val nameGroup = remember {mutableStateOf("")}// для поиска по группе
    val user = profileViewModel.profile.collectAsState()
    LaunchedEffect(
        Unit
    ) {
        user.value?.teacherClassroom?.first()?.let { teacherViewModel.searchByClassroom(it) }
    }
    SideBarMenu { drawerState ->
        CustomScaffold(
            title = "Кабинет Учителя",
            navigationIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
                            tint = Color.Black
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MyGray)
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        modifier = Modifier.weight(0.9f),
                        value = nameGroup.value,
                        label = "Поиск",
                        placeholder = "22ит17"
                    ) {
                        nameGroup.value = it
                    }
                    IconButton(
                        modifier = Modifier.weight(0.1f),
                        onClick = {teacherViewModel.searchByClassroom(nameGroup.value)}
                    ) {
                        Icon(painter = painterResource(R.drawable.search),
                            contentDescription = null,
                            tint = ContrastBlu
                        )
                    }
                }
                when(val state = resultState.value){
                    is StudentResultState.Error ->
                        state.message
                    StudentResultState.Idle ->
                        Text(
                            text = "найдите группу"
                        )
                    StudentResultState.Loading ->
                        Text(
                            text = "загрузка"
                        )
                    is StudentResultState.Success ->
                    {
                        state.result.forEach {
                            CardTeacher(it)
                            Log.i("group", it.toString())
                        }
                    }
                }
            }
        }
    }
}
