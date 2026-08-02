package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.HealthDocument
import com.example.data.local.JournalEntry
import com.example.data.local.Professional
import com.example.data.local.RoutineItem
import com.example.data.repository.NeuroNextRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NeuroNextViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NeuroNextRepository

    val journalEntries: StateFlow<List<JournalEntry>>
    val routineItems: StateFlow<List<RoutineItem>>
    val healthDocuments: StateFlow<List<HealthDocument>>
    val professionals: StateFlow<List<Professional>>

    // Sensitive support alert state
    private val _showSensitiveSupportModal = MutableStateFlow(false)
    val showSensitiveSupportModal: StateFlow<Boolean> = _showSensitiveSupportModal.asStateFlow()

    // Active Reminder Popup simulation state
    private val _activeReminder = MutableStateFlow<RoutineItem?>(null)
    val activeReminder: StateFlow<RoutineItem?> = _activeReminder.asStateFlow()

    // Onboarding flow state
    private val _hasCompletedOnboarding = MutableStateFlow(false)
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    // Toast / Feedback message flow
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    init {
        val dao = AppDatabase.getDatabase(application).dao()
        repository = NeuroNextRepository(dao)

        journalEntries = repository.allJournalEntries.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        routineItems = repository.allRoutineItems.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        healthDocuments = repository.allHealthDocuments.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
        professionals = repository.allProfessionals.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )

        // Seed initial sample data if database is empty
        seedSampleDataIfEmpty()
    }

    private fun seedSampleDataIfEmpty() {
        viewModelScope.launch {
            // Check if seeding is needed
            // Sample professionals
            repository.insertProfessional(
                Professional(
                    name = "Dra. Ana Silva",
                    specialty = "Psicóloga TCC",
                    phone = "(11) 98765-4321",
                    email = "ana.silva@psico.com",
                    notes = "Atendimento quinzenal às terças"
                )
            )

            // Sample documents
            repository.insertDocument(
                HealthDocument(
                    title = "Laudo de Acompanhamento Neuropsicológico",
                    category = "Laudo",
                    notes = "Emitido em Março/2026 com orientações de rotina."
                )
            )
            repository.insertDocument(
                HealthDocument(
                    title = "Exame de Perfil Neurodivergente",
                    category = "Exame Neuro",
                    notes = "Guardado para consulta rápida nas sessões."
                )
            )

            // Sample routine items
            val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            repository.insertRoutine(
                RoutineItem(
                    title = "Consulta com Dra. Ana",
                    description = "Sessão quinzenal de terapia",
                    type = "Consulta",
                    date = todayStr,
                    time = "14:30",
                    priority = "Alta",
                    reminderEnabled = true
                )
            )
            repository.insertRoutine(
                RoutineItem(
                    title = "Pausa de 15 min para respiração",
                    description = "Desconectar do computador e caminhar",
                    type = "Tarefa",
                    date = todayStr,
                    time = "16:00",
                    priority = "Média",
                    reminderEnabled = true
                )
            )

            // Sample journal entry
            repository.insertJournal(
                JournalEntry(
                    title = "Organizando pensamentos da manhã",
                    content = "Hoje acordei com a cabeça cheia de tarefas soltas. Escrever aqui ajuda a tirar o peso antes do trabalho.",
                    type = "Pensamento"
                )
            )
        }
    }

    // --- SENSITIVE CONTENT DETECTOR ---
    fun checkSensitiveContent(text: String): Boolean {
        val sensitiveKeywords = listOf(
            "suicídio", "suicidio", "desespero", "sem esperança", "sem esperanca",
            "quero morrer", "quero sumir", "não aguento mais", "nao aguento mais",
            "autoflagelação", "corte", "me matar", "dar fim", "vazio insuportável"
        )
        val isSensitive = sensitiveKeywords.any { text.lowercase(Locale.getDefault()).contains(it) }
        if (isSensitive) {
            _showSensitiveSupportModal.value = true
        }
        return isSensitive
    }

    fun dismissSensitiveModal() {
        _showSensitiveSupportModal.value = false
    }

    // --- JOURNAL ACTIONS ---
    fun addJournalEntry(title: String, content: String, type: String) {
        viewModelScope.launch {
            val isSensitive = checkSensitiveContent(content) || checkSensitiveContent(title)
            val entry = JournalEntry(
                title = title.ifBlank { "Sem título" },
                content = content,
                type = type,
                hasSensitiveContent = isSensitive
            )
            repository.insertJournal(entry)
            _toastEvent.emit("Feito. Já tá seguro com a gente.")
        }
    }

    fun deleteJournalEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.deleteJournal(entry)
            _toastEvent.emit("Registro removido.")
        }
    }

    fun toggleJournalFavorite(entry: JournalEntry) {
        viewModelScope.launch {
            repository.updateJournal(entry.copy(isFavorite = !entry.isFavorite))
        }
    }

    // --- ROUTINE ACTIONS ---
    fun addRoutineItem(
        title: String,
        description: String,
        type: String,
        date: String,
        time: String,
        priority: String,
        reminderEnabled: Boolean
    ) {
        viewModelScope.launch {
            val item = RoutineItem(
                title = title,
                description = description,
                type = type,
                date = date,
                time = time,
                priority = priority,
                reminderEnabled = reminderEnabled
            )
            repository.insertRoutine(item)
            _toastEvent.emit("Marcado. Eu aviso na hora certa.")
        }
    }

    fun toggleRoutineCompleted(item: RoutineItem) {
        viewModelScope.launch {
            repository.updateRoutine(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun deleteRoutineItem(item: RoutineItem) {
        viewModelScope.launch {
            repository.deleteRoutine(item)
            _toastEvent.emit("Compromisso removido.")
        }
    }

    fun triggerSimulatedReminder(item: RoutineItem) {
        _activeReminder.value = item
    }

    fun dismissActiveReminder(snooze: Boolean = false) {
        viewModelScope.launch {
            if (snooze) {
                _toastEvent.emit("Tudo bem, todo mundo merece um respiro. Te aviso de novo daqui a pouco.")
            }
            _activeReminder.value = null
        }
    }

    // --- HEALTH DOCUMENTS & PROFESSIONALS ---
    fun addHealthDocument(title: String, category: String, notes: String) {
        viewModelScope.launch {
            val doc = HealthDocument(
                title = title,
                category = category,
                notes = notes
            )
            repository.insertDocument(doc)
            _toastEvent.emit("Guardado. Fácil de achar quando você precisar.")
        }
    }

    fun deleteDocument(doc: HealthDocument) {
        viewModelScope.launch {
            repository.deleteDocument(doc)
            _toastEvent.emit("Documento removido.")
        }
    }

    fun addProfessional(name: String, specialty: String, phone: String, email: String, notes: String) {
        viewModelScope.launch {
            val prof = Professional(
                name = name,
                specialty = specialty,
                phone = phone,
                email = email,
                notes = notes
            )
            repository.insertProfessional(prof)
            _toastEvent.emit("Profissional adicionado(a).")
        }
    }

    fun deleteProfessional(prof: Professional) {
        viewModelScope.launch {
            repository.deleteProfessional(prof)
            _toastEvent.emit("Profissional removido(a).")
        }
    }

    // --- LYAI COGNITIVE ASSISTANT (PARSER & HELPER) ---
    fun processMessyNoteWithLyai(rawNote: String, onParsed: (type: String, title: String, content: String) -> Unit) {
        viewModelScope.launch {
            checkSensitiveContent(rawNote)
            val lower = rawNote.lowercase(Locale.getDefault())

            val detectedType = when {
                lower.contains("consulta") || lower.contains("médico") || lower.contains("terapia") || lower.contains("dra") -> "Consulta"
                lower.contains("remédio") || lower.contains("medicamento") || lower.contains("tomar") -> "Medicamento"
                lower.contains("lembrar") || lower.contains("fazer") || lower.contains("tarefa") -> "Tarefa"
                else -> "Pensamento"
            }

            val extractedTitle = rawNote.lines().firstOrNull()?.take(40) ?: "Anotação rápida"
            onParsed(detectedType, extractedTitle, rawNote)
            _toastEvent.emit("Lyai organizou sua anotação!")
        }
    }

    // --- ONBOARDING & SETTINGS ---
    fun completeOnboarding() {
        _hasCompletedOnboarding.value = true
    }

    fun getTimeGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Bom dia. Bora ver o que rolou hoje?"
            in 12..17 -> "Boa tarde. Bora ver o que rolou hoje?"
            else -> "Boa noite. Bora ver o que rolou hoje?"
        }
    }
}
