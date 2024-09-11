package com.example.cravequest.models

data class LoginRequest(
    val email: String,
    val role: String,
    val password: String
)