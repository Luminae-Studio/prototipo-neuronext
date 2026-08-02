package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_items")
data class RoutineItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val type: String = "Tarefa", // Tarefa, Consulta, Medicamento, Meta
    val date: String, // YYYY-MM-DD or readable formatted date
    val time: String = "09:00",
    val priority: String = "Média", // Baixa, Média, Alta
    val isCompleted: Boolean = false,
    val reminderEnabled: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
