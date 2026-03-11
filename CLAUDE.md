# Focus Real Life — Claude Code Skills

## App Overview

**Focus Real Life** is an Android app blocker (Kotlin, API 26–34) that helps users restrict access to selected apps on a configurable schedule. Users can temporarily override blocks but must write a ≥20-character justification (friction-based accountability).

**App name:** Focus Real Life
**Package namespace:** `com.focuslock`
**Repo:** https://github.com/Morgorth/Appblock
**Build tool:** Gradle 8.2 (`./gradlew`)
**Architecture:** MVVM + Room + LiveData + Coroutines + WorkManager
**Min SDK:** 26 (Android 8.0) | **Target SDK:** 34 (Android 14)

---

## Build & Deploy Workflow

Always set env vars before Gradle or ADB commands:
```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
export ANDROID_HOME=/Users/nathanrudman/Library/Android/sdk
```

Standard cycle for every change:
```bash
# 1. Build
./gradlew assembleDebug

# 2. Install on connected phone
/Users/nathanrudman/Library/Android/sdk/platform-tools/adb install -r app/build/outputs/apk/debug/app-debug.apk

# 3. Commit & push
git add <files>
git commit -m "message"
git push
```

Shortcut — installDebug does build + install in one step:
```bash
./gradlew installDebug
```

Check connected device:
```bash
/Users/nathanrudman/Library/Android/sdk/platform-tools/adb devices
```

Pull crash logs:
```bash
/Users/nathanrudman/Library/Android/sdk/platform-tools/adb logcat -d | grep -E "FATAL|AndroidRuntime|focuslock"
```

---

## Key Source Map

| What to change | File |
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
| OEM battery whitelist | `app/src/main/java/com/focuslock/utils/OemUtils.kt` |
| Time formatting / schedule helpers | `app/src/main/java/com/focuslock/utils/TimeUtils.kt` |
| Notifications | `app/src/main/java/com/focuslock/utils/NotificationUtils.kt` |
| Accessibility service fallback | `app/src/main/java/com/focuslock/services/FocusLockAccessibilityService.kt` |
| Watchdog (WorkManager, 15-min) | `app/src/main/java/com/focuslock/services/BlockerWatchdogWorker.kt` |
| Boot/reboot restart | `app/src/main/java/com/focuslock/services/BootReceiver.kt` |
| App singleton / DI root | `app/src/main/java/com/focuslock/FocusLockApplication.kt` |
| Manifest (permissions, components) | `app/src/main/AndroidManifest.xml` |
| Gradle dependencies | `app/build.gradle` |
| Layouts | `app/src/main/res/layout/` |
| Strings / colors / styles / themes | `app/src/main/res/values/` |

---

## Architecture Rules

### MVVM Data Flow
```
Activity → ViewModel → Repository → DAO → Room DB
                           ↓
                     LiveData pushes back to UI
```
- Never query DB directly from Activities — always go through ViewModel → Repository.
- Use `viewModelScope.launch` in ViewModels, `CoroutineScope(Dispatchers.IO)` in services.

### Database Changes
When modifying Room entities:
1. Increment `version` in `AppDatabase.kt`
2. Add a migration or use `fallbackToDestructiveMigration()` during dev
3. Build to verify Room schema compiles

### Adding a New Screen
1. Create `Activity` + `ViewModel` + layout XML
2. Register in `AndroidManifest.xml`
3. Access repos via `(application as FocusLockApplication).profileRepository`
4. Use ViewBinding: `ActivityXxxBinding.inflate(layoutInflater)`

### Adding a New Preference
1. Add key constant + getter/setter in `PreferenceManager.kt`
2. Expose in `SettingsActivity`
3. Consume in `BlockerService` or relevant component

---

## Critical Implementation Details

### Polling & Detection
- `BlockerService` polls `UsageStatsManager` every **1500ms**
- `FocusLockAccessibilityService` is a fallback for Xiaomi/Huawei/OEM devices that restrict UsageStats
- Both run in parallel — don't remove either without OEM testing

### Schedule Logic (overnight support)
```kotlin
// Supports windows that span midnight, e.g. 22:00–07:00
if (endMins > startMins) currentMins in startMins until endMins
else currentMins >= startMins || currentMins < endMins
```
Day mapping: 1=Mon … 7=Sun (NOT Android Calendar's 1=Sun)

### Override System
- Active override stored in SharedPreferences: `activeOverridePackage` + `activeOverrideExpiryMs`
- Countdown coroutine in `BlockerService` updates notification every second
- Every override writes a permanent record to `override_logs` table
- Override history count shown on block overlay to shame the user (amber text)
- Override duration configurable, default 5 min

### Overlay Activity
- `launchMode="singleInstance"` + `excludeFromRecents="true"` + `showOnLockScreen="true"`
- Must use `FLAG_ACTIVITY_NEW_TASK` when launching from service
- Confirm button gated on ≥20 chars justification (TextWatcher)

### Foreground Service
- Declared as `foregroundServiceType="specialUse"`
- `BlockerWatchdogWorker` (WorkManager 15-min) restarts it if killed
- `BootReceiver` restarts after reboot

### Theme
- All activities use `Theme.FocusLock` (NoActionBar) — activities that need a toolbar include one in their layout and call `setSupportActionBar()`
- Overlay uses `Theme.FocusLock.Overlay`

---

## Permissions Checklist

| Permission | Purpose |
|---|---|
| `PACKAGE_USAGE_STATS` | Detect foreground app — requires user grant in system settings |
| `SYSTEM_ALERT_WINDOW` | Draw overlay above blocked apps |
| `BIND_ACCESSIBILITY_SERVICE` | Accessibility fallback |
| `FOREGROUND_SERVICE` | Keep blocking service alive |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` | Bypass OEM battery kill |
| `QUERY_ALL_PACKAGES` | Enumerate installed apps for picker |

All permissions declared in `AndroidManifest.xml` and checked at runtime via `PermissionUtils.kt`.

---

## Common Patterns

```kotlin
// Observe LiveData
viewModel.profiles.observe(this) { profiles -> adapter.submitList(profiles) }

// Coroutine in ViewModel
viewModelScope.launch { repository.createProfile(name, duration) }

// Access repo from Activity/Service
val repo = (application as FocusLockApplication).profileRepository

// ViewBinding
binding = ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)

// Start service safely
if (PermissionUtils.allCorePermissionsGranted(context)) {
    BlockerService.start(context)
}
```

---

## Out of Scope (v1.0)

Do not add: website/browser blocking, cloud sync, remote/family controls, iOS, daily time limits.
