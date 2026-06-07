# DaysLite Play Upload Manifest

Release candidate:

- App: DaysLite
- Package: `com.dayslite.countdown`
- Version: `1.0.0`
- Version code: `1`
- Target SDK: `35`
- AAB: `app/build/outputs/bundle/release/app-release.aab`
- AAB size: approximately 23 KB
- AAB SHA-256: generated in `dist/DaysLite-1.0.0-play-upload/SHA256SUMS.txt`
- Signing certificate expiry: October 21, 2053

## Upload Assets

English screenshots:

```text
docs/store-assets/screenshots/en-US/phone/
```

Simplified Chinese screenshots:

```text
docs/store-assets/screenshots/zh-CN/phone/
```

Store text:

```text
docs/play-store-listing.md
```

Play Console declarations:

```text
docs/play-console-answers.md
```

Privacy policy:

```text
https://xudongjhdd.github.io/DaysLite/privacy-policy.html
```

Release and testing documents:

```text
docs/release-build.md
docs/closed-testing.md
docs/testing/
```

## Generate Local Upload Package

Run:

```bash
./scripts/prepare-play-upload.sh
```

Output:

```text
dist/DaysLite-1.0.0-play-upload/
```

The `dist/` directory is ignored by Git because it contains a copy of the signed AAB.
