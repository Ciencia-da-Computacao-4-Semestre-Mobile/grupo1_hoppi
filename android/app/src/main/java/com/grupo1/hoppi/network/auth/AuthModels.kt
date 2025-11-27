package com.grupo1.hoppi.network.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("username") val username: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("birth_date") val birthDate: String,
    @SerializedName("institution") val institution: String
)

@Serializable
data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)

@Serializable
data class VerifyCodeRequest(
    @SerializedName("email") val email: String,
    @SerializedName("code") val code: String
)

@Serializable
data class ResetPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("code") val code: String,
    @SerializedName("newPassword") val newPassword: String
)

@Serializable
data class AuthResponse(
    @SerializedName("access_token") val access_token: String? = null,
    @SerializedName("user") val user: UserDTO?,
)

@Serializable
data class UserDTO(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String
)
