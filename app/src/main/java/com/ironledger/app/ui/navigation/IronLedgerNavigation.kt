package com.ironledger.app.ui.navigation

import androidx.navigation.NavHostController

object Destinations {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val FOOD_LOG = "food_log"
    const val WORKOUT_LOG = "workout_log"
    const val PHOTOS = "photos"
    const val SETTINGS = "settings"
    const val SLEEP_LOG = "sleep_log"
    const val WEIGHT_LOG = "weight_log"
    const val MEASUREMENTS_LOG = "measurements_log"
    const val AI_COACH = "ai_coach"
    const val MOTIVATION = "motivation"
    const val PERSONAL_STREAK = "personal_streak"
    const val PROGRESS = "progress"
}

class NavigationActions(private val navController: NavHostController) {
    fun navigateTo(destination: String) {
        navController.navigate(destination) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}
