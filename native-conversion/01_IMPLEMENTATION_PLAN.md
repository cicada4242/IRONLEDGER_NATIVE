# IronLedger Native — Implementation Plan

## Goal

Build a **fully offline, privacy-first** native Android fitness tracker using Kotlin + Jetpack Compose. The web app at https://project-ironledger.netlify.app/ is **inspiration only** — the native app should feel premium and can improve on the web version's UX.

## Core Architecture Decisions

| Decision | Choice | Rationale |
|---|---|---|
| Authentication | **PIN + Biometric (fingerprint)** | No Google Sign-In. Fully local. User unlocks app with PIN or fingerprint. |
| Data storage | **Room (SQLite)** | All data stays on-device. No cloud sync required. |
| AI Coach | **Export prompt to clipboard** | User copies a context-rich prompt and pastes it into any LLM (ChatGPT, Gemini, etc.). No API keys needed. |
| Firebase | **REMOVE entirely** | Not needed. Reduces APK size, eliminates initialization overhead, removes the Google Services plugin that may cause sign-in prompts. |
| Cloud backup | **JSON export/import** | Manual backup via file export. No Firestore. |

> [!CAUTION]
> **FIRST PRIORITY: Remove Firebase.** The Google Services plugin + Firebase SDK may be causing unwanted sign-in behavior. Remove all Firebase dependencies before any other work. See Phase 0 below.

---

## Phase 0: Remove Firebase & Fix Launch ✅→ DO THIS FIRST

The app currently includes Firebase Auth + Firestore + Google Services plugin. These must be completely removed.

### Step 0.1: Remove from `app/build.gradle.kts`

Delete these 3 lines from the `plugins` block:
```kotlin
// DELETE this line:
alias(libs.plugins.google.services)
```

Delete these 4 lines from the `dependencies` block:
```kotlin
// DELETE these lines:
implementation(platform(libs.firebase.bom))
implementation(libs.firebase.auth)
implementation(libs.firebase.firestore)
implementation(libs.kotlinx.coroutines.play.services)
```

### Step 0.2: Remove from root `build.gradle.kts`

Delete this line from the `plugins` block:
```kotlin
// DELETE this line:
alias(libs.plugins.google.services) apply false
```

### Step 0.3: Delete `google-services.json`

```bash
rm /Users/cicada3301/Desktop/IRONLEDGER_NATIVE/app/google-services.json
```

### Step 0.4: Clean entries from `libs.versions.toml`

Remove `firebaseBom` version, `firebase-bom`, `firebase-auth`, `firebase-firestore` libraries, `google-services` plugin, and `coroutines-play-services` library.

### Step 0.5: Verify build

```bash
cd /Users/cicada3301/Desktop/IRONLEDGER_NATIVE && ./gradlew assembleDebug
```

If it builds successfully, Firebase is fully gone.

---

## Phase 1: App Lock (PIN + Biometric) — NEW FEATURE

This replaces Google Sign-In. The user sets a 4-digit PIN on first launch. On subsequent launches, the app shows a lock screen that accepts PIN or fingerprint.

### Step 1.1: Add biometric dependency

In `libs.versions.toml`, add:
```toml
[versions]
biometric = "1.1.0"

[libraries]
androidx-biometric = { group = "androidx.biometric", name = "biometric", version.ref = "biometric" }
```

In `app/build.gradle.kts`, add:
```kotlin
implementation(libs.androidx.biometric)
```

### Step 1.2: Create `AppLockViewModel.kt`

**File:** `app/src/main/java/com/ironledger/app/ui/viewmodel/AppLockViewModel.kt`

This ViewModel manages:
- **First launch detection:** Check if setting `"pin_hash"` exists in Room settings table
- **PIN setup:** Hash the 4-digit PIN with `MessageDigest.getInstance("SHA-256")`, store hash in settings table as `SettingEntity(key = "pin_hash", value = hashString)`
- **PIN validation:** Hash input, compare to stored hash
- **Lockout:** After 5 failed attempts, lock for 30 seconds. Store `"failed_attempts"` and `"lockout_until"` in settings table
- **Biometric support:** Check `BiometricManager.canAuthenticate(BIOMETRIC_STRONG)` to determine if fingerprint is available

Key functions:
```kotlin
fun isPinSetup(): Boolean  // checks if "pin_hash" setting exists
fun setupPin(pin: String)  // hashes and stores PIN
fun validatePin(pin: String): Boolean  // compares hash
fun isLockedOut(): Boolean  // checks lockout timer
fun incrementFailedAttempts()  // tracks failures
fun resetFailedAttempts()  // on successful unlock
```

### Step 1.3: Create `AppLockScreen.kt`

**File:** `app/src/main/java/com/ironledger/app/ui/screens/AppLockScreen.kt`

Two modes:

**Setup mode** (first launch, no PIN exists):
- Welcome message: "Set up your PIN"
- 4-digit PIN input field (`keyboardType = KeyboardType.NumberPassword`)
- Confirm PIN field
- "Create PIN" button
- After setup, auto-unlock and show Dashboard

**Unlock mode** (PIN exists):
- PIN input field (auto-submits when 4 digits entered)
- Fingerprint button (if biometric available) — uses `BiometricPrompt` from `androidx.biometric`
- Failed attempt counter + lockout message
- The PIN input should use large, centered digits with `letterSpacing = 12.sp`

### Step 1.4: Wire into `MainActivity.kt`

Wrap `MainScreen()` in a state check:
```kotlin
setContent {
    IronLedgerNativeTheme {
        val isUnlocked = remember { mutableStateOf(false) }
        if (isUnlocked.value) {
            MainScreen()
        } else {
            AppLockScreen(onUnlocked = { isUnlocked.value = true })
        }
    }
}
```

### Step 1.5: BiometricPrompt integration

In `AppLockScreen.kt`, when the fingerprint button is tapped:
```kotlin
val promptInfo = BiometricPrompt.PromptInfo.Builder()
    .setTitle("Unlock IronLedger")
    .setSubtitle("Use your fingerprint to unlock")
    .setNegativeButtonText("Use PIN")
    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
    .build()

// BiometricPrompt requires a FragmentActivity, get it from LocalContext
val activity = LocalContext.current as FragmentActivity
val biometricPrompt = BiometricPrompt(activity, executor, callback)
biometricPrompt.authenticate(promptInfo)
```

> [!IMPORTANT]
> `BiometricPrompt` needs a `FragmentActivity`. The existing `MainActivity` extends `ComponentActivity`. **Change it to extend `FragmentActivity`** (or `AppCompatActivity` which extends it). Update: `class MainActivity : AppCompatActivity()` and add `implementation("androidx.appcompat:appcompat:1.6.1")` if not present.

---

## Phase 2: Implement Remaining Screens

Each screen follows the pattern in `03_CODE_PATTERNS.md`. Below are the specific details.

### 2.1: Weight Log ✅ DONE

Already implemented with `WeightLogScreen.kt` + `WeightLogViewModel.kt`.

### 2.2: Sleep Log

**New files:**
- `ui/viewmodel/SleepLogViewModel.kt`
- `ui/screens/SleepLogScreen.kt`

**Entity migration required.** Current `SleepLogEntity` has only `durationHours` and `quality`. Add fields: `bedtime: String = ""`, `wakeTime: String = ""`, `note: String = ""`. Since there's no user data yet, use `fallbackToDestructiveMigration()` in `DatabaseModule.kt`:

```kotlin
return Room.databaseBuilder(app, IronLedgerDatabase::class.java, IronLedgerDatabase.DATABASE_NAME)
    .fallbackToDestructiveMigration()
    .build()
```

**Sleep duration calculation:** Given bedtime "23:00" and wakeTime "07:00", compute hours:
```kotlin
fun calculateSleepDuration(bedtime: String, wakeTime: String): Float {
    val bedParts = bedtime.split(":").map { it.toInt() }
    val wakeParts = wakeTime.split(":").map { it.toInt() }
    val bedMinutes = bedParts[0] * 60 + bedParts[1]
    val wakeMinutes = wakeParts[0] * 60 + wakeParts[1]
    val diff = if (wakeMinutes > bedMinutes) wakeMinutes - bedMinutes
               else (24 * 60 - bedMinutes) + wakeMinutes
    return diff / 60f
}
```

**Quality logic:** 7-9h = green ("Great"), 5-7h = yellow ("Fair"), <5h = red ("Poor").

**Screen layout:** Time pickers for bedtime/wake → duration preview with color → optional note → save button → stats row (7-day avg, nights logged) → recent entries list.

### 2.3: Measurements Log

**New files:**
- `ui/viewmodel/MeasurementsLogViewModel.kt`
- `ui/screens/MeasurementsLogScreen.kt`

**Measurement types** (hardcoded list):
```kotlin
enum class MeasurementType(val label: String, val unit: String) {
    CHEST("Chest", "cm"), WAIST("Waist", "cm"), ARMS("Arms (Flexed)", "cm"),
    THIGHS("Thighs", "cm"), CALVES("Calves", "cm"), NECK("Neck", "cm")
}
```

**Screen layout:** Horizontal tab row of measurement types → latest reading card → "Log New" FAB → opens bottom sheet (type dropdown, date picker, value input in cm) → history list filtered by selected type with swipe-to-delete.

### 2.4: Settings

**New files:**
- `ui/viewmodel/SettingsViewModel.kt`
- `ui/screens/SettingsScreen.kt`
- `utils/Calculations.kt`

**Must port `calculateAllTargets()` from web.** The formula:
```kotlin
fun calculateAllTargets(weight: Float, height: Float, age: Int, sex: String, workoutsPerWeek: Int): Targets {
    // Mifflin-St Jeor BMR
    val bmr = if (sex == "male") {
        10 * weight + 6.25 * height - 5 * age + 5
    } else {
        10 * weight + 6.25 * height - 5 * age - 161
    }
    val activityMultiplier = when (workoutsPerWeek) {
        in 0..2 -> 1.375f; in 3..4 -> 1.55f; in 5..6 -> 1.725f; else -> 1.9f
    }
    val tdee = (bmr * activityMultiplier).toInt()
    val surplus = 300  // lean bulk surplus
    return Targets(
        tdee = tdee,
        workoutDayCalories = tdee + surplus,
        restDayCalories = tdee,
        proteinTarget = (weight * 2.2).toInt(),  // 1g per lb
        carbsTarget = ((tdee * 0.45) / 4).toInt(),
        fatTarget = ((tdee * 0.25) / 9).toInt()
    )
}
```

**Screen sections:** Profile form → calculated targets display → data management (Export JSON / Import JSON / Clear All) → app info.

**No API keys section** — AI Coach uses export-to-clipboard, not API keys.

### 2.5: Onboarding

**New file:** `ui/screens/OnboardingScreen.kt` (replace placeholder)

Multi-step wizard: Welcome → Name → Physical stats (weight/height/age/sex) → Workouts per week → Done. Saves profile to Room. Navigates to Dashboard.

**First-run detection:** In `MainActivity`, check if profile exists. If not, show Onboarding instead of Dashboard. After onboarding completes, the PIN setup screen should also appear (if PIN not yet set).

### 2.6: AI Coach — Export Prompt Approach

**Rewrite `AICoachViewModel.kt`** to remove simulated responses.

New approach: The AI Coach screen generates a **context-rich prompt** that includes the user's recent data (last 7 days of meals, workouts, weight, sleep). The user taps "Copy Prompt" and pastes it into any LLM.

**Screen layout:**
- Card explaining: "IronLedger prepares a prompt with your fitness data. Copy it and paste into your favorite AI (ChatGPT, Gemini, Claude, etc.)"
- "Generate Prompt" button → builds a detailed prompt string
- Prompt preview (scrollable text)
- "Copy to Clipboard" button → uses `ClipboardManager`
- Optional: text field where user can paste the AI's response back for storage

**Prompt template:**
```
You are a fitness coach. Here is my data for the last 7 days:

PROFILE: [name], [age]yo [sex], [weight]kg, [height]cm, [workoutsPerWeek]x/week

MEALS (last 7 days):
[date]: [totalCal] kcal, [protein]g protein, [carbs]g carbs, [fat]g fat

WORKOUTS (last 7 days):
[date]: [type] — [exercises with sets/reps/weight]

WEIGHT LOG (last 7 days):
[date]: [weight]kg

SLEEP (last 7 days):
[date]: [duration]h ([quality])

Based on this data, please:
1. Assess my nutrition adherence
2. Evaluate my training consistency
3. Check my weight trend
4. Give specific recommendations for next week
```

### 2.7: Progress Screen

**New files:**
- `ui/viewmodel/ProgressViewModel.kt`
- `ui/screens/ProgressScreen.kt`

**Charting:** Use `Canvas` composable for custom drawing. No external library needed for simple line/bar charts. Key drawing functions:

```kotlin
// Simple line chart in Canvas
Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
    val points = data.mapIndexed { i, value ->
        Offset(
            x = i * (size.width / (data.size - 1)),
            y = size.height - (value - minVal) / (maxVal - minVal) * size.height
        )
    }
    // Draw line
    for (i in 0 until points.size - 1) {
        drawLine(color = TealNeon, start = points[i], end = points[i+1], strokeWidth = 3f)
    }
    // Draw dots
    points.forEach { drawCircle(color = TealNeon, radius = 6f, center = it) }
}
```

**4 tabs:** Weight (line chart) / Strength (line chart per exercise) / Calories (bar chart actual vs target) / Gym Attendance (heatmap grid).

### 2.8: Photos, Motivation, Personal Streak

**Photos:** Camera via `ActivityResultContracts.TakePicture()`. Store compressed JPEG as Base64 in Room. Gallery grid grouped by date.

**Motivation:** Static quote list (48 quotes, rotated by day-of-year). YouTube video list opens via `Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))`.

**Personal Streak:** Most complex screen. PIN-protected (separate from app lock — this has its own PIN for extra privacy). Heatmap calendar, milestones, streak history. See `PersonalStreak.jsx` (497 lines) for full feature spec. Uses its own BiometricPrompt instance.

---

## Phase 3: Data & Utilities

### Embedded Data Files

Create in `app/src/main/java/com/ironledger/app/data/`:

**`KenyanFoodsDB.kt`** — Port 38 foods from `src/data/kenyanFoods.js`:
```kotlin
data class KenyanFood(val name: String, val calories: Int, val protein: Float,
    val carbs: Float, val fat: Float, val servingSize: Float, val servingUnit: String)

val kenyanFoodsDB = listOf(
    KenyanFood("Ugali", 150, 3.5f, 33f, 0.5f, 100f, "g"),
    // ... 37 more
)
```

**`ExerciseDB.kt`** — Port 11 exercises from `src/data/exercises.js`
**`Quotes.kt`** — 48 motivational quotes
**`Videos.kt`** — 9 curated YouTube videos (title + URL)

### Calculation Utilities

Create `app/src/main/java/com/ironledger/app/utils/Calculations.kt` with:
- `calculateAllTargets()` — BMR → TDEE → macros (see Settings section above)
- `calculateWeightGainRate()` — weekly kg change from last 2 weigh-ins
- `getGainRateStatus()` — returns green/yellow/red + message
- `calculateStreak()` — consecutive workout days
- `getWeeklyConsistency()` — workout days in last 7
- `calculateSleepDuration()` — hours between bedtime/wake
- `getSleepQuality()` — green/yellow/red based on hours
- `calculateRollingAverage()` — 7-day rolling average

---

## Phase 4: Polish

- **App icon:** Create adaptive icon (foreground: IronLedger logo on transparent, background: solid `#0A0A1A`)
- **Splash screen:** Use `SplashScreen` API (Android 12+). Show logo on dark background for 300ms.
- **Data export:** Serialize all Room tables to JSON. Use `Intent.ACTION_CREATE_DOCUMENT` to let user pick save location.
- **Data import:** Use `Intent.ACTION_OPEN_DOCUMENT` to pick a JSON file. Deserialize and replace all Room data.

---

## Verification Plan

### Build
```bash
cd /Users/cicada3301/Desktop/IRONLEDGER_NATIVE && ./gradlew assembleDebug
```

### On-Device Testing
1. Fresh install → shows PIN setup screen (no sign-in prompt)
2. Set PIN → unlocks to Dashboard
3. Close app → reopen → shows PIN/fingerprint unlock
4. All 6 bottom tabs navigate correctly via swipe and tap
5. Weight Log: enter weight, see it in list, persists after restart
6. Settings: edit profile, see calculated targets update
7. AI Coach: tap Generate Prompt → copy → verify prompt text is correct
8. Export JSON → reimport → data intact
