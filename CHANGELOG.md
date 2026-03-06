# Changelog

All notable changes to RealLife Focus are documented here.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).
This project uses [Semantic Versioning](https://semver.org/).

---

## [1.0.0] — 2024-03-06

### Added
- **App blocking schedules** — block any installed app during user-defined time windows, per day of week
- **Multiple profiles** — create named profiles (e.g. "Work Hours", "Sleep Time") with independent schedules
- **Overnight schedule support** — windows that span midnight (e.g. 22:00–07:00) work correctly
- **Emergency override** — bypass a block with mandatory written justification (≥20 characters); reinstates automatically after a configurable window (1–30 min, default 5 min)
- **Override history** — local log of all overrides with justification text, timestamps, and app details
- **Shared-time schedule sync** — changing time on one day propagates to all synced days in the profile
- **Accessibility service fallback** — secondary detection via AccessibilityService for Xiaomi, Huawei, and other OEMs that restrict background UsageStats
- **OEM battery whitelist onboarding** — prompts users on problematic OEMs to whitelist the app from battery optimisation
- **WorkManager watchdog** — restarts the blocker service every 15 minutes if killed by aggressive battery management
- **Boot receiver** — restarts the service after device reboot or app update
- **Fully offline** — no accounts, no network, no data leaves the device

### Technical
- MVVM architecture with Room, LiveData, and Coroutines
- Room database v1 with exported schema
- Foreground service declared as `specialUse` (Android 14 requirement)
- Network security config blocks all cleartext traffic
