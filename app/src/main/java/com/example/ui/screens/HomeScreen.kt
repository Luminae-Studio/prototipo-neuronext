package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FolderShared
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddDocumentDialog
import com.example.ui.components.AddJournalDialog
import com.example.ui.components.AddProfessionalDialog
import com.example.ui.components.AddRoutineDialog
import com.example.ui.components.EmergencySupportModal
import com.example.ui.components.ReminderNotificationDialog
import com.example.ui.components.SettingsAndFreemiumSheet
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.LavenderBase
import com.example.ui.theme.LavenderClara
import com.example.ui.theme.LilacBackground
import com.example.ui.viewmodel.NeuroNextViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: NeuroNextViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Dialog & Modal visibility states
    var showAddJournalDialog by remember { mutableStateOf(false) }
    var showAddRoutineDialog by remember { mutableStateOf(false) }
    var showAddDocDialog by remember { mutableStateOf(false) }
    var showAddProfDialog by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // State Collectors
    val journalEntries by viewModel.journalEntries.collectAsState()
    val routineItems by viewModel.routineItems.collectAsState()
    val healthDocuments by viewModel.healthDocuments.collectAsState()
    val professionals by viewModel.professionals.collectAsState()
    val showSensitiveModal by viewModel.showSensitiveSupportModal.collectAsState()
    val activeReminder by viewModel.activeReminder.collectAsState()

    // Observe toast events
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = DeepPurple,
                contentColor = LilacBackground,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                // Tab 0: Diário
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            if (selectedTab == 0) Icons.Filled.Book else Icons.Outlined.Book,
                            contentDescription = "Diário"
                        )
                    },
                    label = { Text("Diário", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepPurple,
                        selectedTextColor = GoldenYellow,
                        indicatorColor = GoldenYellow,
                        unselectedIconColor = LavenderBase,
                        unselectedTextColor = LavenderBase
                    )
                )

                // Tab 1: Rotina
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            if (selectedTab == 1) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                            contentDescription = "Rotina"
                        )
                    },
                    label = { Text("Rotina", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepPurple,
                        selectedTextColor = GoldenYellow,
                        indicatorColor = GoldenYellow,
                        unselectedIconColor = LavenderBase,
                        unselectedTextColor = LavenderBase
                    )
                )

                // Tab 2: Documentos
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            if (selectedTab == 2) Icons.Filled.FolderShared else Icons.Outlined.FolderShared,
                            contentDescription = "Pasta"
                        )
                    },
                    label = { Text("Pasta", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepPurple,
                        selectedTextColor = GoldenYellow,
                        indicatorColor = GoldenYellow,
                        unselectedIconColor = LavenderBase,
                        unselectedTextColor = LavenderBase
                    )
                )

                // Tab 3: Lyai
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            Icons.Filled.AutoAwesome,
                            contentDescription = "Lyai"
                        )
                    },
                    label = { Text("Lyai", fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepPurple,
                        selectedTextColor = GoldenYellow,
                        indicatorColor = GoldenYellow,
                        unselectedIconColor = LavenderBase,
                        unselectedTextColor = LavenderBase
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Crossfade(targetState = selectedTab, label = "TabCrossfade") { tab ->
                when (tab) {
                    0 -> JournalTabScreen(
                        entries = journalEntries,
                        greetingText = viewModel.getTimeGreeting(),
                        onAddEntryClick = { showAddJournalDialog = true },
                        onDeleteEntry = { viewModel.deleteJournalEntry(it) },
                        onToggleFavorite = { viewModel.toggleJournalFavorite(it) }
                    )

                    1 -> RoutineTabScreen(
                        items = routineItems,
                        onAddRoutineClick = { showAddRoutineDialog = true },
                        onToggleCompleted = { viewModel.toggleRoutineCompleted(it) },
                        onDeleteRoutine = { viewModel.deleteRoutineItem(it) },
                        onTestNotification = { viewModel.triggerSimulatedReminder(it) }
                    )

                    2 -> HealthDocsTabScreen(
                        documents = healthDocuments,
                        professionals = professionals,
                        onAddDocumentClick = { showAddDocDialog = true },
                        onAddProfessionalClick = { showAddProfDialog = true },
                        onDeleteDocument = { viewModel.deleteDocument(it) },
                        onDeleteProfessional = { viewModel.deleteProfessional(it) }
                    )

                    3 -> LyaiAssistantTabScreen(
                        onProcessMessyNote = { rawNote ->
                            viewModel.processMessyNoteWithLyai(rawNote) { type, title, content ->
                                if (type == "Tarefa" || type == "Consulta" || type == "Medicamento") {
                                    viewModel.addRoutineItem(title, content, type, "Hoje", "15:00", "Média", true)
                                } else {
                                    viewModel.addJournalEntry(title, content, type)
                                }
                            }
                        },
                        onOpenEmergencyModal = { viewModel.checkSensitiveContent("desespero") },
                        onOpenSettingsSheet = { showSettingsSheet = true }
                    )
                }
            }
        }
    }

    // Dialogs Management
    if (showAddJournalDialog) {
        AddJournalDialog(
            onDismiss = { showAddJournalDialog = false },
            onSave = { title, content, type ->
                viewModel.addJournalEntry(title, content, type)
            }
        )
    }

    if (showAddRoutineDialog) {
        AddRoutineDialog(
            onDismiss = { showAddRoutineDialog = false },
            onSave = { title, desc, type, date, time, priority, reminder ->
                viewModel.addRoutineItem(title, desc, type, date, time, priority, reminder)
            }
        )
    }

    if (showAddDocDialog) {
        AddDocumentDialog(
            onDismiss = { showAddDocDialog = false },
            onSave = { title, category, notes ->
                viewModel.addHealthDocument(title, category, notes)
            }
        )
    }

    if (showAddProfDialog) {
        AddProfessionalDialog(
            onDismiss = { showAddProfDialog = false },
            onSave = { name, spec, phone, email, notes ->
                viewModel.addProfessional(name, spec, phone, email, notes)
            }
        )
    }

    if (showSettingsSheet) {
        SettingsAndFreemiumSheet(
            onDismiss = { showSettingsSheet = false }
        )
    }

    // Sensitive Support Modal Trigger
    if (showSensitiveModal) {
        EmergencySupportModal(
            onDismiss = { viewModel.dismissSensitiveModal() }
        )
    }

    // Active Reminder Overlay Simulation
    activeReminder?.let { reminder ->
        ReminderNotificationDialog(
            item = reminder,
            onComplete = {
                viewModel.toggleRoutineCompleted(reminder)
                viewModel.dismissActiveReminder()
            },
            onSnooze = {
                viewModel.dismissActiveReminder(snooze = true)
            },
            onOpen = {
                selectedTab = 1
                viewModel.dismissActiveReminder()
            }
        )
    }
}
