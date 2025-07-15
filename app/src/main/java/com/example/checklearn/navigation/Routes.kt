package com.example.checklearn.navigation

object Routes {
    const val CAMERA = "camera"
    const val STATISTIC = "statistic"
    const val TASKS = "tasks"
    fun getTitle(routes: String): String{
        return when(routes){
            STATISTIC -> "Статистика"
            TASKS -> "Похожие задачи"
            else -> ""
        }
    }
}
