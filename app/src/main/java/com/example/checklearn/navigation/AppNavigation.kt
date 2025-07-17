package com.example.checklearn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checklearn.view.CameraScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.CAMERA
    ) {
        composable(Routes.CAMERA) {
            CameraScreen {  }
        }
        composable(Routes.STATISTIC) {
        }
        composable(Routes.TASKS) {
        }
    }
}