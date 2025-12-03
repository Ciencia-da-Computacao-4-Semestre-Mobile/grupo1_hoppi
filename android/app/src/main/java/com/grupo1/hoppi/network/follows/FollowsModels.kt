package com.grupo1.hoppi.network.follows.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateFollowRequest(
    val followee_id: String
)

@Serializable
data class FollowResponse(
    val follower_id: String,
    val followee_id: String,
    val follower: UserResponse,
    val followee: UserResponse,
    val followed_at: String
)

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val display_name: String,
    val institution: String? = null,
    val avatarIndex: Int? = 0
)