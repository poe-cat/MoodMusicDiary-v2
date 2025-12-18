package com.example.moodmusic.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.api.RetrofitInstance
import com.example.moodmusic.model.Mood
import com.example.moodmusic.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.moodmusic.data.MoodPreferences

class MoodViewModel(application: Application) : AndroidViewModel(application) {

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _currentMood = MutableStateFlow<Mood?>(null)
    val currentMood = _currentMood.asStateFlow()

    private val prefs = MoodPreferences(getApplication())

    private val _surpriseMoods = MutableStateFlow<List<Mood>>(emptyList())
    val surpriseMoods = _surpriseMoods.asStateFlow()

    fun loadMood(mood: Mood) {
        viewModelScope.launch {
            _currentMood.value = mood
            _isLoading.value = true
            _error.value = null
            _tracks.value = emptyList()

            prefs.saveMood(mood.name)

            try {
                val allTracks = mutableListOf<Track>()

                for (tag in mood.tags) {
                    val response = RetrofitInstance.api.getTopTracksByTag(
                        tag = tag,
                        limit = 10,
                        apiKey = "4c0e716e5f6a272f9b32b5f038b5f907"
                    )
                    allTracks.addAll(response.tracks.trackList)
                }

                val uniqueTracks = allTracks
                    .distinctBy { "${it.name}-${it.artist.name}" }
                    .shuffled()

                _tracks.value = uniqueTracks

            } catch (e: Exception) {
                _error.value = "Could not load tracks. Try again."
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun restoreLastMood() {
        viewModelScope.launch {
            prefs.lastMood.collect { moodName ->
                moodName?.let {
                    val mood = Mood.valueOf(it)
                    _currentMood.value = mood
                    loadMood(mood)
                }
            }
        }
    }

    fun loadSurpriseMood() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _tracks.value = emptyList()
            _currentMood.value = null

            try {
                // Pick 3 different moods
                val selectedMoods = Mood.values().toList().shuffled().take(3)
                _surpriseMoods.value = selectedMoods

                // Pick 2 tags from each mood
                val selectedTags = selectedMoods.flatMap { mood ->
                    mood.tags.shuffled().take(2)
                }

                // Fetch tracks for all selected tags
                val allTracks = mutableListOf<Track>()

                for (tag in selectedTags) {
                    val response = RetrofitInstance.api.getTopTracksByTag(
                        tag = tag,
                        limit = 8,
                        apiKey = "4c0e716e5f6a272f9b32b5f038b5f907"
                    )
                    allTracks.addAll(response.tracks.trackList)
                }

                // Deduplicate + shuffle
                val uniqueTracks = allTracks
                    .distinctBy { "${it.name}-${it.artist.name}" }
                    .shuffled()

                _tracks.value = uniqueTracks

            } catch (e: Exception) {
                _error.value = "Could not load surprise mix"
            } finally {
                _isLoading.value = false
            }
        }
    }


}
