package com.ronny.naturenest.ui.screens.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ronny.naturenest.models.NatureNestRepository
import com.ronny.naturenest.models.Reminder
import com.ronny.naturenest.models.ReminderType
import com.ronny.naturenest.navigation.ROUT_REMINDER
import com.ronny.naturenest.ui.screens.components.NatureNestBottomBar
import com.ronny.naturenest.ui.screens.components.NatureNestTopBar
import com.ronny.naturenest.ui.screens.components.ReminderCard
import com.ronny.naturenest.ui.theme.*

@Composable
fun RemindersScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ROUT_REMINDER

    var reminders by remember {
        mutableStateOf(NatureNestRepository.getReminders())
    }
    var showAddDialog by remember { mutableStateOf(false) }

    val completedCount = reminders.count { it.isCompleted }

    Scaffold(
        containerColor = SurfaceCream,
        topBar = {
            NatureNestTopBar(
                title = "Reminders",
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add reminder", tint = Blush)
                    }
                }
            )
        },
        bottomBar = {
            NatureNestBottomBar(
                currentRoute = currentRoute,
                onNavigate = { navController.navigate(it) { launchSingleTop = true } }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp, )
        ) {
            // Progress summary
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlushLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✅", fontSize = 32.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "$completedCount of ${reminders.size} done today",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { if (reminders.isEmpty()) 0f else completedCount.toFloat() / reminders.size },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp),
                                color = Blush,
                                trackColor = BlushLight
                            )
                        }
                    }
                }
            }

            // Active reminders
            item {
                Text(
                    "Today",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            items(reminders.filter { !it.isCompleted }, key = { it.id }) { reminder ->
                ReminderCard(
                    reminder = reminder,
                    onToggle = {
                        reminders = reminders.map {
                            if (it.id == reminder.id) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Completed section
            val completedReminders = reminders.filter { it.isCompleted }
            if (completedReminders.isNotEmpty()) {
                item {
                    Text(
                        "Completed",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 10.dp)
                    )
                }
                items(completedReminders, key = { "done_${it.id}" }) { reminder ->
                    ReminderCard(
                        reminder = reminder,
                        onToggle = {
                            reminders = reminders.map {
                                if (it.id == reminder.id) it.copy(isCompleted = !it.isCompleted) else it
                            }
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }

    // Add reminder dialog
    if (showAddDialog) {
        AddReminderDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, time, type ->
                val newId = (reminders.maxOfOrNull { it.id } ?: 0) + 1
                reminders = reminders + Reminder(
                    id = newId,
                    title = title,
                    description = "",
                    time = time,
                    date = "Daily",
                    type = type
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddReminderDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, ReminderType) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(ReminderType.MEDICATION) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                "Add Reminder",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Reminder Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blush,
                        focusedLabelColor = Blush,
                        unfocusedBorderColor = CreamDark
                    )
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g. 8:00 AM)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blush,
                        focusedLabelColor = Blush,
                        unfocusedBorderColor = CreamDark
                    )
                )
                Text("Type", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ReminderType.values().take(3).forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text("${type.emoji} ${type.label}", style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BlushLight,
                                selectedLabelColor = BlushDark
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank()) onAdd(title, time.ifBlank { "TBD" }, selectedType) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blush)
            ) {
                Text("Add", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RemindersScreenPreview() {
    RemindersScreen(navController = rememberNavController())
}