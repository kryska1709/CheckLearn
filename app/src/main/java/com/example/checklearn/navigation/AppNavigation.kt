package com.example.checklearn.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checklearn.view.CameraScreen
import com.example.checklearn.view.StatisticScreen
import com.example.checklearn.viewmodel.CameraViewModel
import com.example.checklearn.viewmodel.TaskViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel<CameraViewModel>()
    val taskViewModel: TaskViewModel = viewModel<TaskViewModel>()

    NavHost(
        navController = navController,
        startDestination = Routes.CAMERA
    ) {
        composable(Routes.CAMERA) {
            CameraScreen(navController,cameraViewModel) {
                navController.navigate(Routes.STATISTIC)
            }
        }
        composable(Routes.STATISTIC) {
            StatisticScreen(cameraViewModel){
                navController.popBackStack()
            }
        }
        composable(Routes.TASKS) {
        }

        composable(Routes.HISTORY) {

        }
    }
}