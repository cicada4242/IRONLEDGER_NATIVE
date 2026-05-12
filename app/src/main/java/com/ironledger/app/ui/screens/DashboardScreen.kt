package com.ironledger.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ironledger.app.ui.viewmodel.DashboardViewModel

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.ironledger.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToSleep: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "IRONLEDGER", 
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(color = PrimaryVibrant)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Bold Greeting
                Text(
                    text = buildAnnotatedString {
                        append("Hey, ")
                        withStyle(SpanStyle(color = PrimaryVibrant)) {
                            append(uiState.profile?.name ?: "Athlete")
                        }
                        append("!")
                    },
                    style = MaterialTheme.typography.headlineLarge
                )

                // Nutrition Summary Card (Premium Gradient)
                val totalCalories = uiState.todaysMeals.sumOf { it.calories as Int }
                val totalProtein = uiState.todaysMeals.sumOf { it.protein.toDouble() }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryVibrant.copy(alpha = 0.3f)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(CardGradientStart, CardGradientEnd)
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column {
                            Text(
                                "DAILY FUEL", 
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimaryVibrant,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.Bottom
                            ) {
                                Column {
                                    Text(
                                        text = "$totalCalories",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = Color.White
                                    )
                                    Text("kcal consumed", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
                                }
                                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                                    Text(
                                        text = "${String.format("%.1f", totalProtein)}g",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TealNeon
                                    )
                                    Text("protein", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
                                }
                            }
                        }
                    }
                }

                // Workout Section
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "TODAY'S GRIND",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryVibrant,
                        fontWeight = FontWeight.Black
                    )
                    
                    if (uiState.todaysWorkouts.isEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            color = Dark800,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                        ) {
                            Text(
                                "No active session. Rest day?",
                                modifier = Modifier.padding(20.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    } else {
                        uiState.todaysWorkouts.forEach { workout ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                                color = Dark800,
                                border = androidx.compose.foundation.BorderStroke(1.dp, SecondaryVibrant.copy(alpha = 0.2f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(20.dp),
                                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(SecondaryVibrant.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape),
                                        contentAlignment = androidx.compose.ui.Alignment.Center
                                    ) {
                                        Icon(
                                            androidx.compose.material.icons.Icons.Default.FitnessCenter,
                                            contentDescription = null,
                                            tint = SecondaryVibrant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(workout.workoutType, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        if (workout.isRestDay) {
                                            Text("Active Recovery", style = MaterialTheme.typography.bodySmall, color = GreenNeon)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Sleep Card
                Surface(
                    onClick = onNavigateToSleep,
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    color = Dark700.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2DD4BF).copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Brush.radialGradient(listOf(Color(0xFF2DD4BF), Color.Transparent)),
                                    androidx.compose.foundation.shape.CircleShape
                                ),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Icon(
                                androidx.compose.material.icons.Icons.Default.Nightlight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text("RECOVERY", style = MaterialTheme.typography.labelSmall, color = Color(0xFF2DD4BF), fontWeight = FontWeight.Black)
                            Text("Sleep Log", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Check your recovery stats", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
                        }
                    }
                }

                // Streak Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    color = Dark700.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TertiaryVibrant.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Brush.radialGradient(listOf(TertiaryVibrant, Color.Transparent)),
                                    androidx.compose.foundation.shape.CircleShape
                                ),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Icon(
                                androidx.compose.material.icons.Icons.Default.Whatshot,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text("CONSISTENCY", style = MaterialTheme.typography.labelSmall, color = TertiaryVibrant, fontWeight = FontWeight.Black)
                            Text("Personal Streak", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            val startDate = uiState.habitTracker?.startDate ?: "Ready to start?"
                            Text(startDate, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.6f))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
