# App Store Screenshot Plan

The PNG files under `development/` are simulator evidence, not final App Store assets. They are currently 1206×2622 from an iPhone 17 Pro simulator and must not be uploaded unless App Store Connect explicitly accepts that size at submission time.

## Final capture standard

- Device class: 6.9-inch iPhone display.
- Recommended source simulator: iPhone 16 Pro Max, portrait.
- Final pixel size: 1320×2868 PNG, no alpha.
- Appearance: Light, standard text size, fully charged/default status bar where practical.
- Languages: English (U.S.) and Simplified Chinese (China).
- Content: use realistic sample events; never include personal, account, payment, or private information.
- Quantity: four screenshots per language. App Store Connect supports 1–10 screenshots per device size.

Capture status: complete on 2026-08-06. The final `en-US/` and `zh-Hans/` files are 1320×2868 PNG screenshots from an iPhone 17 Pro Max simulator and were visually inspected at full size.

Apple may change accepted devices and dimensions. Verify the current [official screenshot specifications](https://developer.apple.com/help/app-store-connect/reference/app-information/screenshot-specifications/) immediately before capture.

## Capture matrix

| Order | Language | Scenario | Source simulator | Final filename |
| --- | --- | --- | --- | --- |
| 01 | English | Populated home with upcoming, yearly, and past events | iPhone 16 Pro Max | `en-US/01-home.png` |
| 02 | English | New-event editor showing date, colors, note, and yearly repeat | iPhone 16 Pro Max | `en-US/02-editor.png` |
| 03 | English | Settings with language and privacy entry visible | iPhone 16 Pro Max | `en-US/03-settings.png` |
| 04 | English | Privacy page demonstrating local-only behavior | iPhone 16 Pro Max | `en-US/04-privacy.png` |
| 01 | 简体中文 | 含即将到来、每年重复和已过期事件的首页 | iPhone 16 Pro Max | `zh-Hans/01-home.png` |
| 02 | 简体中文 | 展示日期、颜色、备注和每年重复的新建页 | iPhone 16 Pro Max | `zh-Hans/02-editor.png` |
| 03 | 简体中文 | 展示语言与隐私入口的设置页 | iPhone 16 Pro Max | `zh-Hans/03-settings.png` |
| 04 | 简体中文 | 展示仅本地存储说明的隐私页 | iPhone 16 Pro Max | `zh-Hans/04-privacy.png` |

## Capture procedure

1. Install the exact Release candidate build on the selected simulator.
2. Reset the app, set the target language, and create the same non-personal sample events in both languages.
3. Capture with `xcrun simctl io <UDID> screenshot <filename>.png`.
4. Verify dimensions with `sips -g pixelWidth -g pixelHeight <filename>.png`.
5. Inspect every image at full size for clipped text, keyboard overlays, debug labels, sensitive data, and inconsistent content.
6. Upload the highest required device size and inspect App Store Connect's generated previews before submitting.

Apple upload instructions: [Upload app previews and screenshots](https://developer.apple.com/help/app-store-connect/manage-app-information/upload-app-previews-and-screenshots/).
