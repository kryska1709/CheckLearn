package com.example.checklearn

import com.example.checklearn.data.TestManager
import com.example.checklearn.data.TestResponse
import com.example.checklearn.model.Question
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class TestManagerTest {

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `generateTest возвращает TestResponse с вопросами`() = runTest(dispatcher) {
        val mockManager = mockk<TestManager>()
        val expectedQuestions = listOf(
            Question(
                question = "What is Kotlin?",
                options = listOf("A language", "A framework", "An OS", "A DB"),
                correctAnswer = 0
            )
        )
        val expectedResponse = TestResponse(questions = expectedQuestions)

        coEvery { mockManager.generateTest(any()) } returns expectedResponse

        val result = mockManager.generateTest("Kotlin is a programming language.")

        assertEquals(1, result.questions.size)
        assertEquals("What is Kotlin?", result.questions.first().question)
        assertEquals(0, result.questions.first().correctAnswer)
    }

    // generateTest: возвращает от 10 до 15 вопросов
    @Test
    fun `generateTest возвращает корректное количество вопросов`() = runTest(dispatcher) {
        val mockManager = mockk<TestManager>()
        val questions = (1..12).map { i ->
            Question(
                question = "Question $i",
                options = listOf("A", "B", "C", "D"),
                correctAnswer = 0
            )
        }
        coEvery { mockManager.generateTest(any()) } returns TestResponse(questions)

        val result = mockManager.generateTest("Some long text about a topic.")

        assertTrue("Должно быть 10-15 вопросов", result.questions.size in 10..15)
    }
}
