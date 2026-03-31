package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.checklearn.R
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.CustomTextField
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.ui.theme.MyGray
import com.example.checklearn.viewmodel.TeacherViewModel
import kotlinx.coroutines.launch

@Composable
fun TeacherScreen(
    teacherViewModel: TeacherViewModel
) {
    val scope = rememberCoroutineScope()
    val nameGroup = remember {mutableStateOf("")}// для поиска по группе
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
                    .background(MyGray)
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

            }
        }
    }
}
