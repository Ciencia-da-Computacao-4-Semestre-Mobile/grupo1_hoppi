package com.grupo1.hoppi.profile

import com.grupo1.hoppi.ui.screens.home.Post

object ProfileValidator {
    fun validateProfile(username: String, name: String, bio: String): Boolean {
        return username.isNotBlank() && name.isNotBlank() && bio.isNotBlank()
    }

    fun validatePosts(posts: List<Post>): Boolean {
        if (posts.isEmpty()) return false
        return posts.all { post ->
            post.id >= 0 &&
                    post.username.isNotBlank() &&
                    post.content.isNotBlank()
        }
    }

    fun toggleLike(posts: List<Post>, postId: Int): List<Post> {
        return posts.map { post ->
            if (post.id == postId) post.copy(isLiked = !post.isLiked)
            else post
        }
    }
}