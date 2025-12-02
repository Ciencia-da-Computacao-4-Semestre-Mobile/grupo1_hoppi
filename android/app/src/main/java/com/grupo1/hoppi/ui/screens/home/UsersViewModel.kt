package com.grupo1.hoppi.ui.screens.home

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.users.UpdatePasswordRequest
import com.grupo1.hoppi.network.users.UpdateUserRequest
import com.grupo1.hoppi.network.users.UserResponse
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
}
