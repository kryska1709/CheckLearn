package com.example.checklearn

import com.example.checklearn.data.GeminiService
import com.example.checklearn.model.BodyContent1
import com.example.checklearn.model.BodyParts1
import com.example.checklearn.model.BodyRequest
import com.example.checklearn.model.BodyResponse
import com.example.checklearn.model.BodyText
import com.example.checklearn.model.PartsResponse
import com.example.checklearn.model.TextResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class GeminiServiceTest {

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `generateContent возвращает BodyRequest при успешном ответе`() = runTest(dispatcher) {

        // Подготовка: инжектировать MockEngine в ClientCore до создания сервиса.
        val fakeResponse = BodyRequest(
            candidates = listOf(
                BodyContent1(
                    content = BodyParts1(
                        parts = listOf(BodyText(text = "```json\n{\"questions\":[]}\n```"))
                    )
                )
            )
        )

        val mockService = mockk<GeminiService>()
        val requestBody = BodyResponse(
            contents = listOf(
                PartsResponse(parts = listOf(TextResponse(text = "test")))
            )
        )

        coEvery { mockService.generateContent(requestBody) } returns fakeResponse

        val result = mockService.generateContent(requestBody)

        assertEquals(1, result.candidates.size)
        assertEquals("```json\n{\"questions\":[]}\n```", result.candidates.first().content.parts.first().text)
        coVerify(exactly = 1) { mockService.generateContent(requestBody) }
    }

    @Test(expected = Exception::class)
    fun `generateContent пробрасывает исключение при сетевой ошибке`() = runTest(dispatcher) {

        val mockService = mockk<GeminiService>()
        val requestBody = BodyResponse(
            contents = listOf(
                PartsResponse(parts = listOf(TextResponse(text = "test")))
            )
        )

        coEvery { mockService.generateContent(any()) } throws RuntimeException("Network error")

        mockService.generateContent(requestBody)
    }
}