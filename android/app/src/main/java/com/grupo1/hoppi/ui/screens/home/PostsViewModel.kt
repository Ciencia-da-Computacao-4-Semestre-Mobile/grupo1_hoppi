package com.grupo1.hoppi.ui.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Post(
    val id: Int,
    val username: String,
    val handle: String,
    val content: String,
    val isSale: Boolean = false,
    val tag: String? = null,
    val communityId: Int? = null,

    val likes: Int = 0,
    val isLiked: Boolean = false,
    val comments: Int = 0,
    val shares: Int = 0
)

class PostsViewModel : ViewModel() {

    var currentUser: String = "Fulano de Tal"
        private set

    fun setCurrentUser(username: String) {
        currentUser = username
    }

    private val _posts = mutableStateListOf<Post>().apply {
        addAll(
            List(10) { i ->
                Post(
                    id = i,
                    username = "Fulano de Tal",
                    handle = "@fulan.tal",
                    content = "Lorem Ipsum is simply dummy text of the printing industry.",
                    isSale = i % 3 == 0,
                    likes = (5..40).random(),
                    isLiked = false,
                    communityId = if (i % 4 == 0) 1 else null,
                    tag = null
                )
            }
        )
    }

    val posts: List<Post> get() = _posts

    private fun addNewPost(
        content: String,
        username: String,
        isSale: Boolean,
        tag: String?,
        communityId: Int?
    ) {
        val newPost = Post(
            id = _posts.size + 1,
            username = username,
            handle = "@fulan.tal",
            content = content,
            isSale = isSale,
            tag = tag,
            communityId = communityId,
            likes = 0,
            isLiked = false
        )
        _posts.add(0, newPost)
    }

    fun addPost(content: String, username: String, isSale: Boolean, tag: String?) {
        addNewPost(content, username, isSale, tag, communityId = null)
    }

    fun addCommunityPost(content: String, username: String, isSale: Boolean, tag: String?, communityId: Int) {
        addNewPost(content, username, isSale, tag, communityId)
    }

    private fun ensureMockCommunityPosts(communityId: Int) {
        val existing = posts.filter { it.communityId == communityId }
        if (existing.isNotEmpty()) return

        repeat(5) { i ->
            addNewPost(
                content = "Conteúdo de exemplo #$i da comunidade $communityId",
                username = "Usuário $i",
                isSale = false,
                tag = null,
                communityId = communityId
            )
        }
    }

    fun getCommunityPosts(communityId: Int): List<Post> {
        ensureMockCommunityPosts(communityId)
        return posts.filter { it.communityId == communityId }
    }

    fun toggleLike(postId: Int) {
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = _posts[index]
            _posts[index] = post.copy(
                isLiked = !post.isLiked,
                likes = if (post.isLiked) post.likes - 1 else post.likes + 1
            )
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

    fun getUserPosts(username: String): List<Post> {
        return posts.filter { it.username == username }
    }
}