package com.example.checklearn.model

import kotlinx.serialization.Serializable

@Serializable
data class BodyResponse(
    val contents: List<PartsResponse>
)
@Serializable
data class PartsResponse(
    val parts: List<TextResponse>
)
@Serializable
data class TextResponse(
    val text: String
)
//{
//    "contents": [
//    {
//        "parts": [
//        {
//            "text": "что такое интерстеллар?"
//        }
//        ]
//    }
//    ]
//}