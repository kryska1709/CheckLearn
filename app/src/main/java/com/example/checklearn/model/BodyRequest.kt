package com.example.checklearn.model

import kotlinx.serialization.Serializable

@Serializable
data class BodyRequest(
    val candidates: List<BodyContent1>
)

@Serializable
data class BodyContent1(
    val content: BodyParts1
)

@Serializable
data class BodyParts1(
    val parts: List<BodyText>
)

@Serializable
data class BodyText(
    val text: String
)