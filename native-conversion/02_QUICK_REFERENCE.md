# IronLedger Native — Quick Reference

> Companion to `01_IMPLEMENTATION_PLAN.md`.
> Designed so any AI model or developer has full context in one place.

## Current State (as of 2026-05-12)

| Item | Status | Details |
|---|---|---|
| Project path | ✅ | `/Users/cicada3301/Desktop/IRONLEDGER_NATIVE` |
| IDE | ✅ | Open in Android Studio |
| Build system | ✅ | Gradle 8.x + Kotlin DSL + Version Catalog |
| Min SDK | 34 | Android 14 |
| Target SDK | 35 | Android 15 |
| Kotlin | ✅ | 2.0.21 |
| Compose BOM | ✅ | 2026.02.01 |
| Room | ✅ | 2.6.1 |
| Hilt | ✅ | 2.59.2 |
| Firebase | ✅ | BOM 33.7.0 (configured, not wired) |
| google-services.json | ✅ | Present at `app/google-services.json` |
| Java | ✅ | JVM Target 11 |

## Firebase Project

- **Project ID:** `ironledger-31097`
- **Auth Domain:** `ironledger-31097.firebaseapp.com`
- **Package name:** `com.ironledger.app`
- Uses: Firebase Auth (Google Sign-In) + Firestore — **not yet wired**

## Key Architecture Decisions

### Why Native (Not Capacitor)

A previous attempt wrapped the React web app in a Capacitor WebView. That code is at `/Users/cicada3301/Desktop/PROJECT_IRONLEDGER/android-conversion/` — **IGNORE IT**. The native approach gives true Material You rendering, reliable SQLite storage, and direct SDK access.

### Architecture Pattern

```
UI (Compose) → ViewModel (StateFlow) → Repository → DAO (Room) → SQLite
                    ↑ injected by Hilt
```

Every screen follows the same pattern:
1. `@HiltViewModel` class with `@Inject constructor(repository)`
2. `StateFlow` using `stateIn(WhileSubscribed(5000))`
3. `@Composable` screen collecting state via `collectAsStateWithLifecycle()`

### Navigation

- **HorizontalPager** for 6 main tabs (swipe between them)
- **NavHost** for secondary screens (Sleep, Measurements, Photos, Settings, etc.)
- **BottomNavigationBar** synced with pager via `LaunchedEffect`

### Screens Currently Implemented vs Placeholder

| Screen | Status | File |
|---|---|---|
| Dashboard | ✅ Full UI | `DashboardScreen.kt` |
| Food Log | ✅ UI, ❌ add dialog | `FoodLogScreen.kt` |
| Workout Log | ✅ UI, ❌ add dialog | `WorkoutLogScreen.kt` |
| AI Coach | ✅ UI, ❌ real API | `AICoachScreen.kt` |
| Weight Log | ❌ Placeholder | `PlaceholderScreens.kt` |
| Sleep Log | ❌ Placeholder | `PlaceholderScreens.kt` |
| Measurements | ❌ Placeholder | `PlaceholderScreens.kt` |
| Photos | ❌ Placeholder | `PlaceholderScreens.kt` |
| Settings | ❌ Placeholder | `PlaceholderScreens.kt` |
| Progress | ❌ Placeholder | `PlaceholderScreens.kt` |
| Motivation | ❌ Placeholder | `PlaceholderScreens.kt` |
| Personal Streak | ❌ Placeholder | `PlaceholderScreens.kt` |
| Onboarding | ❌ Placeholder | `PlaceholderScreens.kt` |

### ViewModels Implemented

| ViewModel | Observes | Computes |
|---|---|---|
| `DashboardViewModel` | profile, today's meals, today's workouts, habit tracker | Combined UI state |
| `FoodLogViewModel` | meals by selected date | Macro totals (cal, protein, carbs, fat) |
| `WorkoutLogViewModel` | workouts by selected date | Workout list |
| `AICoachViewModel` | chat history | Message list + simulated responses |

## Quick Build Commands

```bash
# Build debug APK
cd /Users/cicada3301/Desktop/IRONLEDGER_NATIVE
./gradlew assembleDebug

# Build and install on connected device
./gradlew installDebug

# Clean build
./gradlew clean assembleDebug

# Run lint
./gradlew lint
```

## Color Reference (Theme)

| Name | Hex | Usage |
|---|---|---|
| `Dark900` | `#07070F` | Deepest background |
| `Dark800` | `#0F0F1E` | Cards, bottom nav |
| `Dark700` | `#16162C` | Elevated surfaces |
| `DarkBackground` | `#0A0A1A` | Main background |
| `PrimaryVibrant` | `#6366F1` | Indigo — primary accent, nav selected |
| `SecondaryVibrant` | `#EC4899` | Pink — secondary accent |
| `TertiaryVibrant` | `#8B5CF6` | Violet — tertiary accent |
| `TealNeon` | `#2DD4BF` | Teal — success, progress, buttons |
| `GreenNeon` | `#22C55E` | Green — positive indicators |
| `RedNeon` | `#EF4444` | Red — negative, delete, danger |
| `BlueNeon` | `#3B82F6` | Blue — info |
| `OrangeNeon` | `#F59E0B` | Orange/Amber — warnings |

## Web App Reference Files

Use these as feature specs when implementing each screen:

| Native Screen | Web File | Lines | Complexity |
|---|---|---|---|
| Weight Log | `src/pages/WeightLog.jsx` | 189 | Low |
| Sleep Log | `src/pages/SleepLog.jsx` | 181 | Low |
| Measurements | `src/pages/MeasurementsLog.jsx` | 199 | Medium |
| Settings | `src/pages/Settings.jsx` | 425 | High |
| Onboarding | `src/pages/Onboarding.jsx` | 139 | Low |
| Progress | `src/pages/Progress.jsx` | 310 | High |
| Photos | `src/pages/Photos.jsx` | 282 | Medium |
| Motivation | `src/pages/Motivation.jsx` | 180 | Low |
| Personal Streak | `src/pages/PersonalStreak.jsx` | 497 | Very High |
| Dashboard | `src/pages/Dashboard.jsx` | 342 | High (done) |
| Food Log | `src/pages/FoodLog.jsx` | ~400 | High (partial) |
| Workout Log | `src/pages/WorkoutLog.jsx` | ~500 | Very High (partial) |
| AI Coach | `src/pages/AICoach.jsx` | ~300 | Medium (partial) |

Web source path: `/Users/cicada3301/Desktop/PROJECT_IRONLEDGER/src/`
