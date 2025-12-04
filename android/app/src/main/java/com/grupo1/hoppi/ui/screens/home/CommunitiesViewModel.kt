package com.grupo1.hoppi.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.communities.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommunitiesViewModel(
    private val api: CommunitiesApiService = ApiClient.communities
) : ViewModel() {

    private val _communities = MutableStateFlow<List<Community>>(emptyList())
    val communities: StateFlow<List<Community>> = _communities

    private val _members = MutableStateFlow<Map<String, List<CommunityMember>>>(emptyMap())
    val members: StateFlow<Map<String, List<CommunityMember>>> = _members
    private val _followedCommunities = MutableStateFlow<Set<String>>(emptySet())
    val followedCommunities = _followedCommunities.asStateFlow()

    fun loadCommunities() {
        viewModelScope.launch {
            try {
                _communities.value = api.getAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadCommunityMembers(communityId: String, token: String, page: Int? = 1, limit: Int? = 20, role: String? = null) {
        viewModelScope.launch {
            try {
                val list = api.listMembers(communityId, "Bearer $token", page, limit, role)
                _members.value = _members.value.toMutableMap().apply { put(communityId, list) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun joinCommunity(communityId: String, token: String) {
        viewModelScope.launch {
            try {
                api.join(communityId, "Bearer $token")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun leaveCommunity(communityId: String, token: String) {
        viewModelScope.launch {
            try {
                api.leave(communityId, "Bearer $token")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createCommunity(request: CreateCommunityRequest, token: String) {
        viewModelScope.launch {
            try {
                val newCommunity = api.create(request, "Bearer $token")
                _communities.value = _communities.value + newCommunity
            } catch (e: Exception) {
                Log.e("COMMUNITY_CREATE", "ERRO AO CRIAR:", e)
            }
        }
    }

    fun updateCommunity(communityId: String, request: UpdateCommunityRequest, token: String) {
        viewModelScope.launch {
            try {
                val updated = api.update(communityId, request, "Bearer $token")
                _communities.value = _communities.value.map { if (it.id == communityId) updated else it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun markCommunityAsFollowed(name: String) {
        _followedCommunities.value = _followedCommunities.value + name
    }

    fun markCommunityAsUnfollowed(name: String) {
        _followedCommunities.value = _followedCommunities.value - name
    }
}