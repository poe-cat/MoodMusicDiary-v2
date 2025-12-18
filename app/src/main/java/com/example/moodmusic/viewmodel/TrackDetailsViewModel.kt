package com.example.moodmusic.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.api.RetrofitInstance
import com.example.moodmusic.data.AppDatabase
import com.example.moodmusic.data.NoteEntity
import com.example.moodmusic.model.TrackInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrackDetailsViewModel : ViewModel() {


    // ---------- TRACK INFO STATE ----------
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _info = MutableStateFlow<TrackInfo?>(null)
    val info = _info.asStateFlow()


    // ---------- DATABASE ----------
    private var db: AppDatabase? = null

    fun init(context: Context) {
        if (db == null) {
            db = AppDatabase.get(context)
        }
    }


    // ---------- LOAD TRACK INFO ----------
    fun load(artist: String, track: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _info.value = null

            try {
                val response = RetrofitInstance.api.getTrackInfo(
                    artist = artist,
                    track = track,
                    apiKey = apiKey
                )
                _info.value = response.track
            } catch (e: Exception) {
                Log.e("TrackDetails", e.toString())
                _error.value = "Could not load track details"
            } finally {
                _isLoading.value = false
            }
        }
    }


    // ---------- SAVE NOTE ----------
    fun saveNote(
        artist: String,
        track: String,
        note: String,
        mood: String? = null
    ) {
        val database = db ?: return  //safety check

        viewModelScope.launch {
            database.noteDao().insert(
                NoteEntity(
                    artist = artist,
                    track = track,
                    note = note,
                    mood = mood
                )
            )
        }
    }
}
