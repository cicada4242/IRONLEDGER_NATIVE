# IronLedger Native — Complete Dependency List

All dependencies are managed via the Gradle Version Catalog at:
`gradle/libs.versions.toml`

---

## Versions

| ID | Version | Notes |
|---|---|---|
| `agp` | 9.2.1 | Android Gradle Plugin |
| `kotlin` | 2.0.21 | Kotlin compiler |
| `ksp` | 2.0.21-1.0.28 | Kotlin Symbol Processing (for Room + Hilt) |
| `composeBom` | 2026.02.01 | Compose Bill of Materials |
| `room` | 2.6.1 | Room database |
| `hilt` | 2.59.2 | Dagger Hilt DI |
| `navigationCompose` | 2.8.5 | Navigation Compose |
| `firebaseBom` | 33.7.0 | Firebase BOM (configured, NOT actively used) |
| `retrofit` | 2.11.0 | Retrofit HTTP client |
| `gson` | 2.11.0 | Gson JSON serialization |
| `coroutines` | 1.9.0 | Kotlin Coroutines |
| `coreKtx` | 1.10.1 | AndroidX Core |
| `lifecycleRuntimeKtx` | 2.6.1 | Lifecycle runtime |
| `activityCompose` | 1.8.0 | Activity Compose |

---

## Libraries (in `app/build.gradle.kts`)

### Compose UI
```
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.activity.compose)
implementation(libs.androidx.compose.material3)
implementation(libs.androidx.compose.material.icons.extended)
implementation(libs.androidx.compose.ui)
implementation(libs.androidx.compose.ui.graphics)
implementation(libs.androidx.compose.ui.tooling.preview)
```

### AndroidX Core
```
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.lifecycle.runtime.ktx)
```

### Navigation
```
implementation(libs.androidx.navigation.compose)
```

### Room Database
```
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)
```

### Hilt Dependency Injection
```
implementation(libs.hilt.android)
ksp(libs.hilt.compiler)
implementation(libs.androidx.hilt.navigation.compose)
```

### Firebase (configured, NOT actively used)
```
implementation(platform(libs.firebase.bom))
implementation(libs.firebase.auth)
implementation(libs.firebase.firestore)
```

### Networking
```
implementation(libs.retrofit)
implementation(libs.retrofit.converter.gson)
implementation(libs.kotlinx.coroutines.android)
implementation(libs.kotlinx.coroutines.play.services)
```

### Testing
```
testImplementation(libs.junit)
androidTestImplementation(platform(libs.androidx.compose.bom))
androidTestImplementation(libs.androidx.compose.ui.test.junit4)
androidTestImplementation(libs.androidx.espresso.core)
androidTestImplementation(libs.androidx.junit)
debugImplementation(libs.androidx.compose.ui.test.manifest)
debugImplementation(libs.androidx.compose.ui.tooling)
```

---

## Gradle Plugins

```
alias(libs.plugins.android.application)
alias(libs.plugins.kotlin.compose)
alias(libs.plugins.ksp)
alias(libs.plugins.hilt)
alias(libs.plugins.google.services)   ← can be removed if Firebase is dropped
```

---

## Dependencies NOT Yet Added (Will Be Needed)

| Library | Purpose | When Needed |
|---|---|---|
| `androidx.biometric:biometric` | BiometricPrompt for PIN/fingerprint auth | App lock + Personal Streak |
| `io.github.patrykandpatrick.vico:compose-m3` | Charts (line, bar, area) | Weight Log, Sleep Log, Progress |
| `androidx.datastore:datastore-preferences` | Encrypted preferences for PIN hash | App lock |
| `androidx.camera:camera-camera2` | CameraX for progress photos | Photos screen |
| `androidx.core:core-splashscreen` | Splash Screen API | App startup |

---

## Firebase Note

> [!WARNING]
> Firebase Auth and Firestore are currently in the build file. If Firebase is no longer needed (user wants fully local app with PIN/biometric auth instead of Google Sign-In), these can be safely removed:
>
> 1. Remove from `app/build.gradle.kts`:
>    - `alias(libs.plugins.google.services)` from plugins block
>    - `implementation(platform(libs.firebase.bom))`
>    - `implementation(libs.firebase.auth)`
>    - `implementation(libs.firebase.firestore)`
>
> 2. Remove from root `build.gradle.kts`:
>    - `alias(libs.plugins.google.services) apply false`
>
> 3. Optionally delete `app/google-services.json`
>
> This will reduce APK size and eliminate the Firebase SDK initialization overhead.
