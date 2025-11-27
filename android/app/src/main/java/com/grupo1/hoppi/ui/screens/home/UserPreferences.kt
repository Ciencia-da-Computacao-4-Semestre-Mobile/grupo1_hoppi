package com.grupo1.hoppi.ui.screens.home

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPrefs {
    val PROFILE_AVATAR = intPreferencesKey("profile_avatar")
}

fun getProfileAvatar(context: Context): Flow<Int> {
    return context.dataStore.data
        .map { prefs -> prefs[UserPrefs.PROFILE_AVATAR] ?: 5 }
}

fun saveProfileAvatar(context: Context, avatarIndex: Int) {
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
        context.dataStore.edit { prefs ->
            prefs[UserPrefs.PROFILE_AVATAR] = avatarIndex
        }
    }
}
