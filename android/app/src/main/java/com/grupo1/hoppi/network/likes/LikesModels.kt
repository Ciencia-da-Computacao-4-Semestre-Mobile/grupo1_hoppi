package com.grupo1.hoppi.network.likes

import com.google.gson.annotations.SerializedName
import com.grupo1.hoppi.network.posts.PostResponse
import com.grupo1.hoppi.network.posts.AuthorDTO

data class LikeResponse(
    @SerializedName("user_id")
    val user_id: String,
    @SerializedName("post_id")
    val post_id: String,
    @SerializedName("liked_at")
    val liked_at: String,
    val user: AuthorDTO,
    val post: PostResponse
)


data class CreateLikeRequest(
    val post_id: String
)