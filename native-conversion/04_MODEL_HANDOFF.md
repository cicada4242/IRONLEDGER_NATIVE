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
| **Consultation Workflow** | Architectural changes reviewed by Superior LLMs | Ensures senior-level patterns and future-proofing. |

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

## What Has Already Been Done

- [x] **Phase 0: Firebase Removal** — 100% complete.
- [x] **Phase 1: App Lock** — PIN + Biometric gatekeeper (MainActivity gating).
- [x] **Phase 4: Data & Utilities** — Kenyan Foods, Exercises, Quotes, Videos, and Calculations ported.
- [x] **Senior Patterns:** Room Migration 1->2 (Sleep Log) and Linear Regression for trends.
- [x] Core architecture (Room, Hilt, Nav, Theme)
- [x] Reusable component library (`IronLedgerComponents.kt`)

## Remaining Execution Checklist

- [ ] Sleep Log screen + ViewModel (Migration is DONE)
- [ ] Measurements Log screen + ViewModel
- [ ] Settings screen + ViewModel + calculateAllTargets() + data export/import
- [ ] Onboarding screen
- [ ] Food Log "Add Food" dialog + embedded food DB search
- [ ] Workout Log "Start Workout" flow + exercise DB
- [ ] AI Coach rewrite (data export + prompt generator - Markdown format)
- [ ] Progress screen (4 tabs with Canvas charts)
- [ ] Photos screen (camera integration)
- [ ] Personal Streak (its own PIN, biometrics, heatmap, milestones)
- [ ] App icon (adaptive icon)
- [ ] Splash screen
- [ ] Data export/import (JSON files)
- [ ] UX polish pass (animations, transitions — see 06_UX_STANDARDS.md)

## Critical Warnings & Tips

- **Consultation Workflow:** BEFORE implementing any major new module (AICoach, Progress, Personal Streak), you MUST draft a detailed technical prompt for the user to run through a "Superior LLM" (Claude 3.5 Sonnet / GPT-4o). Incorporate its feedback into the plan. If the response is ambiguous, ask follow-up questions before coding.
- **Android Studio Agent:** The user has an AI agent active in Android Studio that can help fix compilation or build errors. If you get stuck on a complex build issue, ask the user to use their Studio agent to help.
- **Package Name Conflict:** The device might have an old Capacitor build with `com.ironledger.app`. Always `adb uninstall com.ironledger.app` if the old sign-in screen appears.
- **Room Migrations:** Always write `Migration` objects for schema changes (see `Migrations.kt`). Do NOT use destructive migrations.
- **UI Consistency:** Always use the components in `ui/components/IronLedgerComponents.kt`.
- **AI Coach is NOT API-based.** It generates a data summary + prompt (Markdown format) that the user exports/copies and pastes into any external LLM. Use **Linear Regression** for weight trends.
- **Personal Streak is PRIVATE.** Has its own separate PIN (different from app lock PIN). Must be excluded from data exports. Never mention the specific vice.
- **UX MUST BE PREMIUM.** Read `06_UX_STANDARDS.md`. Every screen needs entry animations, every card needs press feedback, every transition must be smooth.
