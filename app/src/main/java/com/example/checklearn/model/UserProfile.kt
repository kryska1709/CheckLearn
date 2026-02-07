package com.example.checklearn.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val fullName: String = "",
    val classRoom: String = ""
)
