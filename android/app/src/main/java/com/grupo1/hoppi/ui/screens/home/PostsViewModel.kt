package com.grupo1.hoppi.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.posts.CreatePostRequest
import com.grupo1.hoppi.network.posts.PostResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<PostResponse>>(emptyList())
    val posts: StateFlow<List<PostResponse>> = _posts
    private val _userPosts = MutableStateFlow<List<PostResponse>>(emptyList())
    val userPosts: StateFlow<List<PostResponse>> = _userPosts
    private val _communityPosts = MutableStateFlow<List<PostResponse>>(emptyList())
    val communityPosts: StateFlow<List<PostResponse>> = _communityPosts
    private val _comments = MutableStateFlow<List<PostResponse>>(emptyList())
    val comments = _comments.asStateFlow()

    private var cursor: String? = null

    fun loadFeed(token: String) {
        viewModelScope.launch {
            try {
                val result = ApiClient.posts.getFeed(
                    token = "Bearer $token",
                    limit = 20,
                    cursor = cursor,
                    strategy = "main"
                )

                if (result.isNotEmpty()) cursor = result.last().id

                _posts.value = result.filter { it.is_reply_to == null }

                Log.d("PostsViewModel", "TOKEN: $token")
                Log.d("PostsViewModel", "RESULT: $result")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    fun loadUserPosts(userId: String, token: String) {
        viewModelScope.launch {
            try {
                val result = ApiClient.posts.getPostsByUser(userId, "Bearer $token")

                result.forEach { post ->
                    println("POST ${post.id} -> metadata = ${post.metadata}")
                }

                _userPosts.value = result

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadComments(postId: String) {
        viewModelScope.launch {
            try {
                val post = ApiClient.posts.getPost(postId)
                _comments.value = post.replies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createPost(content: String, token: String, tag: String? = null) {
        viewModelScope.launch {
            try {
                ApiClient.posts.createPost(
                    token = "Bearer $token",
                    body = CreatePostRequest(
                        content = content,
                        metadata = if (tag != null) {
                            mapOf("tags" to listOf(tag))
                        } else {
                            null
                        }
                    )

                )

                loadFeed(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createComment(
        postId: String,
        content: String,
        token: String,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                val newComment = ApiClient.posts.createPost(
                    token = "Bearer $token",
                    body = CreatePostRequest(
                        content = content,
                        is_reply_to = postId
                    )
                )

                val updated = _comments.value.toMutableList()
                updated.add(0, newComment)
                _comments.value = updated

                onSuccess?.invoke()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadCommunityPosts(communityId: String) {
        _communityPosts.value = emptyList()
    }

    fun deletePost(postId: String, token: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                ApiClient.posts.deletePost(
                    token = "Bearer $token",
                    id = postId
                )
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}