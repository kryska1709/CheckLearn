package com.example.checklearn.data.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class ClientCore {
    companion object {
        val instance: ClientCore = ClientCore()
    }
    val serializer = Json{
        ignoreUnknownKeys = true
        encodeDefaults = true //меняем значения по умолчанию
    }
    val client = HttpClient(CIO){ //через него все запросы
        install(ContentNegotiation){ //настройка клиента по параметрам сериалайзера
            json(serializer)
        }
    }
}