package com.example.cravequest.models

data class SignUpRequest(
    val imageUser: String,
    val username: String,
    val email: String,
    val role: String,
    val password: String
)
