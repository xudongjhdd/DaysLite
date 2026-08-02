# DaysLite Release Build Guide

This guide prepares DaysLite for Google Play release builds without committing private signing files or passwords.

## Current Build Targets

Debug APK:

```bash
./gradlew assembleDebug
```

Release AAB for Google Play:

```bash
./gradlew bundleRelease
```

Release APK for local smoke testing:

```bash
./gradlew assembleRelease
```

## Release Signing

The project reads signing values from `local.properties`. This file is ignored by Git and must stay local.

Add these keys after creating a release keystore:

```properties
RELEASE_STORE_FILE=keystore/dayslite-release.jks
RELEASE_STORE_PASSWORD=replace-with-store-password
RELEASE_KEY_ALIAS=dayslite
RELEASE_KEY_PASSWORD=replace-with-key-password
```

Do not commit:

- `local.properties`
- `*.jks`
- `*.keystore`
- signing passwords

## Keystore Location

Recommended local-only path:

```text
keystore/dayslite-release.jks
```

The `keystore/` directory should be ignored by Git.

Current local setup already uses this path. Before the first real Play upload, use a strong password, keep `local.properties` private, and back up the keystore somewhere safe. Losing the upload key can block future release updates until the key reset flow is completed.

## Versioning

Before each Play release, update:

```text
app/build.gradle
  versionCode
  versionName
```

Rules:

- `versionCode` must increase every uploaded release.
- `versionName` is user-facing, such as `1.0.0`.

## Pre-Upload Checklist

- Run `./gradlew bundleRelease`
- Confirm generated AAB exists at `app/build/outputs/bundle/release/`
- Verify the AAB signature with `jarsigner -verify app/build/outputs/bundle/release/app-release.aab`
- Retain `app/build/outputs/mapping/release/mapping.txt` for Play Console deobfuscation and production crash analysis
- Install and smoke-test a debug build on a real device
- Confirm package name is `com.dayslite.countdown`
- Confirm privacy policy URL is ready
- Confirm Play Console Data Safety answers match the final APK/AAB

## Current Release Candidate

- Version name: `1.0.9`
- Version code: `9`
- Package name: `com.dayslite.countdown`
- Target SDK: `36`
- Release AAB: `app/build/outputs/bundle/release/app-release.aab`
- AAB signature: verified
- R8 code shrinking, optimization, and obfuscation: enabled
- Unused resource shrinking: enabled
- R8 mapping: `app/build/outputs/mapping/release/mapping.txt`
- Sensitive permissions: none
- Cloud backup and device transfer: disabled for countdown data
