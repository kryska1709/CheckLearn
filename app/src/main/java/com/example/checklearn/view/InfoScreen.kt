package com.example.checklearn.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checklearn.R
import com.example.checklearn.components.CustomScaffold
import com.example.checklearn.components.SideBarMenu
import com.example.checklearn.ui.theme.BlueMainColor
import com.example.checklearn.ui.theme.MyGray
import kotlinx.coroutines.launch

@Composable
fun InfoScreen() {

    val scope = rememberCoroutineScope()

    SideBarMenu { drawerState ->
        CustomScaffold(
            title = "О приложении",
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
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        ) { innerPadding ->
            val cards = listOf(
                "О приложении" to "Приложение помогает быстро и легко создавать тесты с помощью искусственного интеллекта — загрузите текст или фото, а мы сгенерируем вопросы и ответы. 🤖📚",
                "Как начать работу?" to "Для начала войдите через Google, затем сфотографируйте нужный текст или загрузите изображение из галереи. 🔑📸",
                "Как получить тест" to "После загрузки или съёмки текста проверьте превью, затем нажмите «Сгенерировать» — и готовый тест появится у вас в приложении. 📝➡️🚀",
                "Что делать дальше" to "После генерации тест можно проходить: в каждом вопросе — один правильный ответ. Количество вопросов обычно 10–15. Удачи! ✅🎯",
                "История запросов" to "Если вы авторизованы, все ваши запросы сохраняются в истории — можно вернуться к ранее сгенерированным тестам в любой момент. 🔁📂",
                "Связаться с поддержкой" to "Наши контакты: в Telegram — @your_support (замените на ваш), или по электронной почте: support@example.com. Пишите при ошибках или неточностях — мы быстро поможем. 💬✉️"
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MyGray),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cards) { (title, description) ->
                    Card(
                        modifier = Modifier.fillMaxSize()
                            .padding(start = 8.dp, end = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(BlueMainColor.copy(0.8f)),
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = description,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
