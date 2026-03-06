# RealLife Focus

![Android CI](https://github.com/Morgorth/Appblock/actions/workflows/android.yml/badge.svg)
![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)
![Min SDK](https://img.shields.io/badge/Android-8.0%2B-green.svg)
![Target SDK](https://img.shields.io/badge/Target-API%2034-green.svg)

**RealLife Focus** is a free, open-source Android app blocker that helps you take back control of
your phone habits. Block distracting apps on a configurable schedule — and if you need access in
an emergency, you can override the block, but only after writing a reason. The friction of
justifying yourself is the point.

> **Fully offline.** No accounts, no network, no data ever leaves your device.

---

## Features

- **App blocking schedules** — block any user-installed app during named time windows
- **Multiple profiles** — "Work Hours", "Sleep Time", "Weekend Focus" — each with independent per-day-of-week schedules
- **Overnight support** — windows that span midnight (e.g. 22:00–07:00) work correctly
- **Emergency override** — bypass a block with a mandatory written justification (≥20 characters); automatically reinstates after 1–30 min (configurable)
- **Override history** — local audit log of every override with justification, timestamp, and app details
- **Shame counter** — the overlay shows how many times you've already overridden a given app this session
- **OEM reliability** — dual detection via UsageStatsManager + AccessibilityService fallback for Xiaomi, Huawei, and other restrictive OEMs
- **WorkManager watchdog** — restarts the service every 15 minutes if killed by battery optimisation
- **Boot persistence** — service restarts automatically after reboot or app update

---

## Installation

### From Google Play
*(Coming soon)*

### Build from Source

**Requirements:**
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK with API 26–34

```bash
git clone https://github.com/Morgorth/Appblock.git
cd Appblock
./gradlew assembleDebug
# Install on a connected device:
./gradlew installDebug
```

The release APK is built with:
```bash
./gradlew assembleRelease
```
Output: `app/build/outputs/apk/release/app-release.apk`

---

## Required Permissions

| Permission | Why it's needed |
|---|---|
| `PACKAGE_USAGE_STATS` | Detect which app is in the foreground (primary method) |
| `SYSTEM_ALERT_WINDOW` | Draw the block overlay above other apps |
| `BIND_ACCESSIBILITY_SERVICE` | Fallback foreground detection on restrictive OEMs |
| `RECEIVE_BOOT_COMPLETED` | Restart the service after reboot |
| `FOREGROUND_SERVICE` | Keep the monitoring service alive in the background |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` | Prevent OEMs from killing the service |
| `QUERY_ALL_PACKAGES` | Enumerate installed apps in the app picker |
| `POST_NOTIFICATIONS` | Show the persistent status notification (Android 13+) |

All sensitive permissions are explained during onboarding, with direct links to the relevant system settings screen.

---

## Architecture

```
Activity/Fragment → ViewModel → Repository → DAO → Room DB
                                    ↓
                              LiveData pushes updates back to UI
```

- **MVVM** with `ViewModel` + `LiveData`
- **Room** (SQLite) for all local persistence
- **WorkManager** for the 15-minute watchdog
- **LifecycleService** for the foreground blocker service
- **Kotlin Coroutines** throughout — `viewModelScope` in ViewModels, `Dispatchers.IO` in services

### Project Structure

```
app/src/main/java/com/focuslock/
├── FocusLockApplication.kt        # App singleton, DI root
├── data/
│   ├── db/
│   │   ├── AppDatabase.kt         # Room database (v1)
│   │   ├── dao/                   # DAO interfaces
│   │   └── entities/              # Room entities
│   └── repository/                # Single source of truth
├── services/
│   ├── BlockerService.kt          # Foreground service — UsageStats polling (1500ms)
│   ├── FocusLockAccessibilityService.kt  # Accessibility fallback
│   ├── BootReceiver.kt            # Restarts service after reboot / update
│   └── BlockerWatchdogWorker.kt   # WorkManager watchdog (15 min)
├── ui/
│   ├── onboarding/                # Multi-step permission setup
│   ├── main/                      # Profile list
│   ├── profile/                   # Profile editor + schedule
│   ├── apps/                      # App picker (multi-select)
│   ├── overlay/                   # Block overlay screen
│   ├── settings/                  # Global settings
│   └── history/                   # Override audit log
├── viewmodel/                     # ViewModels
└── utils/                         # Permissions, OEM detection, time helpers
```

---

## Key Design Decisions

### Blocking Mechanism
`BlockerService` polls `UsageStatsManager.queryUsageStats()` every 1500ms. When a blocked app
is detected during an active schedule window, it launches `BlockOverlayActivity` with
`FLAG_ACTIVITY_NEW_TASK` to appear on top.

### Accessibility Fallback
`FocusLockAccessibilityService` listens for `TYPE_WINDOW_STATE_CHANGED` events as a second layer.
This is critical on Xiaomi (MIUI), Huawei (EMUI), and Samsung OneUI, which restrict background
UsageStats access for third-party apps.

### Override Friction
The override requires at least 20 characters of justification before the confirm button activates.
There is no PIN, no secondary confirmation, no timer. The friction is entirely the act of writing
a reason — enough to cause a moment of reflection, not enough to be unbreakable.

### Schedule Logic (overnight support)
```kotlin
if (endMins > startMins) currentMins in startMins until endMins  // normal
else currentMins >= startMins || currentMins < endMins             // overnight
```

Day mapping: 1 = Monday … 7 = Sunday (not Android's Calendar 1 = Sunday).

---

## Privacy

RealLife Focus processes everything locally:
- No network requests are made (enforced via `network-security-config`)
- No analytics, no crash reporting, no telemetry
- No account required
- All data (profiles, schedules, override history) is stored in a local SQLite database that stays on your device

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for how to set up the project, the architecture rules,
and the PR process.

## Changelog

See [CHANGELOG.md](CHANGELOG.md).

## License

[MIT](LICENSE) © RealLife Focus contributors
