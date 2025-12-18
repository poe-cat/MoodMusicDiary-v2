package com.example.moodmusic.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "mood_prefs")

class MoodPreferences(private val context: Context) {

    companion object {
        private val MOOD_KEY = stringPreferencesKey("last_mood")
    }

    val lastMood: Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[MOOD_KEY]
        }

    suspend fun saveMood(mood: String) {
        context.dataStore.edit { prefs ->
            prefs[MOOD_KEY] = mood
        }
    }
}
