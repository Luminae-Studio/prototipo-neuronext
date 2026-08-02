package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NeuroNextDao {
    // Journal Entries
    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntry): Long

    @Update
    suspend fun updateJournalEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteJournalEntry(entry: JournalEntry)

    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteJournalEntryById(id: Long)

    // Routine Items
    @Query("SELECT * FROM routine_items ORDER BY isCompleted ASC, timestamp DESC")
    fun getAllRoutineItems(): Flow<List<RoutineItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutineItem(item: RoutineItem): Long

    @Update
    suspend fun updateRoutineItem(item: RoutineItem)

    @Delete
    suspend fun deleteRoutineItem(item: RoutineItem)

    // Health Documents
    @Query("SELECT * FROM health_documents ORDER BY dateAdded DESC")
    fun getAllHealthDocuments(): Flow<List<HealthDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthDocument(doc: HealthDocument): Long

    @Delete
    suspend fun deleteHealthDocument(doc: HealthDocument)

    // Professionals
    @Query("SELECT * FROM professionals ORDER BY id DESC")
    fun getAllProfessionals(): Flow<List<Professional>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfessional(prof: Professional): Long

    @Delete
    suspend fun deleteProfessional(prof: Professional)
}
