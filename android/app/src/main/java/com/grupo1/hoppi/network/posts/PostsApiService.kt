package com.grupo1.hoppi.network.posts

import retrofit2.http.*

interface PostsApiService {

    @GET("posts/feed")
    suspend fun getFeed(
        @Header("Authorization") token: String,
        @Query("limit") limit: Int = 20,
        @Query("cursor") cursor: String? = null,
        @Query("strategy") strategy: String = "main",
        @Query("communityId") communityId: String? = null,
    ): List<PostResponse>

    @GET("users/{id}/posts")
    suspend fun getPostsByUser(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): List<PostResponse>


    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body body: CreatePostRequest
    ): PostResponse

    @GET("posts/{id}")
    suspend fun getPost(
        @Path("id") id: String
    ): PostResponse

    @GET("posts/{id}/replies")
    suspend fun getReplies(
        @Path("id") id: String
    ): List<PostResponse>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}
