package com.ironledger.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun OnboardingScreen() { PlaceholderScreen("Onboarding") }




@Composable
fun PhotosScreen() { PlaceholderScreen("Photos") }

@Composable
fun SettingsScreen() { PlaceholderScreen("Settings") }

@Composable
fun SleepLogScreen() { PlaceholderScreen("Sleep Log") }

@Composable
fun MeasurementsLogScreen() { PlaceholderScreen("Measurements") }


@Composable
fun MotivationScreen() { PlaceholderScreen("Motivation") }

@Composable
fun PersonalStreakScreen() { PlaceholderScreen("Personal Streak") }

@Composable
fun ProgressScreen() { PlaceholderScreen("Progress") }

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title)
    }
}
