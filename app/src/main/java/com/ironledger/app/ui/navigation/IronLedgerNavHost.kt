package com.ironledger.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ironledger.app.ui.screens.*

@Composable
fun IronLedgerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Destinations.DASHBOARD
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Destinations.ONBOARDING) { OnboardingScreen() }
        composable(Destinations.DASHBOARD) { 
            DashboardScreen(onNavigateToSleep = {
                navController.navigate(Destinations.SLEEP_LOG)
            }) 
        }
        composable(Destinations.FOOD_LOG) { FoodLogScreen() }
        composable(Destinations.WORKOUT_LOG) { WorkoutLogScreen() }
        composable(Destinations.PHOTOS) { PhotosScreen() }
        composable(Destinations.SETTINGS) { SettingsScreen() }
        composable(Destinations.SLEEP_LOG) { SleepLogScreen() }
        composable(Destinations.WEIGHT_LOG) { WeightLogScreen() }
        composable(Destinations.MEASUREMENTS_LOG) { MeasurementsLogScreen() }
        composable(Destinations.AI_COACH) { AICoachScreen() }
        composable(Destinations.MOTIVATION) { MotivationScreen() }
        composable(Destinations.PERSONAL_STREAK) { PersonalStreakScreen() }
        composable(Destinations.PROGRESS) { ProgressScreen() }
    }
}
