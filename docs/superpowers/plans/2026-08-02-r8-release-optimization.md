# R8 Release Optimization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Produce a signed DaysLite 1.0.9 release AAB with R8 code optimization and unused-resource shrinking enabled.

**Architecture:** Keep application code and debug behavior unchanged. Configure only the release variant to use Android's optimized default R8 rules plus a narrow project rules file, then verify the minified bundle, mapping output, metadata, and signature.

**Tech Stack:** Java, Android SDK 36/36.1, Android Gradle Plugin 8.13.1, Gradle 9.0.0, R8, JUnit 4.

## Global Constraints

- Minimum SDK remains API 26.
- Compile SDK remains Android 16 SDK 36.1.
- Target SDK remains API 36.
- Release identity becomes version code 9 and version name 1.0.9.
- No new runtime dependency, permission, data-format change, or unrelated refactor.
- Debug builds remain unminified.
- Google Play upload and rollout remain manual release steps.

---

### Task 1: Configure the optimized release variant

**Files:**
- Modify: `app/build.gradle:21-43`
- Create: `app/proguard-rules.pro`

**Interfaces:**
- Consumes: Android Gradle Plugin's `proguard-android-optimize.txt` defaults and the existing release signing configuration.
- Produces: version 1.0.9 release variants processed by R8 and the Android resource shrinker.

- [x] **Step 1: Verify the current build configuration fails the release policy assertion**

Run:

```bash
rg -n 'versionCode 9|versionName "1.0.9"|minifyEnabled true|shrinkResources true|proguard-android-optimize.txt' app/build.gradle
```

Expected: no matching output because the project is version 1.0.8 and explicitly disables minification.

- [x] **Step 2: Configure the next optimized release**

Set the release identity and build type to:

```groovy
versionCode 9
versionName "1.0.9"

release {
    signingConfig signingConfigs.release
    minifyEnabled true
    shrinkResources true
    proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
}
```

Create `app/proguard-rules.pro` with comments explaining that no custom keep rules are currently required and that future reflection-based entry points must use narrow keep rules.

- [x] **Step 3: Verify the release policy assertion passes**

Run:

```bash
rg -n 'versionCode 9|versionName "1.0.9"|minifyEnabled true|shrinkResources true|proguard-android-optimize.txt|proguard-rules.pro' app/build.gradle
```

Expected: one match for each required release setting.

- [x] **Step 4: Run unit tests and compile both variants**

Run:

```bash
./gradlew testDebugUnitTest compileDebugJavaWithJavac compileReleaseJavaWithJavac
```

Expected: `BUILD SUCCESSFUL` with all unit tests passing.

### Task 2: Build and verify the Play artifact

**Files:**
- Verify: `app/build/outputs/bundle/release/app-release.aab`
- Verify: `app/build/outputs/mapping/release/mapping.txt`
- Verify: `app/build/outputs/mapping/release/configuration.txt`
- Modify: `docs/release-build.md`

**Interfaces:**
- Consumes: the optimized release configuration and local release signing credentials.
- Produces: a signed, minified version 1.0.9 AAB and its R8 diagnostic files.

- [x] **Step 1: Build the release bundle**

Run:

```bash
./gradlew bundleRelease
```

Expected: `BUILD SUCCESSFUL` and `app/build/outputs/bundle/release/app-release.aab` exists.

- [x] **Step 2: Prove R8 ran and inspect the bundle metadata**

Run:

```bash
test -s app/build/outputs/mapping/release/mapping.txt
unzip -l app/build/outputs/bundle/release/app-release.aab
```

Expected: a non-empty `mapping.txt`; the archive lists the base module manifest, dex, and resources without packaging errors.

- [x] **Step 3: Verify the release signature**

Run:

```bash
jarsigner -verify app/build/outputs/bundle/release/app-release.aab
```

Expected: `jar verified.` If local signing credentials are unavailable, report the limitation and do not describe the AAB as upload-ready.

- [x] **Step 4: Update the release guide**

Update `docs/release-build.md` to identify version 1.0.9, version code 9, target SDK 36, the release AAB path, R8 status, resource-shrinking status, and the requirement to retain `mapping.txt` for Play Console deobfuscation.

- [x] **Step 5: Review the scoped diff and commit**

Run:

```bash
git diff --check
git diff -- app/build.gradle app/proguard-rules.pro docs/release-build.md
```

Expected: no whitespace errors and only the scoped R8 release changes.

Commit:

```bash
git add app/build.gradle app/proguard-rules.pro docs/release-build.md docs/superpowers/plans/2026-08-02-r8-release-optimization.md
git commit -m "build: enable R8 for release"
```
