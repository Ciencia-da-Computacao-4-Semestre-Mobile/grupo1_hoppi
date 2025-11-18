package com.grupo1.hoppi.ui.screens.mainapp

import com.grupo1.hoppi.ui.screens.home.Post

object FeedValidator {
    fun hasPosts(posts: List<Post>) = posts.isNotEmpty()
    fun isPostLiked(post: Post) = post.isLiked
    fun toggleLike(post: Post): Post {
        return post.copy(
            isLiked = !post.isLiked,
            likes = if (post.isLiked) post.likes - 1 else post.likes + 1
        )
    }
}