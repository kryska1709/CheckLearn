package com.example.checklearn.data

import android.util.Log
import com.example.checklearn.data.core.ClientCore
import com.example.checklearn.model.BodyRequest
import com.example.checklearn.model.BodyResponse
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append

class GeminiService {
    val client = ClientCore.instance.client
    suspend fun generateContent(
        bodyResponse: BodyResponse
    ): BodyRequest{
        val response = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent"){
            headers{
                append("x-goog-api-key", "AIzaSyCQFzadZ2tLaMGEf1TxSTcQVlRwR4-m7yE") //по документации хэдэры. 1 токен авторизации
                append(HttpHeaders.ContentType, ContentType.Application.Json) //2. тип контента с которым работаем
            }
            setBody(bodyResponse) //тело запроса по дата классу
        }
        Log.i("RESPONSEEE",response.bodyAsText())
        return response.body()
    }
}