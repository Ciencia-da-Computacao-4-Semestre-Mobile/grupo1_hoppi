package com.grupo1.hoppi.ui.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Post(
    val id: Int,
    val username: String,
    val content: String,
    val isSale: Boolean = false,
    val tag: String? = null,

    val likes: Int = 0,
    val isLiked: Boolean = false,
    val comments: Int = 0,
    val shares: Int = 0
)
class PostsViewModel : ViewModel() {
    private val _posts = mutableStateListOf<Post>().apply {
        addAll(
            List(10) { i ->
                Post(
                    id = i,
                    username = "Fulano de Tal",
                    content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                    isSale = i % 3 == 0,
                    likes = (5..40).random(),
                    isLiked = false
                )
            }
        )
    }
    val posts: List<Post> = _posts

    fun addPost(content: String, username: String, isSale: Boolean, tag: String?) {
        val newPost = Post(
            id = _posts.size + 1,
            username = username,
            content = content,
            isSale = isSale,
            tag = tag,
            likes = 0,
            isLiked = false
        )
        _posts.add(0, newPost)
    }

    fun toggleLike(postId: Int) {
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = _posts[index]
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likes = if (post.isLiked) post.likes - 1 else post.likes + 1
            )
            _posts[index] = updatedPost
        }
    }

    fun addComment(postId: Int) {
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = _posts[index]
            _posts[index] = post.copy(comments = post.comments + 1)
        }
    }

    fun deletePost(id: Int) {
        _posts.removeAll { it.id == id }
    }
}