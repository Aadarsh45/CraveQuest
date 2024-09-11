package com.example.cravequest.models

data class AuthResponse(
    val jwt: String,
    val message: String,
    val role: String
)