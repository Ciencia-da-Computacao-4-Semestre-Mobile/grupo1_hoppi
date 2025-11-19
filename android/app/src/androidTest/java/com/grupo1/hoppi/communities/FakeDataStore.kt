package com.grupo1.hoppi.communities

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDataStore(private val prefs: Preferences = emptyPreferences()) :
    DataStore<Preferences> {
    override val data: Flow<Preferences> = flowOf(prefs)
    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences = prefs
}