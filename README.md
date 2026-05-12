# IronLedger Native — Android App

A **fully offline, privacy-first** native Android fitness tracker built with Kotlin + Jetpack Compose. Inspired by the [IronLedger web app](https://project-ironledger.netlify.app/) but reimplemented natively with premium UX.

## Key Principles

- **100% Local** — All data stored on-device via Room (SQLite). No cloud accounts required.
- **PIN + Biometric Lock** — App secured with 4-digit PIN or fingerprint. No Google Sign-In.
- **AI via Export** — Generate a data summary + prompt, copy it, paste into any LLM of your choice.
- **Premium UX** — Smooth animations, haptic feedback, shimmer loading, staggered transitions.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose (Material 3) |
| Navigation | Navigation Compose + HorizontalPager (swipe tabs) |
| Database | Room (SQLite) — fully offline |
| DI | Hilt (Dagger) |
| Architecture | MVVM (ViewModel → StateFlow → Repository → DAO) |
| Auth | Local PIN + AndroidX Biometric (fingerprint) |
| Min SDK | 34 (Android 14) |
| Target SDK | 35 |

## Project Structure

```
app/src/main/java/com/ironledger/app/
├── IronLedgerApp.kt              # @HiltAndroidApp
├── MainActivity.kt               # Single activity, app lock gate
├── di/DatabaseModule.kt          # Hilt: Room DB + DAO
├── data/
│   ├── local/
│   │   ├── IronLedgerDatabase.kt # Room DB (12 entities)
│   │   ├── Converters.kt        # JSON ↔ List type converters
│   │   ├── dao/IronLedgerDao.kt  # All queries (Flow + suspend)
│   │   └── entity/Entities.kt   # 12 entity data classes
│   └── repository/
│       └── IronLedgerRepository.kt
└── ui/
    ├── navigation/               # Destinations + NavHost
    ├── screens/                  # All Compose screens
    ├── viewmodel/                # All @HiltViewModel classes
    └── theme/                    # Color, Theme, Type
```

## For AI Models / Developers

**Read the docs in `native-conversion/` before touching code:**

| Doc | Purpose |
|---|---|
| `04_MODEL_HANDOFF.md` | **Start here.** User context, what's done, what to do. |
| `01_IMPLEMENTATION_PLAN.md` | Full phased plan with exact code samples. |
| `02_QUICK_REFERENCE.md` | Current state, build commands, architecture. |
| `03_CODE_PATTERNS.md` | Exact patterns to follow for new screens. |
| `05_DEPENDENCY_LIST.md` | All Gradle dependencies. |
| `06_UX_STANDARDS.md` | Animation specs — every screen MUST follow these. |

## How to Build

```bash
cd /Users/cicada3301/Desktop/IRONLEDGER_NATIVE
./gradlew assembleDebug
```

Or open in Android Studio and Run → `app`.

## What NOT to Use

- **Firebase** — Being removed. App is fully local.
- **Capacitor** — Old approach, ignore.
- **The stale copy** at `PROJECT_IRONLEDGER/IronLedgerNative/` — ignore, work here only.
