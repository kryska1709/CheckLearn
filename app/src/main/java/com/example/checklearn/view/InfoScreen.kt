package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.R
import com.example.checklearn.components.CustomInfoCard
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.CustomTopAppBar
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.ui.theme.MyGray
import kotlinx.coroutines.launch

@Composable
fun InfoScreen() {

    val scope = rememberCoroutineScope()

    SideBarMenu { drawerState ->
        CustomScaffold(
            topAppBar = {
                CustomTopAppBar(
                    title = {
                        Text(
                        text = "О приложении",
                        fontSize = 22.sp,
                        color = Color.Black
                    )},
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
                )
            }
        ) { innerPadding ->
            val cards = listOf(
                "О приложении" to "Создавайте тесты мгновенно! Загружайте текст или фото — ИИ сделает всё остальное 💫🤖",
                "Старт работы" to "Вход через Google → загрузка контента. Два шага — и вы творите! 🎯📸",
                "Генерация теста" to "Проверяете превью → жмёте «Сгенерировать» → получаете готовый тест! Быстрее, чем кофе успеет остыть ☕🚀",
                "Формат тестирования" to "Умные вопросы • Один правильный ответ • 10-15 заданий • Идеальный баланс сложности 🎮🏆",
                "История запросов" to "Ваша коллекция тестов всегда под рукой. Возвращайтесь к любому материалу в один клик 📚🔄",
                "На связи 24/7" to "Telegram: @your_support 📲 • Email: support@example.com 📧 • Решаем вопросы молниеносно! ⚡💬"
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MyGray),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cards) { (title, description) ->
                    CustomInfoCard(title,description)
                }
            }
        }
    }
}
