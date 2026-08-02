package com.example.data.repository

import com.example.data.local.HealthDocument
import com.example.data.local.JournalEntry
import com.example.data.local.NeuroNextDao
import com.example.data.local.Professional
import com.example.data.local.RoutineItem
import kotlinx.coroutines.flow.Flow

class NeuroNextRepository(private val dao: NeuroNextDao) {
    val allJournalEntries: Flow<List<JournalEntry>> = dao.getAllJournalEntries()
    val allRoutineItems: Flow<List<RoutineItem>> = dao.getAllRoutineItems()
    val allHealthDocuments: Flow<List<HealthDocument>> = dao.getAllHealthDocuments()
    val allProfessionals: Flow<List<Professional>> = dao.getAllProfessionals()

    suspend fun insertJournal(entry: JournalEntry) = dao.insertJournalEntry(entry)
    suspend fun updateJournal(entry: JournalEntry) = dao.updateJournalEntry(entry)
    suspend fun deleteJournal(entry: JournalEntry) = dao.deleteJournalEntry(entry)
    suspend fun deleteJournalById(id: Long) = dao.deleteJournalEntryById(id)

    suspend fun insertRoutine(item: RoutineItem) = dao.insertRoutineItem(item)
    suspend fun updateRoutine(item: RoutineItem) = dao.updateRoutineItem(item)
    suspend fun deleteRoutine(item: RoutineItem) = dao.deleteRoutineItem(item)

    suspend fun insertDocument(doc: HealthDocument) = dao.insertHealthDocument(doc)
    suspend fun deleteDocument(doc: HealthDocument) = dao.deleteHealthDocument(doc)

    suspend fun insertProfessional(prof: Professional) = dao.insertProfessional(prof)
    suspend fun deleteProfessional(prof: Professional) = dao.deleteProfessional(prof)
}
