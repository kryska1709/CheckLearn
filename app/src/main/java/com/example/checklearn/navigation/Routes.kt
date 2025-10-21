package com.example.checklearn.navigation

object Routes {
    const val CAMERA = "camera"
    const val STATISTIC = "statistic"
    const val TASKS = "tasks"
    const val INFO = "information"
    const val HISTORY = "history"
    fun getTitle(routes: String): String{
        return when(routes){
            STATISTIC -> "Статистика"
            TASKS -> "Похожие задачи"
            HISTORY -> "История запросов"
            INFO -> "О приложении"
            else -> ""
        }
    }
}
