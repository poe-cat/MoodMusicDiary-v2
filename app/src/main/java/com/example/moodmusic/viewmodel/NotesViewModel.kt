package com.example.moodmusic.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.AppDatabase
import com.example.moodmusic.data.NoteEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(context: Context) : ViewModel() {

    private val db = AppDatabase.get(context)

    val notes = db.noteDao()
        .getAllNotes()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun delete(note: NoteEntity) {
        viewModelScope.launch {
            db.noteDao().deleteById(note.id)
        }
    }

    fun update(note: NoteEntity, newText: String) {
        viewModelScope.launch {
            db.noteDao().updateNote(note.id, newText)
        }
    }

}
