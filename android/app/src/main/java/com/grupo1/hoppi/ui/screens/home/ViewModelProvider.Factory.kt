package com.grupo1.hoppi.ui.screens.home

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.datastore.preferences.core.Preferences

class UserViewModelFactory(private val dataStore: DataStore<Preferences>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}