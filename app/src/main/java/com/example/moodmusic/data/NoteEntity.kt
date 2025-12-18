package com.example.moodmusic.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val artist: String,
    val track: String,
    val note: String,
    val mood: String?,
    val timestamp: Long = System.currentTimeMillis()
)
