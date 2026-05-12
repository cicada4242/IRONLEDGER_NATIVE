package com.ironledger.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ironledger.app.ui.theme.*
import com.ironledger.app.ui.viewmodel.WeightLogViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightLogScreen(
    viewModel: WeightLogViewModel = hiltViewModel()
) {
    val weightEntries by viewModel.weightEntries.collectAsStateWithLifecycle()
    var weightInput by remember { mutableStateOf("") }
    
    val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    val todaysEntry = weightEntries.find { it.date == today }
    
    LaunchedEffect(todaysEntry) {
        if (todaysEntry != null && weightInput.isEmpty()) {
            weightInput = todaysEntry.weight.toString()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MonitorWeight, contentDescription = null, tint = TealNeon)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Weight Log", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Input Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Dark800,
                border = androidx.compose.foundation.BorderStroke(1.dp, TealNeon.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Today's Weigh-in", style = MaterialTheme.typography.titleSmall, color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = weightInput,
                            onValueChange = { weightInput = it },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            placeholder = { Text("0.0") },
                            suffix = { Text("kg") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TealNeon,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
                            )
                        )
                        
                        Button(
                            onClick = { 
                                weightInput.toFloatOrNull()?.let { viewModel.saveWeight(it) }
                            },
                            enabled = weightInput.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = TealNeon),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text(if (todaysEntry != null) "Update" else "Save", color = Dark900, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Stats Cards
            val latestWeight = weightEntries.lastOrNull()?.weight
            val avgWeight = if (weightEntries.size >= 7) {
                weightEntries.takeLast(7).map { it.weight }.average()
            } else if (weightEntries.isNotEmpty()) {
                weightEntries.map { it.weight }.average()
            } else null

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Current",
                    value = latestWeight?.toString() ?: "—",
                    unit = "kg"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "7-Day Avg",
                    value = avgWeight?.let { String.format("%.1f", it) } ?: "—",
                    unit = "kg",
                    valueColor = TealNeon
                )
            }

            Text("Recent Entries", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            
            if (weightEntries.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No entries yet. Log your first weigh-in above!", color = Color.White.copy(alpha = 0.5f))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // Leave space for Bottom Nav
                ) {
                    items(weightEntries.reversed()) { entry ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Dark800
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(entry.date, color = Color.White.copy(alpha = 0.7f))
                                Text("${entry.weight} kg", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, unit: String, valueColor: Color = Color.White) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Dark800,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = valueColor)
                Spacer(modifier = Modifier.width(4.dp))
                Text(unit, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f), modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}
