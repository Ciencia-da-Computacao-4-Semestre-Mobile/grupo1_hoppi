package com.grupo1.hoppi.network.posts

data class AuthorDTO(
    val id: String,
    val username: String?,
    val display_name: String?,
    val avatar_key: String?
)

data class PostResponse(
    val id: String,
    val content: String,
    val created_at: String,
    val reply_count: Int = 0,
    val is_deleted: Boolean = false,
    val metadata: Map<String, Any>? = null,
    val author: AuthorDTO?,
    val replies: List<PostResponse> = emptyList()
)

data class CreatePostRequest(
    val content: String,
    val is_reply_to: String? = null,
    val metadata: Map<String, Any>? = null
)
