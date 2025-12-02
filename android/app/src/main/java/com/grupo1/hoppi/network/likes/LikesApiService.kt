package com.grupo1.hoppi.network.likes

import retrofit2.http.*

interface LikesApiService {

    @POST("posts/{id}/like")
    suspend fun likePost(
        @Path("id") postId: String,
        @Header("Authorization") token: String
    ): LikeResponse

    @DELETE("posts/{id}/like")
    suspend fun unlikePost(
        @Path("id") postId: String,
        @Header("Authorization") token: String
    ): LikeResponse

    @GET("posts/{id}/likes")
    suspend fun getLikes(
        @Path("id") postId: String
    ): List<LikeResponse>
}