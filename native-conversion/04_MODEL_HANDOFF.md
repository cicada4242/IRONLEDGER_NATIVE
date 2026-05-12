# Model Handoff Brief — IronLedger Native Android

> **For the AI model implementing this plan.**
> Read this FIRST before touching any code.

## Who Is the User?

- **Name:** Brian (goes by cicada4242/cicada3301 on GitHub)
- **Programming experience:** Amateur — he is not a professional developer. He built IronLedger as a personal fitness tracker with AI assistance. Explain technical decisions clearly but don't over-explain. He can follow instructions.
- **Context:** He has a working React web app at https://project-ironledger.netlify.app/. He wants a native Android version that looks and feels **premium** — not a WebView wrapper, not a basic MVP. The UX must feel polished, animated, and modern. Think Samsung Health / Nike Run Club tier.
- **Sensitivity:** The "Personal Streak" page tracks a private vice. Never reference it explicitly in commit messages, README, or comments. Use neutral terms like "habit tracker" or "private streak dashboard."

## Critical Design Philosophy

> [!IMPORTANT]
> **The web app is INSPIRATION ONLY.** It defines the features and data model, but the native app should be *better* — better animations, better transitions, better micro-interactions. Do NOT produce a basic text-and-list app. Every screen must feel alive.
>
> Read `06_UX_STANDARDS.md` for exact animation specs, transition patterns, and visual effects that MUST be applied to every screen.

## Key Architectural Decisions (USER-CONFIRMED)

| Decision | What | Why |
|---|---|---|
| **No Firebase** | Remove all Firebase deps | App is fully local. No Google Sign-In. No cloud sync. |
| **PIN + Biometric app lock** | User sets a 4-digit PIN on first launch. Unlocks with PIN or fingerprint. | Privacy-first. No account needed. |
| **Local food database** | Embed Kenyan foods DB + allow user to add custom foods | No API calls for food lookup. Everything works offline. |
| **AI Coach = Export** | App generates a data summary file + pre-built prompt. User copies/exports and pastes into their LLM of choice. | No API keys. Works with any LLM. User controls their data. |
| **JSON backup** | Export all data as JSON file. Import from JSON. | Manual backup instead of cloud sync. |

## What Exists

- **Active project (open in Android Studio):** `/Users/cicada3301/Desktop/IRONLEDGER_NATIVE`
- **Web app source (INSPIRATION for features):** `/Users/cicada3301/Desktop/PROJECT_IRONLEDGER/src/`
- **Web app deployed:** https://project-ironledger.netlify.app/
- **Web app repo:** `git@github.com:cicada4242/ironledger.git`
- **Old Capacitor attempt (IGNORE):** `/Users/cicada3301/Desktop/PROJECT_IRONLEDGER/android-conversion/`
- **Stale copy (IGNORE):** `/Users/cicada3301/Desktop/PROJECT_IRONLEDGER/IronLedgerNative/`

> [!CAUTION]
> There are TWO copies of the native project:
> - `/Users/cicada3301/Desktop/IRONLEDGER_NATIVE` ← **this is the source of truth**
> - `/Users/cicada3301/Desktop/PROJECT_IRONLEDGER/IronLedgerNative/` ← stale copy, IGNORE
>
> The WeightLog files were synced from the stale copy on 2026-05-12 since it was ahead. Going forward, ALL work must happen in IRONLEDGER_NATIVE only.

## All Docs Are In

```
/Users/cicada3301/Desktop/IRONLEDGER_NATIVE/native-conversion/
├── 01_IMPLEMENTATION_PLAN.md    ← Full phased plan with all screens
├── 02_QUICK_REFERENCE.md        ← Current state, commands, architecture
├── 03_CODE_PATTERNS.md          ← Exact code patterns + entity/repo reference
├── 04_MODEL_HANDOFF.md          ← This file (read first)
├── 05_DEPENDENCY_LIST.md        ← All Gradle dependencies
├── 06_UX_STANDARDS.md           ← Animation specs, transitions, visual effects
```

Also at project root:
```
/Users/cicada3301/Desktop/IRONLEDGER_NATIVE/
├── README.md                     ← Project overview + structure
├── TODO.md                       ← Detailed progress checklist
```

## What Has Already Been Done

> [!CAUTION]
> Do NOT recreate these. They are verified and working.

- [x] Android project with Kotlin DSL build system
- [x] Version catalog (`gradle/libs.versions.toml`)
- [x] Room database with 12 entities (Entities.kt)
- [x] DAO with Flow-based queries (IronLedgerDao.kt — 182 lines)
- [x] Repository layer (IronLedgerRepository.kt — 175 lines)
- [x] Hilt DI setup (DatabaseModule.kt, IronLedgerApp.kt)
- [x] Theme system (Color.kt, Theme.kt, Type.kt)
- [x] Navigation (NavHost with 13 routes + Destinations)
- [x] HorizontalPager + BottomNav (6 tabs, swipe navigation)
- [x] DashboardScreen — greeting, nutrition, workout, streak cards
- [x] FoodLogScreen — macro summary, meal list, FAB (add dialog missing)
- [x] WorkoutLogScreen — workout cards, FAB (add dialog missing)
- [x] AICoachScreen — chat UI, simulated responses (**needs full rewrite** — see AI Coach section)
- [x] WeightLogScreen — input, stats, history list (functional)
- [x] WeightLogViewModel — save/update weight entries (functional)
- [x] AndroidManifest with INTERNET + CAMERA permissions

**NOT done yet (remove Firebase was NOT done yet):**
- [ ] Firebase dependencies still in build files — **MUST be removed first** (see Phase 0 in `01_IMPLEMENTATION_PLAN.md`)

## What You Need To Do

**Start with Phase 0** (remove Firebase), then Phase 1 (app lock), then Phase 2 (screens).

### Recommended Order
1. **Phase 0: Remove Firebase** — eliminate all Firebase deps + google-services.json
2. **Phase 1: App Lock** — PIN setup + biometric unlock
3. **Sleep Log** — similar to Weight Log, needs entity migration
4. **Measurements Log** — adds tab switching pattern
5. **Settings** — profile editor + calculateAllTargets() + data export/import
6. **Onboarding** — first-run wizard
7. **Food Log dialogs** — "Add Food" bottom sheet with embedded food database search
8. **Workout Log dialogs** — workout session flow with exercise DB
9. **AI Coach** — rewrite as data export + prompt generator (NOT API calls)
10. **Progress** — charts with Canvas composable
1. **Sleep Log** — similar to Weight Log, needs entity migration
2. **Measurements Log** — adds tab switching pattern
3. **Settings** — profile editor + calculateAllTargets() + data export/import
4. **Onboarding** — first-run wizard
5. **Food Log dialogs** — "Add Food" bottom sheet with embedded food database search
6. **Workout Log dialogs** — workout session flow with exercise DB
7. **AI Coach** — rewrite as data export + prompt generator (NOT API calls)
8. **Progress** — charts with Canvas composable
9. **Photos** — camera integration
10. **Motivation** — static content (quotes + YouTube links)
11. **Personal Streak** — most complex (its own PIN, biometrics, heatmap, milestones)

## Critical Warnings & Tips

- **Android Studio Agent:** The user has an AI agent active in Android Studio that can help fix compilation or build errors. If you get stuck on a complex build issue, ask the user to use their Studio agent to help.
- **Package Name Conflict:** The device might have an old Capacitor build with `com.ironledger.app`. Always `adb uninstall com.ironledger.app` if the old sign-in screen appears.
- **Room Migrations:** The `SleepLogEntity` needs a schema update soon. Use `fallbackToDestructiveMigration()` in `DatabaseModule.kt` for now to avoid migration boilerplate during development.
- **UI Consistency:** Always use the components in `ui/components/IronLedgerComponents.kt`. Exact code patterns are in `01_IMPLEMENTATION_PLAN.md` Phase 2.7.

4. **Kenyan foods DB not embedded yet.** 38 foods from `src/data/kenyanFoods.js` must be ported to a Kotlin `listOf()` data class.

5. **AI Coach is NOT API-based.** It generates a data summary + prompt that the user exports/copies and pastes into any external LLM. No Retrofit needed for this. No API keys.

6. **Personal Streak is PRIVATE.** Has its own separate PIN (different from app lock PIN). Must be excluded from data exports. Never mention the specific vice.

7. **TWO project copies exist.** Work ONLY in `/Users/cicada3301/Desktop/IRONLEDGER_NATIVE`.

8. **UX MUST BE PREMIUM.** Read `06_UX_STANDARDS.md`. Every screen needs entry animations, every card needs press feedback, every transition must be smooth. This is not optional.

## Tools on the Mac

| Tool | Status | Details |
|---|---|---|
| Android Studio | ✅ | Installed, project is open |
| Java | ✅ | JVM Target 11 |
| Android SDK | ✅ | API 35 + 36.1 at `~/Library/Android/sdk` |
| ADB | ✅ | v37.0.0 |
| Gradle | ✅ | Bundled with the project (wrapper) |
| Kotlin | ✅ | 2.0.21 |

## Remaining Execution Checklist

- [x] Core architecture (Room, Hilt, Nav, Theme)
- [x] DashboardScreen
- [x] FoodLogScreen (partial — no add dialog)
- [x] WorkoutLogScreen (partial — no add dialog)
- [x] AICoachScreen (partial — needs full rewrite for export approach)
- [x] WeightLogScreen + ViewModel
- [x] **PHASE 0: Remove Firebase entirely**
- [x] **PHASE 1: App Lock (PIN + Biometric)**
- [ ] Sleep Log screen + ViewModel + entity migration
- [ ] Measurements Log screen + ViewModel
- [ ] Settings screen + ViewModel + calculateAllTargets() + data export/import
- [ ] Onboarding screen
- [ ] Food Log "Add Food" dialog + embedded food DB
- [ ] Workout Log "Start Workout" flow + exercise DB
- [ ] AI Coach rewrite (data export + prompt generator)
- [ ] Progress screen (4 tabs with Canvas charts)
- [ ] Photos screen (camera integration)
- [ ] Motivation screen (quotes + YouTube links)
- [ ] Personal Streak (its own PIN, biometrics, heatmap, milestones)
- [ ] Kenyan foods database (embedded data)
- [ ] Exercise database (embedded data)
- [ ] Calculation utilities (port from JS)
- [ ] App icon (adaptive icon)
- [ ] Splash screen
- [ ] Data export/import (JSON files)
- [ ] UX polish pass (animations, transitions — see 06_UX_STANDARDS.md)
