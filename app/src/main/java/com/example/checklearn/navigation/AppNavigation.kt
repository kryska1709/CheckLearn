package com.example.checklearn.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checklearn.Util.HistoryViewModelFactory
import com.example.checklearn.view.CameraScreen
import com.example.checklearn.view.HistoryScreen
import com.example.checklearn.view.InfoScreen
import com.example.checklearn.view.StatisticScreen
import com.example.checklearn.view.TeacherScreen
import com.example.checklearn.view.TestScreen
import com.example.checklearn.view.UserProfileScreen
import com.example.checklearn.viewmodel.AuthViewModel
import com.example.checklearn.viewmodel.CameraViewModel
import com.example.checklearn.viewmodel.HistoryViewModel
import com.example.checklearn.viewmodel.ProfileViewModel
import com.example.checklearn.viewmodel.TaskViewModel
import com.example.checklearn.viewmodel.TeacherViewModel

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
val LocalIsTeacher = compositionLocalOf { false }
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val navigator = remember { Navigator() }.apply { bind(navController) }
    val cameraViewModel: CameraViewModel = viewModel<CameraViewModel>()
    val taskViewModel: TaskViewModel = viewModel<TaskViewModel>()
    val authViewModel: AuthViewModel = viewModel<AuthViewModel>()
    val profileViewModel: ProfileViewModel = viewModel<ProfileViewModel>()
    val historyViewModel: HistoryViewModel = viewModel<HistoryViewModel>(
        factory = HistoryViewModelFactory(authViewModel, profileViewModel)
    )
    val teacherViewModel: TeacherViewModel = viewModel<TeacherViewModel>()
    val isTeacher by profileViewModel.profile.collectAsState()
    CompositionLocalProvider(LocalNavigator provides navigator, LocalIsTeacher provides (isTeacher?.teacher ?:false)) {
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
                HistoryScreen(authViewModel,historyViewModel,profileViewModel)
            }
            composable(Routes.INFO){
                InfoScreen()
            }
            composable(Routes.PROFILE) {
                UserProfileScreen(authViewModel, profileViewModel)
            }
            composable(Routes.TEACHER) {
                TeacherScreen(teacherViewModel)
            }
        }
    }
}