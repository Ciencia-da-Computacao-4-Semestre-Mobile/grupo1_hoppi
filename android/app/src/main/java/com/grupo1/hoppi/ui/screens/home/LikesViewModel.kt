package com.grupo1.hoppi.ui.screens.home

import android.util.Log
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
                Log.d("LikesViewModel", "Calling loadLikes for $postId")
                val response = ApiClient.likes.getLikes(postId)
                val updated = _likes.value.toMutableMap()
                updated[postId] = response
                _likes.value = updated
            } catch (e: Exception) {
                Log.e("LikesViewModel", "Error load", e)
                e.printStackTrace()
            }
        }
    }

    fun likePost(postId: String, token: String, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                Log.d("LikesViewModel", "Calling likePost for $postId (tokenPresent=${token.isNotBlank()})")
                ApiClient.likes.likePost(postId, "Bearer $token")
                loadLikes(postId)
                onComplete?.invoke()
            } catch (e: Exception) {
                Log.e("LikesViewModel", "Error likePost", e)
                e.printStackTrace()
            }
        }
    }

    fun unlikePost(postId: String, token: String, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                Log.d("LikesViewModel", "Calling unlikePost for $postId (tokenPresent=${token.isNotBlank()})")
                ApiClient.likes.unlikePost(postId, "Bearer $token")
                loadLikes(postId)
                onComplete?.invoke()
            } catch (e: Exception) {
                Log.e("LikesViewModel", "Error unlikePost", e)
                e.printStackTrace()
            }
        }
    }
}