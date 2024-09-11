package com.example.cravequest.network

import com.example.cravequest.models.AuthResponse
import com.example.cravequest.models.LoginRequest
import com.example.cravequest.models.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/signup")
    fun signUp(@Body request: SignUpRequest): Call<AuthResponse>

    @POST("auth/signin")
    fun signIn(@Body request: LoginRequest): Call<AuthResponse>
}
