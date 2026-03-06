# Contributing to RealLife Focus

Thank you for your interest in contributing! This document explains how to get started.

## Before You Start

- Check the [open issues](../../issues) to see if your idea or bug is already tracked.
- For large changes, open an issue first to discuss the approach before writing code.
- Review the [out-of-scope list](#out-of-scope) to avoid wasted effort.

## Development Setup

1. **Clone the repo** and open in Android Studio (Hedgehog or later).
2. **Sync Gradle** — all dependencies are defined in `app/build.gradle`.
3. **Connect a device or emulator** running Android 8.0+ (API 26+).
4. **Build and run**:
   ```bash
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

> Note: several features (Usage Stats, Overlay, Accessibility) require granting permissions in
> system settings that cannot be automated in the emulator. A physical device is recommended
> for end-to-end testing.

## Architecture

This project follows **MVVM** with Room, LiveData, and Coroutines. See [`CLAUDE.md`](CLAUDE.md)
for a full source map and architecture rules.

Key rules:
- Never query the database directly from an Activity or Fragment — go through the ViewModel/Repository.
- Use `viewModelScope.launch` inside ViewModels; `CoroutineScope(Dispatchers.IO)` inside services.
- All new Activities must be registered in `AndroidManifest.xml`.

## Submitting a Pull Request

1. Fork the repo and create a branch off `main`:
   ```bash
   git checkout -b fix/your-descriptive-name
   ```
2. Make your changes. Keep commits focused and atomic.
3. Verify the debug build compiles:
   ```bash
   ./gradlew assembleDebug
   ```
4. Open a pull request against `main`. Fill in the PR template.

### PR Checklist

- [ ] Build passes (`./gradlew assembleDebug`)
- [ ] No new lint warnings introduced
- [ ] UI strings are in `strings.xml`, not hardcoded
- [ ] New preferences go through `PreferenceManager`
- [ ] Database schema changes include a Room migration

## Code Style

- Kotlin only — no Java.
- Follow the existing code style (4-space indent, no wildcard imports).
- Keep functions short and single-purpose.

## Out of Scope

The following are explicitly **not** planned for v1 and PRs adding them will be closed:

- Website / browser blocking
- Cloud sync or remote management
- Family / parental controls
- iOS / cross-platform port
- Daily time limits ("max 2 hrs/day")

## Reporting Bugs

Use the [bug report template](../../issues/new?template=bug_report.md). Please include:
- Device model and Android version
- Steps to reproduce
- Expected vs. actual behaviour
- Logcat output if available
