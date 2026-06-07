#!/bin/sh

set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
VERSION="1.0.0"
OUTPUT_DIR="$ROOT_DIR/dist/DaysLite-$VERSION-play-upload"
AAB="$ROOT_DIR/app/build/outputs/bundle/release/app-release.aab"

if [ ! -f "$AAB" ]; then
  echo "Release AAB not found. Run ./gradlew bundleRelease first." >&2
  exit 1
fi

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR/app" "$OUTPUT_DIR/screenshots/en-US" "$OUTPUT_DIR/screenshots/zh-CN" "$OUTPUT_DIR/docs"

cp "$AAB" "$OUTPUT_DIR/app/DaysLite-$VERSION.aab"
cp "$ROOT_DIR"/docs/store-assets/screenshots/en-US/phone/*.jpg "$OUTPUT_DIR/screenshots/en-US/"
cp "$ROOT_DIR"/docs/store-assets/screenshots/zh-CN/phone/*.jpg "$OUTPUT_DIR/screenshots/zh-CN/"
cp "$ROOT_DIR/docs/play-store-listing.md" "$OUTPUT_DIR/docs/"
cp "$ROOT_DIR/docs/play-console-answers.md" "$OUTPUT_DIR/docs/"
cp "$ROOT_DIR/docs/play-console-checklist.md" "$OUTPUT_DIR/docs/"
cp "$ROOT_DIR/docs/play-upload-manifest.md" "$OUTPUT_DIR/docs/"
cp "$ROOT_DIR/docs/closed-testing.md" "$OUTPUT_DIR/docs/"
cp "$ROOT_DIR/docs/privacy-policy.md" "$OUTPUT_DIR/docs/"

(
  cd "$OUTPUT_DIR"
  find . -type f ! -name SHA256SUMS.txt -print | sort | xargs shasum -a 256 > SHA256SUMS.txt
)

echo "Prepared: $OUTPUT_DIR"
