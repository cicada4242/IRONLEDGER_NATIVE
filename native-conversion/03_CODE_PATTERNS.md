# IronLedger Native — Existing Code Structure & Patterns

This document maps the existing codebase for any model/developer implementing new screens.
Follow these patterns exactly for consistency.

---

## Pattern: Creating a New Screen

Every screen requires **3 steps**. Here is the exact pattern used by the existing screens.

### Step 1: ViewModel

Create in `app/src/main/java/com/ironledger/app/ui/viewmodel/`

```kotlin
package com.ironledger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ironledger.app.data.repository.IronLedgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Define the UI state data class
data class ExampleUiState(
    val items: List<SomeEntity> = emptyList(),
    val isLoading: Boolean = true
)

// 2. Annotate with @HiltViewModel, inject the repository
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val repository: IronLedgerRepository
) : ViewModel() {

    // 3. Expose a StateFlow using stateIn() with WhileSubscribed(5000)
    val uiState: StateFlow<ExampleUiState> = repository.someFlow
        .map { items -> ExampleUiState(items = items, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExampleUiState()
        )

    // 4. Suspend functions for writes — use viewModelScope.launch
    fun saveItem(item: SomeEntity) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }
}
```

### Step 2: Composable Screen

Create in `app/src/main/java/com/ironledger/app/ui/screens/`

```kotlin
package com.ironledger.app.ui.screens

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ironledger.app.ui.viewmodel.ExampleViewModel

@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel = hiltViewModel()  // Hilt provides it
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Build your Compose UI using uiState
    // Use the theme colors: Dark800, Dark900, TealNeon, PrimaryVibrant, etc.
    // Use Surface with RoundedCornerShape(20.dp) for cards
}
```

### Step 3: Wire Into Navigation

1. Remove the placeholder from `PlaceholderScreens.kt`
2. If it's a **main tab**: update `MainScreen.kt` → `HorizontalPager` `when` block
3. If it's a **secondary screen**: update `IronLedgerNavHost.kt` → add a `composable(route)` block

---

## Existing File Map

### Data Layer

| File | Purpose | Key Elements |
|---|---|---|
| `Entities.kt` | 12 Room entity classes | All `@Entity` data classes + helper data classes (`ExerciseLog`, `WorkoutSet`, `TemplateExercise`) |
| `IronLedgerDao.kt` | Room DAO (182 lines) | Flow-based queries for reads, suspend for writes. Every entity has CRUD. |
| `IronLedgerRepository.kt` | Repository (175 lines) | Wraps DAO. All writes use `withContext(Dispatchers.IO)`. Flows exposed directly. |
| `IronLedgerDatabase.kt` | Room database | 12 entities, version 1, `exportSchema = false` |
| `Converters.kt` | Type converters | `List<ExerciseLog>` ↔ JSON, `List<TemplateExercise>` ↔ JSON (uses Gson) |
| `DatabaseModule.kt` | Hilt DI module | Provides `IronLedgerDatabase` and `IronLedgerDao` as singletons |

### UI Layer — Screens

| File | Lines | Status | Key Composables |
|---|---|---|---|
| `MainScreen.kt` | 144 | ✅ | `MainScreen()` — Scaffold + HorizontalPager + BottomNav |
| `DashboardScreen.kt` | 237 | ✅ | `DashboardScreen()` — greeting, nutrition card, workout cards, streak card |
| `FoodLogScreen.kt` | 248 | ✅ (partial) | `FoodLogScreen()` — macro summary, meal list, FAB (TODO: add dialog) |
| `WorkoutLogScreen.kt` | 184 | ✅ (partial) | `WorkoutLogScreen()` — workout cards, FAB (TODO: add dialog) |
| `AICoachScreen.kt` | ~180 | ✅ (partial) | `AICoachScreen()` — chat bubbles, input (TODO: real API) |
| `WeightLogScreen.kt` | 183 | ✅ | `WeightLogScreen()` — input, stats, history list |
| `PlaceholderScreens.kt` | 44 | Placeholder | Contains: Onboarding, Photos, Settings, Sleep, Measurements, Motivation, PersonalStreak, Progress |

### UI Layer — ViewModels

| File | Lines | Observes | Computes |
|---|---|---|---|
| `DashboardViewModel.kt` | 54 | profile + today's meals + workouts + habit | Combined `DashboardUiState` |
| `FoodLogViewModel.kt` | 53 | meals by date | Macro totals |
| `WorkoutLogViewModel.kt` | 45 | workouts by date | Workout list |
| `AICoachViewModel.kt` | 50 | chat history | Messages + simulated response |
| `WeightLogViewModel.kt` | 40 | all weight entries | Entry list + save/update logic |

### UI Layer — Theme

| File | Defines |
|---|---|
| `Color.kt` | `Dark900`, `Dark800`, `Dark700`, `DarkBackground`, `PrimaryVibrant`, `SecondaryVibrant`, `TertiaryVibrant`, `TealNeon`, `GreenNeon`, `RedNeon`, `BlueNeon`, `OrangeNeon`, `CardGradientStart`, `CardGradientEnd` |
| `Theme.kt` | Material3 dark color scheme, `IronLedgerNativeTheme()`, system bar colors |
| `Type.kt` | Typography scale — `Black`/`ExtraBold` headings, `letterSpacing` |

### Navigation

| File | Purpose |
|---|---|
| `IronLedgerNavigation.kt` | `Destinations` object with route string constants, `NavigationActions` helper |
| `IronLedgerNavHost.kt` | NavHost composable with routes for all 13 screens |

---

## UI Design Rules (Match Existing Screens)

### Card Style
```kotlin
Surface(
    shape = RoundedCornerShape(20.dp),
    color = Dark800,
    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
) { /* content */ }
```

### Gradient Header Cards (Premium)
```kotlin
Box(
    modifier = Modifier
        .fillMaxWidth()
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(CardGradientStart, CardGradientEnd)
            ),
            shape = RoundedCornerShape(20.dp)
        )
) { /* content */ }
```

### Section Title
```kotlin
Text(
    "SECTION TITLE",
    style = MaterialTheme.typography.labelSmall,
    color = PrimaryVibrant,
    letterSpacing = 3.sp,
    fontWeight = FontWeight.Black
)
```

### FAB Style
```kotlin
FloatingActionButton(
    onClick = { /* TODO */ },
    containerColor = PrimaryVibrant,
    contentColor = Color.White,
    shape = RoundedCornerShape(16.dp)
) { Icon(Icons.Default.Add, contentDescription = "Add") }
```

### Status Colors
```kotlin
val statusColor = when {
    value >= goodThreshold -> GreenNeon
    value >= okThreshold -> OrangeNeon
    else -> RedNeon
}
```

---

## Entity Quick Reference

When implementing screens, reference these fields:

### WeightLogEntity
```kotlin
val id: Int, val date: String, val weight: Float
```

### SleepLogEntity (NEEDS MIGRATION — see note in 01_IMPLEMENTATION_PLAN.md)
```kotlin
// Current:
val id: Int, val date: String, val durationHours: Float, val quality: String
// Should be expanded to:
val id: Int, val date: String, val bedtime: String, val wakeTime: String,
val durationHours: Float, val quality: String, val note: String
```

### MeasurementLogEntity
```kotlin
val id: Int, val date: String, val type: String, val value: Float
```

### HabitTrackerEntity
```kotlin
val id: String = "private_tracker", val startDate: String,
val lastResetAt: String, val resetHistory: String // JSON
```

### ProfileEntity
```kotlin
val id: String = "user", val name: String, val email: String,
val photoURL: String, val sex: String, val workoutsPerWeek: Int,
val goal: String, val createdAt: String, val weight: Float,
val height: Float, val age: Int
```

---

## Repository Method Reference

All available methods on `IronLedgerRepository`:

### Profile
- `profileFlow: Flow<ProfileEntity?>` / `getProfile()` / `saveProfile()`

### Foods
- `allFoodsFlow` / `insertFood()` / `updateFood()` / `deleteFood()`

### Meal Logs
- `getMealsByDateFlow(date)` / `getMealsByDate(date)` / `insertMealEntry()` / `updateMealEntry()` / `deleteMealEntry()`

### Workouts
- `getWorkoutsByDateFlow(date)` / `allWorkoutsFlow` / `insertWorkout()` / `updateWorkout()` / `deleteWorkout()`

### Workout Templates
- `allWorkoutTemplatesFlow` / `saveWorkoutTemplate()` / `deleteWorkoutTemplate()`

### Weight Logs
- `allWeightEntriesFlow` / `getWeightByDate(date)` / `insertWeightEntry()` / `updateWeightEntry()`

### Sleep Logs
- `allSleepEntriesFlow` / `getSleepByDate(date)` / `insertSleepEntry()` / `updateSleepEntry()`

### Measurements
- `allMeasurementEntriesFlow` / `insertMeasurementEntry()` / `updateMeasurementEntry()` / `deleteMeasurementEntry()`

### Photos
- `allPhotosFlow` / `insertPhoto()` / `deletePhoto()`

### Chat History
- `chatHistoryFlow` / `insertChatMessage()` / `clearChatHistory()`

### Habit Tracker
- `habitTrackerFlow` / `saveHabitTracker()` / `clearHabitTracker()`

### Settings
- `getSetting(key)` / `saveSetting(SettingEntity)`
