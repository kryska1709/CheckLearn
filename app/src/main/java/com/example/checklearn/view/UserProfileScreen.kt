package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.viewmodel.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun UserProfileScreen(
    profileViewModel: ProfileViewModel
) {
    val profileInfo = profileViewModel.profile.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        profileInfo.value?.let {
            Text(
                text = "Ваши данные:",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = it.fullName,
                color = Color.Black
            )
            Text(
                text = it.classRoom,
                color = Color.Black
            )
        }
        TextButton(
            modifier = Modifier.padding(bottom = 24.dp, start = 20.dp),
            onClick = {
                Firebase.auth.signOut()
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