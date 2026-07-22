# Android 16 Target API Upgrade Design

## Objective

Update DaysLite so that its next production release targets Android 16 (API level 36) and satisfies the Google Play target API requirement effective August 30, 2026.

## Scope

- Compile the application against the installed Android 16 SDK 36.1.
- Change the application's target SDK from API 35 to API 36.
- Increment the release identity from version code 7 / version name 1.0.7 to version code 8 / version name 1.0.8.
- Verify unit tests, Android Lint, debug packaging, and release App Bundle generation.
- Review build and Lint output for Android 16 compatibility issues that affect the existing app.

## Non-goals

- No feature, UI, storage-format, permission, architecture, or dependency changes.
- No Google Play Console submission or production rollout.
- No unrelated refactoring.

## Build Configuration

`app/build.gradle` will use the Android 16 SDK installed locally:

- `compileSdk 36.1`
- `targetSdk 36`
- `versionCode 8`
- `versionName "1.0.8"`

The minimum supported Android version remains API 26. The current Android Gradle Plugin 8.13.1, Gradle 9.0.0 wrapper, and local Build Tools 36.1.0 remain unchanged unless verification demonstrates a concrete incompatibility.

## Compatibility Assessment

DaysLite is a small offline application with no third-party runtime dependencies, no exported component other than the launcher activity, and no sensitive runtime permissions. The upgrade therefore focuses on build configuration and behavior-change validation. Existing countdown calculation, local persistence, language switching, and privacy-policy presentation must remain unchanged.

## Verification

The implementation is accepted when all of the following complete successfully:

1. JVM unit tests pass.
2. Android Lint completes without release-blocking findings.
3. A debug APK builds against Android 16.
4. A signed release AAB builds when the repository's existing local signing configuration is available.
5. The generated artifact metadata reports target SDK 36 and version code 8.

If release signing credentials are unavailable, the unsigned or signing-independent release build checks will be run and the missing local credential prerequisite will be reported explicitly.

## Release Handoff

After verification, the generated AAB is ready for internal or closed testing. Google Play compliance is completed only after version 1.0.8 is promoted to production before the stated deadline.
