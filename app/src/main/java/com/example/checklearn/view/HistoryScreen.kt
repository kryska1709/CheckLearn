package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.R
import com.example.checklearn.components.CustomButton
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.HistoryItem
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.network.rememberFirebaseAuthLauncher
import com.example.checklearn.ui.theme.ContrastBlu
import com.example.checklearn.ui.theme.MyGray
import com.example.checklearn.viewmodel.AuthViewModel
import com.example.checklearn.viewmodel.HistoryViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.ResourceDescriptor
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    authViewModel: AuthViewModel,
    historyViewModel: HistoryViewModel
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val history = historyViewModel.history.collectAsState()
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
                modifier = Modifier.fillMaxSize()
                    .background(MyGray)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (user.value == null) {
                    Text(
                        text = "Пользователь не авторизован",
                        color = Color.Black
                    )
                    CustomButton(
                        text = "Авторизоваться",
                        textColor = Color.White,
                        color = ButtonDefaults.buttonColors(ContrastBlu),
                    ) {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                    }
                } else {
                    if (history.value.isNotEmpty()) {
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
                    } else {
                        Text(
                            text = "Вы авторизованы)))))))",
                            color = Color.Black
                        )
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
            }
        }
    }
}