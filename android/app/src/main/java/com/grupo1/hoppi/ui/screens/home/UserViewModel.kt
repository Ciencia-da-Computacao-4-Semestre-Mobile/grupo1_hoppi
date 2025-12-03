package com.grupo1.hoppi.ui.screens.home

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.lifecycle.ViewModel

class UserViewModel(private val dataStore: DataStore<Preferences>) : ViewModel() {
    val avatarIndexFlow: Flow<Int> = dataStore.data
        .map { prefs -> prefs[UserPrefs.PROFILE_AVATAR] ?: 5 }

    suspend fun setAvatar(index: Int) {
        dataStore.edit { prefs ->
            prefs[UserPrefs.PROFILE_AVATAR] = index
        }
    }
}
