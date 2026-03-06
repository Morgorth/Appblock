# FocusLock

**Android Application Blocker** — v1.0

FocusLock helps users take control of their phone habits by blocking access to selected apps on a configurable schedule. The core philosophy is honest self-accountability: the user can always override a block in an emergency, but the friction of having to justify the override in writing is the primary deterrent.

## Features

- **App blocking schedules** — Block any user-installed app during user-defined time windows
- **Multiple profiles** — Create named profiles (e.g. "Work Hours", "Sleep Time", "Weekend Focus") with independent per-day-of-week schedules
- **Overnight schedules** — Supports windows that span midnight (e.g. 22:00–07:00)
- **Emergency override** — Bypass a block with mandatory written justification (minimum 20 characters); auto-reinstates after a configurable window (1–30 min, default 5 min)
- **Override history** — Local log of all overrides with justification text, timestamps, and app details
- **Fully offline** — No accounts, no network, no cloud sync

## Technical Architecture

### Android API Requirements
- **Minimum SDK**: API 26 (Android 8.0)
- **Target SDK**: API 34 (Android 14)

### Required Permissions
| Permission | Purpose |
|---|---|
| `PACKAGE_USAGE_STATS` | Detect foreground app via UsageStatsManager (primary) |
| `SYSTEM_ALERT_WINDOW` | Display block overlay above blocked apps |
| `BIND_ACCESSIBILITY_SERVICE` | Fallback foreground detection (OEM-reliable) |
| `RECEIVE_BOOT_COMPLETED` | Restart service after reboot |
| `FOREGROUND_SERVICE` | Keep monitoring service alive |

### Architecture Components
- **MVVM** with `ViewModel` + `LiveData`
- **Room** (SQLite) for local data persistence
- **WorkManager** for watchdog periodic task (restarts service if killed by OEM)
- **LifecycleService** for the background blocker service

### Project Structure
```
app/src/main/java/com/focuslock/
├── FocusLockApplication.kt        # App singleton, DI root
├── data/
│   ├── db/
│   │   ├── AppDatabase.kt         # Room database
│   │   ├── dao/                   # DAO interfaces
│   │   └── entities/              # Room entities
│   └── repository/                # Repository layer
├── services/
│   ├── BlockerService.kt          # Foreground service (UsageStats polling)
│   ├── FocusLockAccessibilityService.kt  # Accessibility fallback
│   ├── BootReceiver.kt            # Restarts service after reboot
│   └── BlockerWatchdogWorker.kt   # WorkManager watchdog
├── ui/
│   ├── onboarding/                # Multi-step permission setup
│   ├── main/                      # Profile list screen
│   ├── profile/                   # Profile editor
│   ├── apps/                      # App picker
│   ├── overlay/                   # Block overlay screen
│   ├── settings/                  # Global settings
│   └── history/                   # Override history
├── viewmodel/                     # ViewModels
└── utils/                         # Permission, OEM, time utilities
```

## Building

1. Open in Android Studio (Hedgehog or later)
2. Sync Gradle
3. Build → Make Project

```bash
./gradlew assembleDebug
```

## Key Design Decisions

### Blocking Mechanism
The `BlockerService` runs as a persistent foreground service and polls `UsageStatsManager.queryUsageStats()` every 1500ms. When a blocked app is detected during an active schedule window, it launches `BlockOverlayActivity` with `FLAG_ACTIVITY_NEW_TASK` to appear on top.

### Accessibility Fallback
`FocusLockAccessibilityService` listens for `TYPE_WINDOW_STATE_CHANGED` events as a complementary mechanism. This is especially important on Xiaomi (MIUI), Huawei (EMUI), and other OEMs that restrict `UsageStatsManager` access for background processes.

### Override Friction
The override requires the user to type at least 20 characters of justification before the confirm button activates. There is no PIN or secondary confirmation — the friction is entirely the act of writing a reason. This is intentional: the goal is self-reflection, not an unbreakable lock.

### OEM Battery Whitelisting
On detected problematic OEMs (Xiaomi, Huawei, Samsung, OPPO, Vivo, OnePlus), the onboarding flow prompts users to whitelist FocusLock in the manufacturer's battery settings to prevent the service from being killed.

## Out of Scope (v1)
- Website / browser blocking
- Remote management or family controls
- Cloud backup of profiles
- iOS version
- Scheduled daily limits (e.g. "2 hrs/day max")
