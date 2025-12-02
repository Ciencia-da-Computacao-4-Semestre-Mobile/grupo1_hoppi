package com.grupo1.hoppi.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.likes.LikeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LikesViewModel : ViewModel() {

    private val _likes = MutableStateFlow<Map<String, List<LikeResponse>>>(emptyMap())
    val likes: StateFlow<Map<String, List<LikeResponse>>> = _likes.asStateFlow()

    fun loadLikes(postId: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.likes.getLikes(postId)
                val updated = _likes.value.toMutableMap()
                updated[postId] = response
                _likes.value = updated
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun likePost(postId: String, token: String, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                ApiClient.likes.likePost(postId, "Bearer $token")
                loadLikes(postId)
                onComplete?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun unlikePost(postId: String, token: String, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                ApiClient.likes.unlikePost(postId, "Bearer $token")
                loadLikes(postId)
                onComplete?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}