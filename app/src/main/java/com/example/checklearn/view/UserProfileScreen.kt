package com.example.checklearn.view

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.R
import com.example.checklearn.components.CustomButton
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.CustomTextField
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.model.UserProfile
import com.example.checklearn.navigation.LocalNavigator
import com.example.checklearn.ui.theme.ContrastBlu
import com.example.checklearn.viewmodel.AuthViewModel
import com.example.checklearn.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    val profileInfo = profileViewModel.profile.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.current
    val fullName = remember{ mutableStateOf(profileInfo.value?.fullName?:"") }
    val classRoom = remember { mutableStateOf(profileInfo.value?.classRoom?:"") }
    SideBarMenu { drawerState ->
        CustomScaffold(
            title = "Профиль",
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
                modifier = Modifier.fillMaxSize()
                    .background(Color.White)
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ваши данные:",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                CustomTextField (
                    value = fullName.value,
                    onValueChange = { fullName.value = it },
                    label = "Имя",
                    placeholder = "Иванов Иван"
                )
                CustomTextField (
                    value = classRoom.value,
                    onValueChange = { classRoom.value = it },
                    label = "Класс/группа",
                    placeholder = "22ит17"
                )
                AnimatedVisibility(visible = profileInfo.value?.fullName!=fullName.value||profileInfo.value?.classRoom!=classRoom.value) {
                    CustomButton(
                        text = "Сохранить",
                        textColor = Color.White,
                        color = ButtonDefaults.buttonColors(ContrastBlu)
                    ) {
                        profileViewModel.saveUserProfile(
                            UserProfile(fullName.value,classRoom.value)
                        )
                        Toast.makeText(context, "данные сохранены", Toast.LENGTH_SHORT).show()
                    }
                }
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        authViewModel.signOut()
                        Toast.makeText(context, "вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(
                        text = "Выйти из аккаунта",
                        color = Color.Red,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}