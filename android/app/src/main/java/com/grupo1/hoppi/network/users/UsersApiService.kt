package com.grupo1.hoppi.network.users

import com.grupo1.hoppi.network.posts.PostResponse
import retrofit2.http.*

interface UsersApiService {

    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): UserResponse

    @PATCH("users/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body body: UpdateUserRequest
    ): UserResponse

    @PATCH("users/profile/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body body: UpdatePasswordRequest
    ): UserResponse

    @PATCH("users/{id}/suspend")
    suspend fun suspendUser(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): UserResponse

    @GET("users/{id}/posts")
    suspend fun getUserPosts(
        @Path("id") id: String
    ): List<PostResponse>

    @GET("users/{id}/likes")
    suspend fun getUserLikes(
        @Path("id") id: String
    ): List<Any>

    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): List<PublicUserDTO>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ): PublicUserDTO

}
