package com.ironledger.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ironledger.app.data.local.entity.WorkoutEntity
import com.ironledger.app.ui.theme.*
import com.ironledger.app.ui.viewmodel.WorkoutLogViewModel

@Composable
fun WorkoutLogScreen(
    viewModel: WorkoutLogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Dark900)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
        ) {
            item {
                Text(
                    text = "WORKOUT LOG",
                    style = MaterialTheme.typography.labelSmall,
                    color = SecondaryVibrant,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Today's Grind",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (uiState.workouts.isEmpty()) {
                item {
                    EmptyWorkoutsPlaceholder()
                }
            } else {
                items(uiState.workouts) { workout ->
                    WorkoutItemCard(workout)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Floating Action Button
        LargeFloatingActionButton(
            onClick = { /* TODO: Open Workout Session */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = SecondaryVibrant,
            contentColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Start Workout", modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun WorkoutItemCard(workout: WorkoutEntity) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Dark700, Dark800)
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SecondaryVibrant.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = SecondaryVibrant, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = workout.workoutType,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            workout.exercises.forEach { exercise ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${exercise.sets.size} sets",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryVibrant,
                        fontWeight = FontWeight.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Completed at ${workout.completedAt}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun EmptyWorkoutsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.1f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No active session. Rest day?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}
