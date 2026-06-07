# DaysLite Google Play Console Answers

Prepared for DaysLite `1.0.0` (`versionCode 1`) on June 7, 2026.

Use this document when the developer identity verification is complete. Google may adjust wording or page order, but the answers below reflect the current DaysLite release.

## Verified App Behavior

- Package: `com.dayslite.countdown`
- Category: Productivity
- No account creation or login
- No advertisements
- No in-app purchases or subscriptions
- No third-party SDKs
- No analytics or crash-reporting SDKs
- No network permission
- No sensitive or runtime permissions
- No cloud backup or device-transfer backup
- Countdown titles, dates, notes, colors, repeat settings, and language preference stay in local app storage
- Users can delete individual countdowns in the app
- Users can delete all local data by clearing app storage or uninstalling the app

Recheck these answers whenever the app adds a permission, SDK, network feature, account, backup, advertising, analytics, notification, or cloud-sync feature.

## Privacy Policy

Play Console path:

```text
Policy and programs > App content > Privacy policy
```

Answer:

```text
https://xudongjhdd.github.io/DaysLite/privacy-policy.html
```

The policy is also available inside the app.

## Ads

Play Console path:

```text
Policy and programs > App content > Ads
```

Question:

```text
Does your app contain ads?
```

Answer:

```text
No, my app does not contain ads.
```

## App Access

Play Console path:

```text
Policy and programs > App content > App access
```

Answer:

```text
All functionality is available without special access.
```

Do not provide login credentials or special instructions. DaysLite has no restricted pages, membership, location restriction, or account requirement.

## Data Safety

Play Console path:

```text
Policy and programs > App content > Data safety
```

### Data Collection and Security

Question:

```text
Does your app collect or share any of the required user data types?
```

Answer:

```text
No
```

No data types should be selected. User-created countdown data is processed and stored only on the user's device and is not transmitted to the developer or another organization.

If Play Console asks whether the app provides a way for users to request deletion:

```text
No account is created and no user data is collected by the developer.
Users can delete countdowns in the app or delete all local data by clearing app storage or uninstalling DaysLite.
```

If the form skips encryption-in-transit questions after selecting `No`, leave them skipped. DaysLite does not transmit user data.

Expected store listing summary:

```text
No data collected
No data shared with third parties
```

## Account Deletion

DaysLite does not allow users to create an account.

If asked:

```text
Does your app allow users to create an account?
```

Answer:

```text
No
```

An account-deletion web URL is therefore not required for this release.

## Target Audience and Content

Play Console path:

```text
Policy and programs > App content > Target audience and content
```

Recommended target age groups:

```text
13-15
16-17
18 and over
```

Answer that the app is not designed primarily for children. Do not select age groups under 13.

Reason:

```text
DaysLite is a general productivity utility for managing personal dates. It does not use child-directed characters, games, educational content, social features, or child-focused marketing.
```

Store listing appeal-to-children question:

```text
No
```

## Content Rating

Play Console path:

```text
Policy and programs > App content > Content rating
```

App category in the questionnaire:

```text
Utility, productivity, communication, or other non-game app
```

Contact email:

```text
99799543@qq.com
```

Use these answers for content questions:

- Violence: No
- Fear or horror: No
- Sexual content or nudity: No
- Profanity or crude humor: No
- Controlled substances, alcohol, or tobacco: No
- Gambling or simulated gambling: No
- User-generated content: No
- Users communicating with each other: No
- Location sharing: No
- Digital purchases: No
- In-app purchases: No
- Advertising: No
- Unrestricted web access or web browsing: No
- External promotional content: No

Expected result:

```text
Suitable for all ages / Everyone
```

The final rating is assigned by regional rating authorities based on the submitted questionnaire.

## Financial Features

Play Console path:

```text
Policy and programs > App content > Financial features
```

Answer:

```text
My app doesn't provide any financial features.
```

Do not select banking, loans, payments, wallets, rewards, trading, cryptocurrency, insurance, financial advice, or other financial services.

## Health Apps

Play Console path:

```text
Policy and programs > App content > Health apps
```

Answer:

```text
My app doesn't provide any health features.
```

DaysLite is a general date countdown utility. It does not track health, fitness, medication, sleep, periods, mental health, or medical information.

## Government Apps

If Play Console asks whether DaysLite is a government app:

```text
No
```

DaysLite is not developed by, for, or on behalf of a government organization.

## News and Magazine Declaration

DaysLite is in the Productivity category and does not provide news or magazine content.

If this declaration appears:

```text
DaysLite is not a news or magazine app.
```

## Store Settings

- App name: `DaysLite`
- App type: App
- Category: Productivity
- Free or paid: Free
- In-app products: None
- Default language: English (United States)
- Additional language: Chinese (Simplified)
- Contact email: `99799543@qq.com`
- Privacy policy: `https://xudongjhdd.github.io/DaysLite/privacy-policy.html`

## Review Notes

If Play Console provides an optional notes field:

```text
DaysLite is a lightweight offline countdown app. It requires no account and no special access. All countdown information is stored locally on the device. The app contains no ads, analytics, third-party SDKs, or sensitive permissions.
```

## Before Submission

- Upload `app/build/outputs/bundle/release/app-release.aab`
- Confirm version is `1.0.0` and version code is `1`
- Upload at least four English phone screenshots
- Upload the Simplified Chinese store listing and screenshots
- Confirm the privacy policy URL opens without login
- Recheck the final AAB for new permissions or SDKs
- Save each App content declaration
- Review the Data Safety preview before submitting

## Official References

- Data Safety:
  `https://support.google.com/googleplay/android-developer/answer/10787469`
- App content and review preparation:
  `https://support.google.com/googleplay/android-developer/answer/9859455`
- User Data and privacy policy:
  `https://support.google.com/googleplay/android-developer/answer/10144311`
- Account deletion:
  `https://support.google.com/googleplay/android-developer/answer/13327111`
- Target audience:
  `https://support.google.com/googleplay/android-developer/answer/9867159`
- Content ratings:
  `https://support.google.com/googleplay/android-developer/answer/9859655`
- Financial features:
  `https://support.google.com/googleplay/android-developer/answer/13849271`
- Health apps:
  `https://support.google.com/googleplay/android-developer/answer/14738291`
