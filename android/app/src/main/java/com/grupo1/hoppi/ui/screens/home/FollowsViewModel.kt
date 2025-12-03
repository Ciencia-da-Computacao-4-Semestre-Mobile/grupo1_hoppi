package com.grupo1.hoppi.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.follows.FollowsApiService
import com.grupo1.hoppi.network.follows.models.FollowResponse
import com.grupo1.hoppi.network.follows.models.CreateFollowRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FollowsViewModel(
    private val api: FollowsApiService
) : ViewModel() {

    private val _followers = MutableStateFlow<List<FollowResponse>>(emptyList())
    val followers: StateFlow<List<FollowResponse>> = _followers

    private val _following = MutableStateFlow<List<FollowResponse>>(emptyList())
    val following: StateFlow<List<FollowResponse>> = _following

    fun loadFollowers(userId: String) {
        viewModelScope.launch {
            try {
                val result = api.getFollowers(userId)
                _followers.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadFollowing(userId: String) {
        viewModelScope.launch {
            try {
                val result = api.getFollowing(userId)
                _following.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun followUser(followeeId: String, token: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                api.followUser(CreateFollowRequest(followeeId), "Bearer $token")
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun unfollowUser(followeeId: String, token: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                api.unfollowUser(followeeId, "Bearer $token")
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}