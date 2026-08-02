package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val content: String,
    val type: String = "Pensamento", // Pensamento, Sessão de terapia, Ideia
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isAudioRecording: Boolean = false,
    val hasSensitiveContent: Boolean = false
)
