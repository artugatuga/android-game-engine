package com.innoveworkshop.angryflappingbird.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameHighestScore(private val context: Context ) {
    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("projectVotes")
        val VOTES = intPreferencesKey("votes")
    }

    val currentHighestScore: Flow<Int> =
        context.dataStore.data.map { preferences ->
            preferences[VOTES] ?: 0
        }

    suspend fun saveScore(votes: Int) {
        context.dataStore.edit { preferences ->
            preferences[VOTES] = votes
        }
    }
}
