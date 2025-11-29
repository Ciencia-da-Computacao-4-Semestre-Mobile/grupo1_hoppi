package com.grupo1.hoppi.network.users

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("avatar_key") val avatarKey: String,
    @SerializedName("email") val email: String,
    @SerializedName("birth_date") val birthDate: String,
    @SerializedName("institution") val institution: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("is_suspended") val isSuspended: Boolean
)

data class UpdateUserRequest(
    @SerializedName("username") val username: String? = null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("avatar_key") val avatarKey: String? = null,
    @SerializedName("institution") val institution: String? = null
)

data class UpdatePasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("new_password") val newPassword: String
)

data class PublicUserDTO(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("display_name") val displayName: String
)
