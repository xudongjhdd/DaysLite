# R8 Release Optimization Design

**Date:** 2026-08-02

## Goal

Remove the Google Play recommendation to use R8, reduce the release bundle size, and prepare an uploadable successor to the already-published 1.0.8 release.

## Current State

The `release` build type explicitly sets `minifyEnabled false`, so Android Gradle Plugin does not run R8 for the published build. Resource shrinking is also disabled. The application has no third-party runtime dependencies, reflection, JNI libraries, or annotation-driven serializers that require custom keep rules.

## Chosen Approach

Update the next release to `versionCode 9` and `versionName 1.0.9`. For the `release` build type:

- Enable code shrinking and obfuscation with `minifyEnabled true`.
- Enable unused-resource removal with `shrinkResources true`.
- Use Android's optimized default ProGuard configuration together with a project-owned `proguard-rules.pro` file.
- Keep debug builds unchanged for fast local development and readable stack traces.

The project rules file will begin empty except for explanatory comments. Android Gradle Plugin supplies rules for manifest components, and the current source does not use reflection-based entry points that need broad keep rules. This preserves R8's optimization benefits without unnecessarily retaining application classes.

## Alternatives Considered

1. Enable R8 but leave resource shrinking disabled. This addresses only part of the optimization opportunity and was rejected because the app has a small, conventional resource tree suitable for safe shrinking.
2. Keep version 1.0.8 and only change the build configuration. This cannot be uploaded over an artifact that already uses `versionCode 8`, so it was rejected for the next Play release.

## Verification

The change is accepted when:

- Gradle unit tests pass.
- A signed release bundle builds successfully.
- The release output contains an `.aab` artifact.
- R8 produces `mapping.txt`, proving minification ran.
- The bundle can be inspected without missing-class or resource-shrinker errors.

If release signing credentials are unavailable in the local environment, an unsigned release compilation or equivalent release task will be used to validate R8, and the signing limitation will be reported explicitly.

## Rollback and Diagnostics

The generated `mapping.txt` must be retained with the Play release so obfuscated production stack traces can be retraced. If a release-only issue appears, reproduce it with the minified release build and add the narrowest required keep rule rather than disabling R8 globally.
