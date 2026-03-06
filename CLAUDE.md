# FocusLock — Claude Code Skills

## App Overview

**FocusLock** is an Android app blocker (Kotlin, API 26–34) that helps users restrict access to selected apps on a configurable schedule. Users can temporarily override blocks but must write a ≥20-character justification (friction-based accountability).

**Package namespace:** `com.focuslock`
**Build tool:** Gradle 8.2.2 (./gradlew)
**Architecture:** MVVM + Room + LiveData + Coroutines + WorkManager

---

## Key Source Map

| What you want to change | File |
|---|---|
| App blocking logic / polling loop | `app/src/main/java/com/focuslock/services/BlockerService.kt` |
| Schedule evaluation (time windows) | `app/src/main/java/com/focuslock/data/repository/ProfileRepository.kt` |
| Override friction UI (overlay screen) | `app/src/main/java/com/focuslock/ui/overlay/BlockOverlayActivity.kt` |
| Profile list / home screen | `app/src/main/java/com/focuslock/ui/main/MainActivity.kt` |
| Profile create/edit + schedule | `app/src/main/java/com/focuslock/ui/profile/ProfileEditorActivity.kt` |
| App picker (multi-select) | `app/src/main/java/com/focuslock/ui/apps/AppPickerActivity.kt` |
| Override history log | `app/src/main/java/com/focuslock/ui/history/OverrideHistoryActivity.kt` |
| Permissions onboarding | `app/src/main/java/com/focuslock/ui/onboarding/OnboardingActivity.kt` |
| Global settings screen | `app/src/main/java/com/focuslock/ui/settings/SettingsActivity.kt` |
| Database schema (entities) | `app/src/main/java/com/focuslock/data/db/entities/` |
| DAO queries | `app/src/main/java/com/focuslock/data/db/dao/` |
| SharedPreferences keys | `app/src/main/java/com/focuslock/utils/PreferenceManager.kt` |
| Permission checking | `app/src/main/java/com/focuslock/utils/PermissionUtils.kt` |
| OEM-specific battery whitelist | `app/src/main/java/com/focuslock/utils/OemUtils.kt` |
| Time formatting / schedule helpers | `app/src/main/java/com/focuslock/utils/TimeUtils.kt` |
| Notifications | `app/src/main/java/com/focuslock/utils/NotificationUtils.kt` |
| Accessibility service fallback | `app/src/main/java/com/focuslock/services/FocusLockAccessibilityService.kt` |
| Watchdog (WorkManager, 15-min) | `app/src/main/java/com/focuslock/services/BlockerWatchdogWorker.kt` |
| Boot/reboot restart | `app/src/main/java/com/focuslock/services/BootReceiver.kt` |
| App singleton / DI root | `app/src/main/java/com/focuslock/FocusLockApplication.kt` |
| Manifest (permissions, components) | `app/src/main/AndroidManifest.xml` |
| Gradle dependencies | `app/build.gradle` |
| XML layouts | `app/src/main/res/layout/` |
| Strings / colors / styles | `app/src/main/res/values/` |

---

## Architecture Rules

### MVVM Data Flow
```
Activity/Fragment → ViewModel → Repository → DAO → Room DB
                                    ↓
                              LiveData pushes updates back to UI
```
- ViewModels hold LiveData; Activities observe it — never query DB directly from UI.
- Repositories are the single source of truth; all DB calls go through them.
- Use `viewModelScope.launch` for coroutines inside ViewModels.
- Use `CoroutineScope(Dispatchers.IO)` inside services/workers.

### Database Changes
When modifying Room entities:
1. Increment `version` in `AppDatabase.kt`.
2. Add a migration (`addMigrations(...)`) or set `fallbackToDestructiveMigration()` for dev.
3. Run `./gradlew assembleDebug` to verify Room schema compilation.

### Adding a New Screen
1. Create `Activity` + `ViewModel` + layout XML.
2. Register the Activity in `AndroidManifest.xml`.
3. Inject repositories via `(application as FocusLockApplication).profileRepository` pattern.
4. Use ViewBinding (already enabled): `ActivityXxxBinding.inflate(layoutInflater)`.

### Adding a New Preference/Setting
1. Add key constant to `PreferenceManager.kt`.
2. Add getter/setter in `PreferenceManager`.
3. Expose in `SettingsActivity` (via `PreferenceScreen` XML or programmatically).
4. Consume in `BlockerService` or relevant component.

---

## Critical Implementation Details

### Polling & Detection
- `BlockerService` polls `UsageStatsManager` every **1500ms**.
- `FocusLockAccessibilityService` provides immediate fallback on Xiaomi/Huawei/OEM devices that restrict background UsageStats.
- Both run in parallel — do not remove either without OEM testing.

### Schedule Logic (overnight support)
```kotlin
// In ProfileRepository — supports e.g. 22:00–07:00 spanning midnight
if (endMins > startMins) currentMins in startMins until endMins
else currentMins >= startMins || currentMins < endMins
```
Day mapping: FocusLock uses 1=Mon…7=Sun (not Android Calendar's 1=Sun).

### Override System
- Active override stored in SharedPreferences: `activeOverridePackage` + `activeOverrideExpiryMs`.
- Countdown coroutine in `BlockerService` updates notification every second.
- Permanent audit record written to `override_logs` table on each override.
- Override duration configurable per-profile (1–30 min), default 5 min.

### Overlay Activity
- `launchMode="singleInstance"` + `excludeFromRecents="true"` + `showOnLockScreen="true"`.
- Must use `FLAG_ACTIVITY_NEW_TASK` when launching from service.
- Override confirm button gated on ≥20 chars (real-time TextWatcher validation).

### Foreground Service
- Requires persistent notification (Android 8+).
- Declared as `foregroundServiceType="specialUse"` in manifest.
- `BlockerWatchdogWorker` (WorkManager, 15-min) restarts it if killed by battery optimization.
- `BootReceiver` restarts it after reboot (`BOOT_COMPLETED` + `MY_PACKAGE_REPLACED`).

---

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Clean build artifacts
./gradlew clean

# Full build (all variants)
./gradlew build

# Install on connected device
./gradlew installDebug
```

Output APK: `app/build/outputs/apk/debug/app-debug.apk`

---

## Permissions Checklist

When adding features, consider whether you need:
- `PACKAGE_USAGE_STATS` — reading foreground app (requires user grant in system settings)
- `SYSTEM_ALERT_WINDOW` — drawing overlay above other apps
- `BIND_ACCESSIBILITY_SERVICE` — accessibility event listening
- `FOREGROUND_SERVICE` — persistent background service
- `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` — bypass Doze/battery kill
- `QUERY_ALL_PACKAGES` — enumerate installed apps (needed for app picker)

All permissions must be declared in `AndroidManifest.xml` **and** checked at runtime via `PermissionUtils.kt`.

---

## Common Patterns

### Observe LiveData in Activity
```kotlin
viewModel.profiles.observe(this) { profiles ->
    adapter.submitList(profiles)
}
```

### Launch coroutine from ViewModel
```kotlin
viewModelScope.launch {
    repository.createProfile(name, duration)
}
```

### Access repositories from Activity/Service
```kotlin
val repo = (application as FocusLockApplication).profileRepository
```

### ViewBinding setup
```kotlin
private lateinit var binding: ActivityMainBinding

override fun onCreate(...) {
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
}
```

### Start BlockerService (check permissions first)
```kotlin
if (PermissionUtils.allCorePermissionsGranted(context)) {
    ContextCompat.startForegroundService(context, Intent(context, BlockerService::class.java))
}
```

---

## Out of Scope (v1.0)

Do not add: website/browser blocking, cloud sync, remote/family controls, iOS support, daily time limits (e.g., "2 hrs/day max"). These are explicitly deferred.

---

## Git Branch

Active development branch: `claude/document-app-skills-aGEqj`
