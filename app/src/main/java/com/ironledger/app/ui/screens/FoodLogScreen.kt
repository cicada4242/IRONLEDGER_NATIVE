package com.ironledger.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
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
import com.ironledger.app.data.local.entity.MealEntryEntity
import com.ironledger.app.ui.theme.*
import com.ironledger.app.ui.viewmodel.FoodLogViewModel

@Composable
fun FoodLogScreen(
    viewModel: FoodLogViewModel = hiltViewModel()
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
                    text = "FOOD LOG",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryVibrant,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Daily Fuel",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Macro Summary Card
            item {
                MacroSummaryCard(
                    calories = uiState.totalCalories,
                    protein = uiState.totalProtein,
                    carbs = uiState.totalCarbs,
                    fat = uiState.totalFat
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = "MEALS",
                    style = MaterialTheme.typography.labelSmall,
                    color = SecondaryVibrant,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.meals.isEmpty()) {
                item {
                    EmptyMealsPlaceholder()
                }
            } else {
                items(uiState.meals) { meal ->
                    MealItemRow(meal)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        // Floating Action Button
        LargeFloatingActionButton(
            onClick = { /* TODO: Open Add Food Dialog */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = PrimaryVibrant,
            contentColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Food", modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun MacroSummaryCard(calories: Int, protein: Float, carbs: Float, fat: Float) {
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
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "TOTAL ENERGY",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = calories.toString(),
                        fontSize = 42.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "kcal consumed",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color.White.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroItem("PROTEIN", "${protein}g", PrimaryVibrant)
                MacroItem("CARBS", "${carbs}g", SecondaryVibrant)
                MacroItem("FAT", "${fat}g", TertiaryVibrant)
            }
        }
    }
}

@Composable
fun MacroItem(label: String, value: String, color: Color) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Black
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MealItemRow(meal: MealEntryEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Dark800.copy(alpha = 0.5f))
            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PrimaryVibrant.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Restaurant, contentDescription = null, tint = PrimaryVibrant, modifier = Modifier.size(24.dp))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f) ) {
            Text(
                text = meal.foodName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${meal.mealType} • ${meal.displayQuantity} ${meal.displayUnit}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
        
        Text(
            text = "${meal.calories} kcal",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun EmptyMealsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No meals logged yet. Start fueling!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.3f)
        )
    }
}
