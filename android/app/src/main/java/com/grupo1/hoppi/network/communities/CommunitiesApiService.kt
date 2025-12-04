package com.grupo1.hoppi.network.communities

import retrofit2.http.*
import retrofit2.Response

interface CommunitiesApiService {

    @GET("communities")
    suspend fun getAll(): List<Community>

    @GET("communities/{id}")
    suspend fun getOne(@Path("id") id: String): Community

    @POST("communities")
    suspend fun create(@Body request: CreateCommunityRequest, @Header("Authorization") token: String): Community

    @PATCH("communities/{id}")
    suspend fun update(@Path("id") id: String, @Body request: UpdateCommunityRequest, @Header("Authorization") token: String): Community

    @POST("communities/{id}/join")
    suspend fun join(@Path("id") id: String, @Header("Authorization") token: String): Response<Unit>

    @DELETE("communities/{id}/leave")
    suspend fun leave(@Path("id") id: String, @Header("Authorization") token: String): Response<Unit>

    @PATCH("communities/{community}/members/{user_id}")
    suspend fun updateMemberRole(
        @Path("community") communityId: String,
        @Path("user_id") userId: String,
        @Body body: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("communities/{id}/members")
    suspend fun listMembers(
        @Path("id") id: String,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("role") role: String? = null,
        @Header("Authorization") token: String
    ): List<CommunityMember>

    @GET("communities/{id}/requests")
    suspend fun listRequests(
        @Path("id") id: String,
        @Query("status") status: String? = null,
        @Header("Authorization") token: String
    ): List<CommunityMember>

    @PATCH("communities/{id}/requests/{request_id}")
    suspend fun actOnRequest(
        @Path("id") id: String,
        @Path("request_id") requestId: String,
        @Body body: CommunityRequestActionRequest,
        @Header("Authorization") token: String
    ): Response<Unit>

    @PATCH("communities/{id}/owner")
    suspend fun transferOwner(
        @Path("id") id: String,
        @Body body: TransferOwnerRequest,
        @Header("Authorization") token: String
    ): Response<Unit>
}