package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.RoutineItem
import com.example.ui.components.CategoryChip
import com.example.ui.theme.DeepPurple
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.LavenderBase
import com.example.ui.theme.LavenderClara
import com.example.ui.theme.LilacBackground
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun RoutineTabScreen(
    items: List<RoutineItem>,
    onAddRoutineClick: () -> Unit,
    onToggleCompleted: (RoutineItem) -> Unit,
    onDeleteRoutine: (RoutineItem) -> Unit,
    onTestNotification: (RoutineItem) -> Unit
) {
    var selectedDayIndex by remember { mutableStateOf(0) }

    // Weekday calendar days generator
    val daysList = remember {
        val calendar = Calendar.getInstance()
        val dayFormat = SimpleDateFormat("EEE", Locale("pt", "BR"))
        val numFormat = SimpleDateFormat("dd", Locale.getDefault())
        (0..6).map { i ->
            val calCopy = calendar.clone() as Calendar
            calCopy.add(Calendar.DAY_OF_YEAR, i)
            val dayName = dayFormat.format(calCopy.time).replace(".", "").replaceFirstChar { it.uppercase() }
            val dayNum = numFormat.format(calCopy.time)
            Pair(dayName, dayNum)
        }
    }

    val pendingCount = items.count { !it.isCompleted }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddRoutineClick,
                containerColor = DeepPurple,
                contentColor = LilacBackground,
                shape = RoundedCornerShape(20.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = "Novo compromisso") },
                text = { Text("Novo compromisso", fontWeight = FontWeight.Bold) }
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
                    text = "Rotina",
                    style = MaterialTheme.typography.titleMedium,
                    color = LavenderBase
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Sua semana, sem surpresas",
                    style = MaterialTheme.typography.displayMedium,
                    color = LilacBackground,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (pendingCount == 0) "Todos os compromissos concluídos por hoje!" else "$pendingCount compromissos hoje, nenhum atrasado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GoldenYellow,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Days Carousel Bar
            Surface(
                color = DeepPurple.copy(alpha = 0.95f),
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(daysList.size) { index ->
                        val (dayName, dayNum) = daysList[index]
                        val isSelected = index == selectedDayIndex

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) GoldenYellow else LavenderBase.copy(alpha = 0.2f))
                                .clickable { selectedDayIndex = index }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = dayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) DeepPurple else LilacBackground,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dayNum,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isSelected) DeepPurple else LilacBackground,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Routine Task List
            if (items.isEmpty()) {
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
                                text = "Nada marcado ainda. Bora preencher isso?",
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        RoutineItemCard(
                            item = item,
                            onToggleCompleted = { onToggleCompleted(item) },
                            onDelete = { onDeleteRoutine(item) },
                            onTestNotification = { onTestNotification(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineItemCard(
    item: RoutineItem,
    onToggleCompleted: () -> Unit,
    onDelete: () -> Unit,
    onTestNotification: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LilacBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LavenderBase.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggleCompleted() },
                colors = CheckboxDefaults.colors(
                    checkedColor = DeepPurple,
                    checkmarkColor = LilacBackground
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(LavenderClara)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.type,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = item.time,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else DeepPurple,
                    textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                if (item.description.isNotBlank()) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            IconButton(onClick = onTestNotification) {
                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = "Simular Alarme",
                    tint = GoldenYellow
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}
