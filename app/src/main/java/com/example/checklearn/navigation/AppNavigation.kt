package com.example.checklearn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checklearn.view.CameraScreen
import com.example.checklearn.view.StatisticScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.CAMERA
    ) {
        composable(Routes.CAMERA) {
            CameraScreen {
                navController.navigate(Routes.STATISTIC)
            }
        }
        composable(Routes.STATISTIC) {
            StatisticScreen{
                navController.popBackStack()
            }
        }
        composable(Routes.TASKS) {
        }
    }
}