package com.grupo1.hoppi.ui.screens.home

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.users.PublicUserDTO
import com.grupo1.hoppi.network.users.UpdatePasswordRequest
import com.grupo1.hoppi.network.users.UpdateUserRequest
import com.grupo1.hoppi.network.users.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UsersViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()
    private val _searchResults = MutableStateFlow<List<PublicUserDTO>>(emptyList())
    val searchResults: StateFlow<List<PublicUserDTO>> = _searchResults.asStateFlow()
    private val _allUsers = MutableStateFlow<List<PublicUserDTO>>(emptyList())
    private val _publicProfile = MutableStateFlow<PublicUserDTO?>(null)
    val publicProfile: StateFlow<PublicUserDTO?> = _publicProfile.asStateFlow()

    fun setPublicProfile(profile: PublicUserDTO?) {
        _publicProfile.value = profile
    }

    fun setToken(newToken: String) {
        _token.value = newToken
    }

    private val _profile = MutableStateFlow<UserResponse?>(null)
    val profile: StateFlow<UserResponse?> = _profile.asStateFlow()

    val avatarIndexFlow: Flow<Int> = dataStore.data
        .map { prefs -> prefs[UserPrefs.PROFILE_AVATAR] ?: 5 }

    suspend fun setAvatar(index: Int) {
        dataStore.edit { prefs ->
            prefs[UserPrefs.PROFILE_AVATAR] = index
        }
    }

    fun loadProfile(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.users.getProfile("Bearer $token")
                _profile.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getUserById(id: String): PublicUserDTO? {
        return try {
            ApiClient.users.getUserById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updateProfile(token: String, body: UpdateUserRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val response = ApiClient.users.updateProfile("Bearer $token", body)
                _profile.value = response
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePassword(token: String, current: String, newPass: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                ApiClient.users.updatePassword(
                    "Bearer $token",
                    UpdatePasswordRequest(current, newPass)
                )
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setCurrentUser(username: String) {
        _currentUser.value = username
    }


    fun setCurrentUserId(id: String) {
        _currentUserId.value = id
    }

    fun searchUsers(query: String) {
        val filtered = _allUsers.value.filter { user ->
            user.username.contains(query, ignoreCase = true) ||
                    user.displayName.contains(query, ignoreCase = true)
        }
        _searchResults.value = filtered
    }

    fun loadAllUsers() {
        val tokenValue = _token.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val users = ApiClient.users.getAllUsers("Bearer $tokenValue")
                _allUsers.value = users
                _searchResults.value = users
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
