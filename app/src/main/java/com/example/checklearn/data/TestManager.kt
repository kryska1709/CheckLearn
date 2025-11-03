package com.example.checklearn.data

import com.example.checklearn.data.core.ClientCore
import com.example.checklearn.model.BodyResponse
import com.example.checklearn.model.PartsResponse
import com.example.checklearn.model.TextResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class TestManager {

    val serializer = ClientCore.instance.serializer

    suspend fun generateTest(
        text: String
    ): TestResponse {
        val prompt = """
            Ты — эксперт по созданию образовательных материалов. 
            Проанализируй предоставленный текст и создай на его основе тестовые задания с множественным выбором.
            
            ТРЕБОВАНИЯ К ФОРМАТУ ОТВЕТА:
            - Ответ должен быть ТОЛЬКО в формате JSON без каких-либо пояснений
            - Используй следующую структуру:
            
            {
              "questions": [
                {
                  "question": "текст вопроса",
                  "options": ["вариант 1", "вариант 2", "вариант 3", "вариант 4"],
                  "correct_answer": "индекс правильного ответа (0-3)"
                }
              ]
            }
            
            ИНСТРУКЦИЯ:
            - Создай 10-15 вопросов на основе текста
            - Вопросы должны проверять понимание основной идеи, деталей и контекста
            - Правильный ответ должен быть только один
            
            Текст для анализа:
            $text
        """.trimIndent()

        val bodyResponse = BodyResponse(
            contents = listOf(
                PartsResponse(
                    parts = listOf(
                        TextResponse(
                            text = prompt
                        )
                    )
                )
            )
        )
        val response = GeminiService().generateContent(bodyResponse)
        val jsonResponse = response.candidates.first().content.parts.first().text
        val cleanJson = jsonResponse.removePrefix("```json\n").removeSuffix("```").trim()

        return serializer.decodeFromString<TestResponse>(cleanJson)
    }
}

@Serializable //преобразование сложных дата классов в массив байтов
data class TestResponse(
    val questions: List<Question>
)

@Serializable
data class Question(
    val question: String,
    val options: List<String>,
    @SerialName("correct_answer")
    val correctAnswer: Int
)