package com.ironledger.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ironledger.app.ui.components.*
import com.ironledger.app.ui.theme.Dark800
import com.ironledger.app.ui.theme.PrimaryVibrant
import com.ironledger.app.ui.theme.TealNeon
import com.ironledger.app.ui.viewmodel.SleepLogViewModel
import com.ironledger.app.utils.Calculations
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepLogScreen(
    viewModel: SleepLogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showBedtimePicker by remember { mutableStateOf(false) }
    var showWakeTimePicker by remember { mutableStateOf(false) }
    
    var selectedBedtime by remember { mutableStateOf("22:30") }
    var selectedWakeTime by remember { mutableStateOf("06:30") }
    var note by remember { mutableStateOf("") }
    
    val currentDuration = Calculations.calculateSleepDuration(selectedBedtime, selectedWakeTime)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                SectionLabel("Sleep Log")
                Text(
                    "Recovery is part of the program",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }

        // Summary Card
        item {
            IronLedgerCard {
                Column {
                    Text(
                        "7-DAY AVERAGE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${"%.1f".format(uiState.sevenDayAverage)} hours",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TealNeon,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Input Card
        item {
            IronLedgerCard {
                Column {
                    SectionLabel("LOG LAST NIGHT", color = Color.White.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TimeSelectionButton(
                            label = "Bedtime",
                            time = selectedBedtime,
                            onClick = { showBedtimePicker = true },
                            modifier = Modifier.weight(1f)
                        )
                        
                        TimeSelectionButton(
                            label = "Wake Up",
                            time = selectedWakeTime,
                            onClick = { showWakeTimePicker = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total Sleep",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${"%.1f".format(currentDuration)}h",
                            color = TealNeon,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Note (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryVibrant,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                            focusedLabelColor = PrimaryVibrant,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.4f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Button(
                        onClick = {
                            viewModel.saveSleepEntry(selectedBedtime, selectedWakeTime, note)
                            note = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryVibrant),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Save Sleep Log", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // History Title
        item {
            SectionLabel("HISTORY", color = Color.White.copy(alpha = 0.6f))
        }

        // History List
        items(uiState.entries) { entryUi ->
            IronLedgerCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            entryUi.entity.date,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${entryUi.displayBedtime} - ${entryUi.displayWakeTime}",
                            color = Color.White.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (entryUi.entity.note.isNotEmpty()) {
                            Text(
                                entryUi.entity.note,
                                color = Color.White.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "${"%.1f".format(entryUi.durationHours)}h",
                            color = entryUi.qualityColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            entryUi.quality.label,
                            color = entryUi.qualityColor.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }

    if (showBedtimePicker) {
        SleepTimePickerDialog(
            title = "WHEN DID YOU GO TO BED?",
            initialTime = selectedBedtime,
            onTimeSelected = { 
                selectedBedtime = it
                showBedtimePicker = false 
            },
            onDismiss = { showBedtimePicker = false }
        )
    }
    
    if (showWakeTimePicker) {
        SleepTimePickerDialog(
            title = "WHEN DID YOU WAKE UP?",
            initialTime = selectedWakeTime,
            onTimeSelected = { 
                selectedWakeTime = it
                showWakeTimePicker = false 
            },
            onDismiss = { showWakeTimePicker = false }
        )
    }
}

@Composable
fun TimeSelectionButton(
    label: String,
    time: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.4f)
            )
            Text(
                time,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimePickerDialog(
    title: String,
    initialTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val time = try {
        LocalTime.parse(initialTime)
    } catch (e: Exception) {
        LocalTime.of(22, 30)
    }
    
    val state = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { 
                onTimeSelected("%02d:%02d".format(state.hour, state.minute)) 
            }) {
                Text("SET", color = TealNeon, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color.White.copy(alpha = 0.6f))
            }
        },
        title = {
            Text(
                title,
                style = MaterialTheme.typography.labelSmall,
                color = TealNeon,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Dark800,
                        clockDialSelectedContentColor = Color(0xFF0F172A), // Dark 900
                        clockDialUnselectedContentColor = Color.White,
                        selectorColor = TealNeon,
                        containerColor = Color(0xFF0F172A),
                        periodSelectorBorderColor = TealNeon.copy(alpha = 0.3f)
                    )
                )
            }
        },
        containerColor = Color(0xFF0F172A),
        shape = MaterialTheme.shapes.extraLarge
    )
}
