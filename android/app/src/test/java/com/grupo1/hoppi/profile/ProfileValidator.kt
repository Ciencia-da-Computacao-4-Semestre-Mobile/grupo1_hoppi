package com.grupo1.hoppi.profile

import com.grupo1.hoppi.ui.screens.home.Post

object ProfileValidator {

    fun hasUserPosts(posts: List<Post>): Boolean {
        return posts.isNotEmpty()
    }

    fun isPostLiked(post: Post): Boolean {
        return post.isLiked
    }

    fun toggleLike(post: Post): Post {
        return if (post.isLiked) {
            post.copy(isLiked = false, likes = post.likes - 1)
        } else {
            post.copy(isLiked = true, likes = post.likes + 1)
        }
    }

    fun filterByUser(posts: List<Post>, username: String): List<Post> {
        return posts.filter { it.username == username }
    }
}