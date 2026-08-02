package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_documents")
data class HealthDocument(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "Laudo", // Laudo, Exame Neuro, Receita, RG, CNH, Outro
    val fileUri: String = "",
    val notes: String = "",
    val dateAdded: Long = System.currentTimeMillis()
)
