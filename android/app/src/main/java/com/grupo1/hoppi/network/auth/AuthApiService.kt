package com.grupo1.hoppi.network.auth

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest)

    @POST("auth/verify-reset-code")
    suspend fun verifyResetCode(@Body body: VerifyCodeRequest)

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest)

    @POST("auth/me")
    suspend fun me(@Header("Authorization") token: String): UserDTO
}