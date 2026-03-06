# Appblock

An Android app-blocker targeting **Google Pixel 8** and **Pixel 9** (and any Android device running Android 8+).

## Pixel 8 / Pixel 9 Compatibility

| Feature | Details |
|---|---|
| **Edge-to-edge** | `WindowCompat.setDecorFitsSystemWindows(false)` + insets listener. Android 15 (Pixel 9) enforces this; we opt-in on Android 14 (Pixel 8) for consistency. |
| **Punch-hole camera** | `windowLayoutInDisplayCutoutMode="shortEdges"` in both the manifest and theme so content extends behind the cutout without a black letterbox. |
| **Predictive back gesture** | `android:enableOnBackInvokedCallback="true"` in manifest + `OnBackInvokedDispatcher` callback for Android 13+. |
| **Notification permission** | `POST_NOTIFICATIONS` runtime permission requested at launch (required Android 13+ / Pixel 8+). |
| **Exact alarms** | Both `SCHEDULE_EXACT_ALARM` and `USE_EXACT_ALARM` declared; `USE_EXACT_ALARM` is pre-granted on Android 13+ for scheduling apps. |
| **Foreground service type** | `foregroundServiceType="specialUse"` declared for Android 14+ (required on Pixel 8 and later). |
| **PendingIntent flags** | `FLAG_IMMUTABLE` used everywhere – mandatory on Android 12+ (Pixel 8/9). |
| **Material You** | Theme extends `Theme.Material3.DayNight.NoActionBar` so dynamic colour from the Pixel wallpaper engine works automatically. |

## Minimum Requirements

- **minSdk**: 26 (Android 8.0)
- **targetSdk**: 35 (Android 15 / Pixel 9)
- **compileSdk**: 35

## Permissions Required

| Permission | Why |
|---|---|
| `PACKAGE_USAGE_STATS` | Detect which app is in the foreground |
| `QUERY_ALL_PACKAGES` | List installed apps to block |
| `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_SPECIAL_USE` | Keep blocker alive on Pixel 8/9 with Doze |
| `POST_NOTIFICATIONS` | Show persistent blocking status (Android 13+) |
| `RECEIVE_BOOT_COMPLETED` | Restart blocker after reboot |
| `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM` | Timed block schedules |

## Build

```bash
./gradlew assembleDebug
```
