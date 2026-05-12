# IronLedger Native — Development Progress

> **Last updated:** 2026-05-12
> **Project:** `/Users/cicada3301/Desktop/IRONLEDGER_NATIVE`

## Legend
- `[x]` Completed  — `[/]` In progress  — `[ ]` Not started

---

## Phase 0: Remove Firebase ✅
- [x] Remove `google-services` plugin from `app/build.gradle.kts`
- [x] Remove Firebase BOM + auth + firestore from dependencies
- [x] Remove from root `build.gradle.kts`
- [x] Delete `app/google-services.json`
- [x] Clean `libs.versions.toml` (remove firebase entries)
- [x] Verify build passes: `./gradlew assembleDebug`

## Phase 1: App Lock (PIN + Biometric) ✅
- [x] Add `androidx.biometric` dependency
- [x] Change `MainActivity` to extend `AppCompatActivity`
- [x] Create `AppLockViewModel.kt` (PIN hash, validation, lockout)
- [x] Create `AppLockScreen.kt` (setup mode + unlock mode)
- [x] Wire into `MainActivity.kt` (gate before `MainScreen`)
- [x] BiometricPrompt integration (fingerprint unlock)
- [x] Test: clean build + compile verified.

## Phase 2: Core Architecture ✅
- [x] Android project scaffolding (Kotlin DSL, version catalog)
- [x] Room database — 12 entities (`Entities.kt`)
- [x] DAO — Flow queries + suspend writes (`IronLedgerDao.kt`)
- [x] Repository layer (`IronLedgerRepository.kt`)
- [x] Hilt DI (`DatabaseModule.kt`, `IronLedgerApp.kt`)
- [x] Theme system (`Color.kt`, `Theme.kt`, `Type.kt`)
- [x] Navigation (NavHost + Destinations, 13 routes)
- [x] HorizontalPager + BottomNav (6 swipeable tabs)

## Phase 3: Screen Implementation

### ✅ Dashboard
- [x] Greeting, nutrition card, workout cards, streak card

### ✅ Food Log (partial)
- [x] Macro summary card, meal list, FAB
- [ ] "Add Food" bottom sheet
- [ ] Food search from embedded Kenyan foods DB
- [ ] Custom food entry form
- [ ] Swipe-to-delete meals
- [ ] Date picker

### ✅ Workout Log (partial)
- [x] Workout cards with exercise list, FAB
- [ ] "Start Workout" bottom sheet / flow
- [ ] Exercise picker from embedded exercise DB
- [ ] Live session (add sets/reps/weight per exercise)
- [ ] Rest day toggle
- [ ] Workout templates

### ✅ Weight Log
- [x] Input field, save/update, stats cards, history list
- [ ] Line chart (Canvas-based, last 30 days)
- [ ] Gain rate calculation + status indicator

### AI Coach (needs full rewrite)
- [x] Chat UI exists (but needs replacement)
- [ ] Rewrite as data export + prompt generator
- [ ] "Generate Prompt" button → builds context-rich prompt
- [ ] Prompt preview (scrollable)
- [ ] "Copy to Clipboard" button
- [ ] Data summary export (CSV/JSON file)

### Sleep Log
- [ ] Update `SleepLogEntity` (add bedtime, wakeTime, note fields)
- [ ] Add `fallbackToDestructiveMigration()` to DatabaseModule
- [ ] Create `SleepLogViewModel.kt`
- [ ] Create `SleepLogScreen.kt`
- [ ] Time pickers for bedtime/wake
- [ ] Duration calculation + quality indicator
- [ ] Stats (7-day avg, nights logged)
- [ ] Bar chart (Canvas, last 14 days)
- [ ] Recent entries list

### Measurements Log
- [ ] Create `MeasurementsLogViewModel.kt`
- [ ] Create `MeasurementsLogScreen.kt`
- [ ] Tab row (Chest/Waist/Arms/Thighs/Calves/Neck)
- [ ] Latest reading card
- [ ] "Log New" FAB → bottom sheet
- [ ] History list with delete
- [ ] Trend chart (Canvas)

### Settings
- [ ] Create `SettingsViewModel.kt`
- [ ] Create `SettingsScreen.kt`
- [ ] Port `calculateAllTargets()` to `utils/Calculations.kt`
- [ ] Profile editor form
- [ ] Calculated targets display
- [ ] Data export (all Room tables → JSON file)
- [ ] Data import (JSON file → Room)
- [ ] Clear all data (with confirmation)

### Onboarding
- [ ] Create `OnboardingScreen.kt`
- [ ] Multi-step wizard (name → stats → goal → done)
- [ ] Save profile to Room
- [ ] First-run detection

### Progress
- [ ] Create `ProgressViewModel.kt`
- [ ] Create `ProgressScreen.kt`
- [ ] Weekly summary card
- [ ] Weight tab (Canvas line chart)
- [ ] Strength tab (exercise picker + chart)
- [ ] Calories tab (Canvas bar chart)
- [ ] Gym attendance tab (heatmap grid)

### Photos
- [ ] Create `PhotosScreen.kt`
- [ ] Camera via `ActivityResultContracts.TakePicture`
- [ ] Gallery grid (grouped by date)
- [ ] Photo detail modal
- [ ] Delete entries

### Motivation
- [ ] Create `MotivationScreen.kt`
- [ ] Quote of the day (48 quotes, rotated by day-of-year)
- [ ] Video categories (Mindset/Training/Nutrition)
- [ ] YouTube links via Intent

### Personal Streak
- [ ] Create `PersonalStreakViewModel.kt`
- [ ] Create `PersonalStreakScreen.kt`
- [ ] Separate PIN protection (different from app lock)
- [ ] BiometricPrompt for this screen
- [ ] Current streak counter (Day N)
- [ ] Progress ring to next milestone
- [ ] Monthly heatmap calendar
- [ ] Milestones (7/14/21/30/60/90 days)
- [ ] Streak history (expandable)
- [ ] Reset button with confirmation
- [ ] Exclude from data exports

## Phase 4: Data & Utilities ✅
- [x] Port Kenyan foods DB (38 foods → `KenyanFoodsDB.kt`)
- [x] Port exercise DB (11 exercises → `ExerciseDB.kt`)
- [x] Port quotes (48 → `Quotes.kt` in `MotivationDB.kt`)
- [x] Port videos (9 → `Videos.kt` in `MotivationDB.kt`)
- [x] Create `utils/Calculations.kt`

## Phase 5: Polish
- [ ] UX pass (apply all patterns from `06_UX_STANDARDS.md`)
- [ ] App icon (adaptive icon)
- [ ] Splash screen (SplashScreen API)
- [ ] Haptic feedback on saves
- [ ] Snackbar confirmations
- [ ] Shimmer loading states
- [ ] Entry animations on all screens
