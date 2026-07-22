# Android 16 Target API Upgrade Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Produce DaysLite version 1.0.8 targeting Android 16 API level 36, with build artifacts and back navigation compatible with Android 16.

**Architecture:** Keep the application structure and minimum SDK unchanged. Update the Android build DSL to compile with the locally installed Android 16 QPR2 SDK while targeting base API 36, then route both legacy and Android 16 back events through one navigation method.

**Tech Stack:** Java, Android SDK 36/36.1, Android Gradle Plugin 8.13.1, Gradle 9.0.0, JUnit 4.

## Global Constraints

- Minimum SDK remains API 26.
- Compile SDK is Android 16 SDK 36.1.
- Target SDK is API 36.
- Release identity is version code 8 and version name 1.0.8.
- No new runtime dependency, permission, feature, storage-format change, or unrelated refactor.
- Google Play submission and rollout remain manual release steps.

---

### Task 1: Upgrade the Android build target and release identity

**Files:**
- Modify: `app/build.gradle:13-24`

**Interfaces:**
- Consumes: installed SDK platform `android-36.1`, Build Tools `36.1.0`, AGP 8.13.1.
- Produces: application variants compiled with Android 16 SDK 36.1, targeting API 36, version code 8, version name 1.0.8.

- [ ] **Step 1: Verify the existing configuration fails the new policy assertion**

Run:

```bash
rg -n 'compileSdk 36|targetSdk 36|versionCode 8|versionName "1.0.8"' app/build.gradle
```

Expected: no matching output because the current file uses API 35 and version 1.0.7.

- [ ] **Step 2: Update the Android build configuration**

Replace the compile SDK, Build Tools, and release identity with:

```groovy
android {
    namespace "com.dayslite.countdown"
    compileSdk {
        version = release(36) {
            it.minorApiLevel = 1
        }
    }
    buildToolsVersion "36.1.0"

    defaultConfig {
        applicationId "com.dayslite.countdown"
        minSdk 26
        targetSdk 36
        versionCode 8
        versionName "1.0.8"
    }
}
```

- [ ] **Step 3: Verify the policy configuration is present**

Run:

```bash
rg -n 'release\(36\)|minorApiLevel = 1|targetSdk 36|versionCode 8|versionName "1.0.8"' app/build.gradle
```

Expected: one match for each configured value.

- [ ] **Step 4: Compile the debug variant**

Run:

```bash
./gradlew compileDebugJavaWithJavac
```

Expected: `BUILD SUCCESSFUL` with Android SDK platform `android-36.1` selected.

### Task 2: Preserve custom back navigation on Android 16

**Files:**
- Modify: `app/src/main/java/com/dayslite/countdown/MainActivity.java:1-40,374-381`

**Interfaces:**
- Consumes: `Screen.HOME`, `Screen.EDITOR`, and Android `OnBackInvokedDispatcher` on API 33 and newer.
- Produces: `handleBackNavigation()` as the single path that finishes the home activity or returns the editor to the home screen.

- [ ] **Step 1: Record the Android 16 incompatibility before editing**

Run:

```bash
rg -n 'onBackPressed|OnBackInvokedDispatcher|handleBackNavigation' app/src/main/java/com/dayslite/countdown/MainActivity.java
```

Expected: `onBackPressed` is present while `OnBackInvokedDispatcher` and `handleBackNavigation` are absent.

- [ ] **Step 2: Register the Android 16-compatible back callback**

Add this import:

```java
import android.window.OnBackInvokedDispatcher;
```

Add this registration after `super.onCreate(savedInstanceState)`:

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
            OnBackInvokedDispatcher.PRIORITY_DEFAULT,
            this::handleBackNavigation);
}
```

Add `import android.os.Build;` if it is not already present.

- [ ] **Step 3: Route legacy and callback navigation through one method**

Replace the existing method body with:

```java
@Override
@SuppressWarnings("deprecation")
public void onBackPressed() {
    handleBackNavigation();
}

private void handleBackNavigation() {
    if (currentScreen == Screen.HOME) {
        finish();
        return;
    }
    showHome();
}
```

- [ ] **Step 4: Compile after the compatibility change**

Run:

```bash
./gradlew compileDebugJavaWithJavac
```

Expected: `BUILD SUCCESSFUL` without missing API or Java compilation errors.

### Task 3: Verify the release candidate

**Files:**
- Verify: `app/build.gradle`
- Verify: `app/src/main/java/com/dayslite/countdown/MainActivity.java`
- Verify: `app/build/outputs/apk/debug/app-debug.apk`
- Verify: `app/build/outputs/bundle/release/app-release.aab`

**Interfaces:**
- Consumes: the Android 16 build configuration and compatible back callback from Tasks 1 and 2.
- Produces: test, Lint, APK, and AAB evidence for the release handoff.

- [ ] **Step 1: Run unit tests and Android Lint**

Run:

```bash
./gradlew testDebugUnitTest lintDebug
```

Expected: `BUILD SUCCESSFUL`; no release-blocking Lint error.

- [ ] **Step 2: Build debug and release artifacts**

Run:

```bash
./gradlew assembleDebug bundleRelease
```

Expected: `BUILD SUCCESSFUL`, a debug APK, and a release AAB. If signing credentials are unavailable, run `./gradlew assembleDebug` and report the signing prerequisite instead of claiming the AAB is ready.

- [ ] **Step 3: Verify generated artifact metadata**

Run:

```bash
/Users/dong.xu/Library/Android/sdk/build-tools/36.1.0/aapt2 dump badging app/build/outputs/apk/debug/app-debug.apk
```

Expected metadata includes package `com.dayslite.countdown`, `versionCode='8'`, `versionName='1.0.8'`, and `targetSdkVersion:'36'`.

- [ ] **Step 4: Review the final diff**

Run:

```bash
git diff --check
git diff -- app/build.gradle app/src/main/java/com/dayslite/countdown/MainActivity.java
```

Expected: no whitespace errors and only the scoped Android 16 upgrade changes.

- [ ] **Step 5: Commit the implementation**

```bash
git add app/build.gradle app/src/main/java/com/dayslite/countdown/MainActivity.java docs/superpowers/plans/2026-07-21-android-16-target-upgrade.md
git commit -m "build: target Android 16"
```
