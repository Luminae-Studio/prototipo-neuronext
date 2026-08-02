package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "professionals")
data class Professional(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val specialty: String = "Psicólogo(a)", // Psicólogo(a), Psiquiatra, Neurologista, Terapeuta
    val phone: String = "",
    val email: String = "",
    val notes: String = ""
)
