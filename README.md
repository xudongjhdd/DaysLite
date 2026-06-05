# DaysLite

DaysLite is a lightweight Android countdown app for important dates. It keeps events on the device, requires no account, and avoids ads or analytics in the first version.

## Features

- Create countdown events with a name, target date, color, and optional note
- Start new countdowns with tomorrow as the default target date
- View days left, today, and days passed
- Edit or delete countdowns
- Store countdown data locally with SharedPreferences
- Switch the in-app language between English and Chinese
- In-app privacy policy page
- No login, ads, analytics, or sensitive permissions

## Package

```text
com.dayslite.countdown
```

## Build

```bash
./gradlew assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Project Structure

```text
app/
  src/main/
    AndroidManifest.xml
    java/com/dayslite/countdown/
      MainActivity.java
      CountdownEvent.java
      CountdownStore.java
      CountdownCalculator.java
      UiKit.java
    res/
      drawable/
      mipmap-anydpi-v26/
      values/
      xml/
docs/
  play-store-listing.md
  privacy-policy.md
  screenshots.md
```

## Privacy

Countdown events are stored locally on the user's device. DaysLite does not collect, transmit, sell, or share personal data in the current version.
