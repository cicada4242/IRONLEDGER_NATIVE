package com.ironledger.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ironledger.app.ui.navigation.Destinations
import com.ironledger.app.ui.navigation.IronLedgerNavHost
import com.ironledger.app.ui.theme.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Destinations.DASHBOARD, "Home", Icons.Default.Home),
    BottomNavItem(Destinations.FOOD_LOG, "Food", Icons.Default.Restaurant),
    BottomNavItem(Destinations.WORKOUT_LOG, "Workout", Icons.Default.FitnessCenter),
    BottomNavItem(Destinations.WEIGHT_LOG, "Weight", Icons.Default.MonitorWeight),
    BottomNavItem(Destinations.PERSONAL_STREAK, "Streak", Icons.Default.Whatshot),
    BottomNavItem(Destinations.AI_COACH, "AI", Icons.Default.SmartToy)
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val coroutineScope = rememberCoroutineScope()

    // Map routes to indices for the pager
    val pagerRoutes = bottomNavItems.map { it.route }
    val pagerState = rememberPagerState(pageCount = { pagerRoutes.size })

    // Sync Pager with NavHost / BottomNav
    val isMainTab = currentDestination?.route in pagerRoutes
    val showBottomBar = currentDestination?.route != Destinations.ONBOARDING

    // Update pager index when nav changes (e.g. from a deep link or direct nav)
    LaunchedEffect(currentDestination) {
        val index = pagerRoutes.indexOf(currentDestination?.route)
        if (index != -1 && index != pagerState.currentPage) {
            pagerState.scrollToPage(index)
        }
    }

    // Update nav when pager swipes
    LaunchedEffect(pagerState.currentPage) {
        val targetRoute = pagerRoutes[pagerState.currentPage]
        if (currentDestination?.route != targetRoute) {
            navController.navigate(targetRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Dark800.copy(alpha = 0.95f),
                    contentColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEachIndexed { index, item ->
                        val selected = pagerState.currentPage == index
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    item.icon, 
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp)
                                ) 
                            },
                            label = { 
                                Text(
                                    item.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.Black else FontWeight.Medium
                                ) 
                            },
                            selected = selected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryVibrant,
                                unselectedIconColor = Color.White.copy(alpha = 0.4f),
                                selectedTextColor = PrimaryVibrant,
                                unselectedTextColor = Color.White.copy(alpha = 0.4f),
                                indicatorColor = PrimaryVibrant.copy(alpha = 0.1f)
                            ),
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (isMainTab) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(innerPadding),
                beyondViewportPageCount = 1
            ) { page ->
                when (pagerRoutes[page]) {
                    Destinations.DASHBOARD -> DashboardScreen(onNavigateToSleep = {
                        navController.navigate(Destinations.SLEEP_LOG)
                    })
                    Destinations.FOOD_LOG -> FoodLogScreen()
                    Destinations.WORKOUT_LOG -> WorkoutLogScreen()
                    Destinations.WEIGHT_LOG -> WeightLogScreen()
                    Destinations.PERSONAL_STREAK -> PersonalStreakScreen()
                    Destinations.AI_COACH -> AICoachScreen()
                }
            }
        } else {
            IronLedgerNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
