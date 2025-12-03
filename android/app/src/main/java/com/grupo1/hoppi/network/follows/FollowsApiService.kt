package com.grupo1.hoppi.network.follows

import retrofit2.http.*
import com.grupo1.hoppi.network.follows.models.*

interface FollowsApiService {

    @POST("follows")
    suspend fun followUser(
        @Body body: CreateFollowRequest,
        @Header("Authorization") token: String
    ): FollowResponse

    @DELETE("follows/{followee_id}")
    suspend fun unfollowUser(
        @Path("followee_id") followeeId: String,
        @Header("Authorization") token: String
    ): FollowResponse

    @GET("users/{id}/followers")
    suspend fun getFollowers(
        @Path("id") userId: String
    ): List<FollowResponse>

    @GET("users/{id}/following")
    suspend fun getFollowing(
        @Path("id") userId: String
    ): List<FollowResponse>
}