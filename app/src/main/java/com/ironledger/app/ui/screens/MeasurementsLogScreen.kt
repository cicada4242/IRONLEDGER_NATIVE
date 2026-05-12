package com.ironledger.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ironledger.app.ui.components.*
import com.ironledger.app.ui.theme.*
import com.ironledger.app.ui.viewmodel.MeasurementsLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementsLogScreen(
    viewModel: MeasurementsLogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogSheet by remember { mutableStateOf(false) }

    val measurementTypes = listOf("Neck", "Chest", "Waist", "Hips", "Thighs", "Biceps")

    Scaffold(
        floatingActionButton = {
            AnimatedFAB(onClick = { showLogSheet = true })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        SectionLabel("Measurements")
                        Text(
                            "Tracking dimensions, not just weight",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    
                    // Navy Body Fat Badge
                    if (uiState.bodyFatPercentage > 0) {
                        Surface(
                            color = TealNeon.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TealNeon.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("BODY FAT", style = MaterialTheme.typography.labelSmall, color = TealNeon)
                                Text("${"%.1f".format(uiState.bodyFatPercentage)}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Type Selector Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(measurementTypes) { type ->
                        val isSelected = uiState.selectedType == type
                        Surface(
                            onClick = { viewModel.selectType(type) },
                            color = if (isSelected) PrimaryVibrant else Dark800,
                            shape = CircleShape,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) PrimaryVibrant else Color.White.copy(alpha = 0.1f))
                        ) {
                            Text(
                                type,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Chart Section
            item {
                IronLedgerCard {
                    val info = uiState.typeInfos[uiState.selectedType]
                    if (info == null || info.history.size < 2) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Log at least 2 entries to see trends",
                                color = Color.White.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column {
                                    Text(
                                        "${"%.1f".format(info.latestValue)} cm",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                    Text(
                                        "LATEST MEASUREMENT",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.4f)
                                    )
                                }
                                
                                val change = info.percentChange
                                Text(
                                    text = (if (change >= 0) "+" else "") + "${"%.1f".format(change)}%",
                                    color = if (change <= 0) GreenNeon else RedNeon,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Simple Canvas Line Chart
                            MeasurementLineChart(
                                history = info.history.map { it.value },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }
                    }
                }
            }

            // History List
            item {
                SectionLabel("HISTORY", color = Color.White.copy(alpha = 0.4f))
            }

            val currentHistory = uiState.typeInfos[uiState.selectedType]?.history?.reversed() ?: emptyList()
            items(currentHistory) { entry ->
                IronLedgerCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(entry.date, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(uiState.selectedType, color = Color.White.copy(alpha = 0.4f), style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            "${entry.value} cm",
                            color = TealNeon,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }

    if (showLogSheet) {
        LogMeasurementsSheet(
            onDismiss = { showLogSheet = false },
            onSave = { 
                viewModel.saveMeasurements(it)
                showLogSheet = false
            }
        )
    }
}

@Composable
fun MeasurementLineChart(
    history: List<Float>,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        if (history.size < 2) return@Canvas
        
        val min = history.minOrNull() ?: 0f
        val max = history.maxOrNull() ?: 1f
        val range = (max - min).coerceAtLeast(1f)
        
        val width = size.width
        val height = size.height
        val stepX = width / (history.size - 1)
        
        val path = Path()
        history.forEachIndexed { i, value ->
            val x = i * stepX
            val y = height - ((value - min) / range * height)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        
        drawPath(
            path = path,
            color = PrimaryVibrant,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogMeasurementsSheet(
    onDismiss: () -> Unit,
    onSave: (Map<String, String>) -> Unit
) {
    val types = listOf("Neck", "Chest", "Waist", "Hips", "Thighs", "Biceps")
    val values = remember { mutableStateMapOf<String, String>() }

    IronLedgerBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                "LOG SESSION",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Text(
                "Only fill what you measured today",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                types.chunked(2).forEach { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        pair.forEach { type ->
                            OutlinedTextField(
                                value = values[type] ?: "",
                                onValueChange = { values[type] = it },
                                label = { Text(type) },
                                suffix = { Text("cm") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryVibrant,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                                )
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { onSave(values.toMap()) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryVibrant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("SAVE MEASUREMENTS", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
