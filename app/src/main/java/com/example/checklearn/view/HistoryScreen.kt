package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.R
import com.example.checklearn.components.CustomButton
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.HistoryItem
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.data.rememberFirebaseAuthLauncher
import com.example.checklearn.model.HistoryUIState
import com.example.checklearn.model.UserProfile
import com.example.checklearn.ui.theme.AccentColor
import com.example.checklearn.ui.theme.ContrastBlu
import com.example.checklearn.ui.theme.MyGray
import com.example.checklearn.viewmodel.AuthViewModel
import com.example.checklearn.viewmodel.HistoryViewModel
import com.example.checklearn.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    authViewModel: AuthViewModel,
    historyViewModel: HistoryViewModel,
    profileViewModel: ProfileViewModel
) {

    val user = authViewModel.user.collectAsState()
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            authViewModel.updateUser(result.user)
        },
        onAuthError = {
            authViewModel.updateUser(null)
        }
    )
    val token = stringResource(R.string.web_client_id)
    val uiState = historyViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val history = historyViewModel.history.collectAsState()
    val fullName = remember { mutableStateOf("") }
    val classRoom = remember { mutableStateOf("") }
    SideBarMenu { drawerState ->
        CustomScaffold(
            title = "История запросов",
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
                modifier = Modifier
                    .fillMaxSize()
                    .background(MyGray)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (uiState.value){
                    HistoryUIState.Unauthorized -> {
                        Text(
                            text = "Пользователь не авторизован",
                            color = Color.Black
                        )
                        CustomButton(
                            text = "Авторизоваться",
                            textColor = Color.White,
                            color = ButtonDefaults.buttonColors(ContrastBlu),
                        ) {
                            val gso =
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(token)
                                    .requestEmail()
                                    .build()
                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            launcher.launch(googleSignInClient.signInIntent)
                        }
                    }
                    HistoryUIState.NeedProfileInfo -> {
                        Text(
                            text = "Ваши данные:",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextField(
                            value = fullName.value,
                            label = {Text(text = "ФИО")},
                            placeholder = {Text(text = "Иванов Иван Иванович")},
                            colors = TextFieldDefaults.colors(ContrastBlu, unfocusedTextColor = AccentColor),
                            onValueChange ={fullName.value = it} ,
                        )
                        TextField(
                            value = classRoom.value,
                            label = {Text(text = "Класс/Группа")},
                            placeholder = {Text(text = "22ит17")},
                            colors = TextFieldDefaults.colors(ContrastBlu, unfocusedTextColor = AccentColor),
                            onValueChange ={classRoom.value = it} ,
                        )
                        CustomButton(
                            color = ButtonDefaults.buttonColors(ContrastBlu),
                            text = "Сохранить",
                            textColor = Color.White,
                        ) {
                            val profile = UserProfile(fullName.value,classRoom.value)
                            profileViewModel.saveUserProfile(profile)
                        }
                    }
                    HistoryUIState.ShowHistory -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(history.value){
                                HistoryItem(it)
                            }
                        }
                    }
                }
            }
        }
    }
}