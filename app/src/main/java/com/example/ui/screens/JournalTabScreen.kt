package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.JournalEntry
import com.example.ui.components.CategoryChip
import com.example.ui.components.LyaiBubbleCard
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.LavenderBase
import com.example.ui.theme.LavenderClara
import com.example.ui.theme.LilacBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JournalTabScreen(
    entries: List<JournalEntry>,
    greetingText: String,
    onAddEntryClick: () -> Unit,
    onDeleteEntry: (JournalEntry) -> Unit,
    onToggleFavorite: (JournalEntry) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    var entryToDelete by remember { mutableStateOf<JournalEntry?>(null) }
    var entryToShare by remember { mutableStateOf<JournalEntry?>(null) }

    val context = LocalContext.current
    val filters = listOf("Todos", "Pensamento", "Sessão de terapia", "Ideia")

    val filteredEntries = when (selectedFilter) {
        "Todos" -> entries
        else -> entries.filter { it.type == selectedFilter }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddEntryClick,
                containerColor = DeepPurple,
                contentColor = LilacBackground,
                shape = RoundedCornerShape(20.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = "Registrar") },
                text = { Text("Registrar", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DeepPurple)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = greetingText,
                    style = MaterialTheme.typography.titleMedium,
                    color = LavenderBase
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "O que tá passando pela sua cabeça?",
                    style = MaterialTheme.typography.displayMedium,
                    color = LilacBackground,
                    fontWeight = FontWeight.Bold
                )
            }

            // Quick Widget
            Box(modifier = Modifier.padding(16.dp)) {
                LyaiBubbleCard(
                    text = "Toca aqui e desabafa. Eu guardo.",
                    actionButton = {
                        OutlinedButton(
                            onClick = onAddEntryClick,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.EditNote, contentDescription = null, tint = DeepPurple)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Escrever um pensamento", color = DeepPurple, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }

            // Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                items(filters) { filter ->
                    CategoryChip(
                        text = filter,
                        isSelected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            // Empty State or List
            if (filteredEntries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.2.dp, LavenderBase, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        color = LilacBackground
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Ainda não tem nada por aqui. Um pensamento solto já é um bom começo.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = DeepPurple,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredEntries, key = { it.id }) { entry ->
                        JournalEntryCard(
                            entry = entry,
                            onToggleFavorite = { onToggleFavorite(entry) },
                            onDelete = { entryToDelete = entry },
                            onShare = {
                                entryToShare = entry
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, entry.title)
                                    putExtra(Intent.EXTRA_TEXT, "Pensamento do Neuro Next:\n\n${entry.title}\n\n${entry.content}")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Compartilhar este pensamento?"))
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            title = {
                Text(
                    text = "Tem certeza?",
                    fontWeight = FontWeight.Bold,
                    color = DeepPurple
                )
            },
            text = {
                Text(
                    text = "Essa aqui não tem volta.",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        entryToDelete?.let { onDeleteEntry(it) }
                        entryToDelete = null
                    }
                ) {
                    Text("Excluir", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { entryToDelete = null }) {
                    Text("Cancelar", color = DeepPurple)
                }
            },
            containerColor = LilacBackground,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun JournalEntryCard(
    entry: JournalEntry,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val dateFormatted = remember(entry.timestamp) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(entry.timestamp))
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LilacBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LavenderBase.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(LavenderClara)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = entry.type,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = dateFormatted,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opções", tint = DeepPurple)
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(LilacBackground)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Compartilhar este pensamento?") },
                            onClick = {
                                menuExpanded = false
                                onShare()
                            },
                            leadingIcon = { Icon(Icons.Default.Share, contentDescription = null, tint = DeepPurple) }
                        )
                        DropdownMenuItem(
                            text = { Text(if (entry.isFavorite) "Remover favorito" else "Favoritar") },
                            onClick = {
                                menuExpanded = false
                                onToggleFavorite()
                            },
                            leadingIcon = {
                                Icon(
                                    if (entry.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = DeepPurple
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Excluir", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (entry.title.isNotBlank()) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepPurple
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )
        }
    }
}
