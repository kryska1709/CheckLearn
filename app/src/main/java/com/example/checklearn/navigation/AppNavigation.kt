package com.example.checklearn.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checklearn.view.CameraScreen
import com.example.checklearn.view.HistoryScreen
import com.example.checklearn.view.InfoScreen
import com.example.checklearn.view.StatisticScreen
import com.example.checklearn.view.TestScreen
import com.example.checklearn.viewmodel.AuthViewModel
import com.example.checklearn.viewmodel.CameraViewModel
import com.example.checklearn.viewmodel.TaskViewModel

class Navigator(){
    private var navController: NavController? = null
    fun bind(navController: NavController){
        this.navController = navController
    }
    fun navigate(route: String){
        navController?.navigate(route)
    }
    fun popBackStack(){
        navController?.popBackStack()
    }
}

val LocalNavigator = staticCompositionLocalOf<Navigator> { error("NO NAVIGATOR") }
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val navigator = remember { Navigator() }.apply { bind(navController) }
    val cameraViewModel: CameraViewModel = viewModel<CameraViewModel>()
    val taskViewModel: TaskViewModel = viewModel<TaskViewModel>()
    val authViewModel: AuthViewModel = viewModel<AuthViewModel>()

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavHost(
            navController = navController,
            startDestination = Routes.CAMERA
        ) {
            composable(Routes.CAMERA) {
                CameraScreen( cameraViewModel)
            }
            composable(Routes.STATISTIC) {
                StatisticScreen(cameraViewModel, taskViewModel)
            }
            composable(Routes.TASKS) {
                TestScreen(taskViewModel, cameraViewModel)
            }
            composable(Routes.HISTORY) {
                HistoryScreen(authViewModel)
            }
            composable(Routes.INFO){
                InfoScreen()
            }
        }
    }
}