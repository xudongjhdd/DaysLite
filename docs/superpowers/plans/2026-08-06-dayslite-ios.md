# DaysLite iOS Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build, test, sign, distribute through TestFlight, and publish a native iOS version of DaysLite while recording a reusable App Store publishing workflow.

**Architecture:** Add an independent SwiftUI application under `ios/DaysLite` without changing the Android build. The app uses a small observable application model, pure date-calculation types, Codable JSON persisted in app-scoped UserDefaults, manual English/Chinese copy, and no third-party runtime dependencies.

**Tech Stack:** Xcode 26.5, Swift 6, SwiftUI, Observation, Foundation, XCTest, iOS 17 minimum deployment target, App Store Connect, TestFlight.

## Global Constraints

- Keep all iOS source and iOS-specific documentation under `ios/DaysLite`.
- Do not change the Android application's behavior, package, build, or release process.
- Minimum deployment target is iOS 17.0; submission builds use Xcode 26 or later and the iOS 26 SDK or later.
- Bundle ID starts as `com.dayslite.countdown.ios` and must be checked for availability before creating the App Store Connect record.
- App Store display name is `DaysLite`; version starts at `1.0.0` with build number `1`.
- Use only Apple frameworks in the shipped app; add no ads, analytics, tracking, login, cloud sync, notifications, or sensitive permissions.
- Store countdowns only on the device. Do not migrate or synchronize Android data.
- Support English and Simplified Chinese through the in-app language setting.
- Every task updates `ios/DaysLite/docs/progress.md` with status, evidence, blockers, and next action.
- Never put an Apple Account password, verification code, payment details, certificate private key, API key, or identity-document number in Git.

---

## File Map

The implementation creates the following focused files:

```text
ios/DaysLite/
  DaysLite.xcodeproj/project.pbxproj
  DaysLite/
    App/DaysLiteApp.swift
    App/AppModel.swift
    Models/LocalDate.swift
    Models/CountdownEvent.swift
    Models/CountdownDraft.swift
    Services/CountdownCalculator.swift
    Services/CountdownStore.swift
    Services/AppLanguage.swift
    Services/AppText.swift
    Views/RootView.swift
    Views/HomeView.swift
    Views/CountdownCardView.swift
    Views/CountdownEditorView.swift
    Views/SettingsView.swift
    Views/PrivacyPolicyView.swift
    Views/Theme.swift
    Resources/Assets.xcassets/Contents.json
    Resources/Assets.xcassets/AccentColor.colorset/Contents.json
    Resources/Assets.xcassets/AppIcon.appiconset/Contents.json
    Resources/Assets.xcassets/AppIcon.appiconset/AppIcon-1024.png
    Resources/PrivacyInfo.xcprivacy
  DaysLiteTests/
    LocalDateTests.swift
    CountdownCalculatorTests.swift
    CountdownStoreTests.swift
    AppModelTests.swift
    AppTextTests.swift
  StoreAssets/
    app-store-listing.md
    review-notes.md
    screenshots/README.md
  docs/
    progress.md
    publishing-playbook.md
    app-store-checklist.md
  scripts/generate-app-icon.swift
```

`LocalDate` owns calendar-day semantics. `CountdownCalculator` is pure and owns recurrence calculations. `CountdownStore` owns persistence. `AppModel` owns mutations and presentation-ready collections. Views consume these interfaces and contain no persistence logic.

---

### Task 1: Scaffold a Buildable Native iOS Project

**Files:**
- Create: `ios/DaysLite/DaysLite.xcodeproj/project.pbxproj`
- Create: `ios/DaysLite/DaysLite/App/DaysLiteApp.swift`
- Create: `ios/DaysLite/DaysLite/Views/RootView.swift`
- Create: `ios/DaysLite/DaysLite/Views/Theme.swift`
- Create: `ios/DaysLite/DaysLite/Resources/Assets.xcassets/Contents.json`
- Create: `ios/DaysLite/DaysLite/Resources/Assets.xcassets/AccentColor.colorset/Contents.json`
- Create: `ios/DaysLite/DaysLiteTests/ProjectSmokeTests.swift`
- Modify: `ios/DaysLite/docs/progress.md`

**Interfaces:**
- Produces: Xcode scheme `DaysLite`, application target `DaysLite`, unit-test target `DaysLiteTests`, `DaysLiteApp`, `RootView`, and shared `Theme` constants.
- Consumes: no implementation interfaces.

- [ ] **Step 1: Create the Xcode project graph**

Create a manually maintained Xcode project with file-system-synchronized groups for `DaysLite` and `DaysLiteTests`. Configure these exact application build settings in Debug and Release:

```text
PRODUCT_BUNDLE_IDENTIFIER = com.dayslite.countdown.ios
PRODUCT_NAME = DaysLite
MARKETING_VERSION = 1.0.0
CURRENT_PROJECT_VERSION = 1
IPHONEOS_DEPLOYMENT_TARGET = 17.0
SDKROOT = iphoneos
SUPPORTED_PLATFORMS = "iphoneos iphonesimulator"
TARGETED_DEVICE_FAMILY = 1
SWIFT_VERSION = 6.0
CODE_SIGN_STYLE = Automatic
GENERATE_INFOPLIST_FILE = YES
INFOPLIST_KEY_CFBundleDisplayName = DaysLite
INFOPLIST_KEY_UIApplicationSceneManifest_Generation = YES
INFOPLIST_KEY_UIApplicationSupportsIndirectInputEvents = YES
INFOPLIST_KEY_UILaunchScreen_Generation = YES
ASSETCATALOG_COMPILER_APPICON_NAME = AppIcon
ASSETCATALOG_COMPILER_GLOBAL_ACCENT_COLOR_NAME = AccentColor
```

Configure the test target with `TEST_HOST = $(BUILT_PRODUCTS_DIR)/DaysLite.app/$(BUNDLE_EXECUTABLE_FOLDER_PATH)/DaysLite` and `BUNDLE_LOADER = $(TEST_HOST)`. Create a shared scheme so command-line builds do not depend on user-specific Xcode files.

- [ ] **Step 2: Write a smoke test before the app entry exists**

```swift
import XCTest
@testable import DaysLite

final class ProjectSmokeTests: XCTestCase {
    func testProductNameIsDaysLite() {
        XCTAssertEqual(Bundle.main.object(forInfoDictionaryKey: "CFBundleDisplayName") as? String, "DaysLite")
    }
}
```

- [ ] **Step 3: Confirm the expected initial build failure**

Run:

```bash
xcodebuild -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'generic/platform=iOS Simulator' build
```

Expected: FAIL because `DaysLiteApp` and the initial source tree do not yet exist.

- [ ] **Step 4: Add the minimal SwiftUI entry and theme**

```swift
import SwiftUI

@main
struct DaysLiteApp: App {
    var body: some Scene {
        WindowGroup { RootView() }
    }
}
```

```swift
import SwiftUI

struct RootView: View {
    var body: some View {
        Text("DaysLite")
            .accessibilityIdentifier("root.dayslite")
    }
}
```

```swift
import SwiftUI

enum Theme {
    static let background = Color(red: 248 / 255, green: 250 / 255, blue: 252 / 255)
    static let textPrimary = Color(red: 15 / 255, green: 23 / 255, blue: 42 / 255)
    static let textSecondary = Color(red: 100 / 255, green: 116 / 255, blue: 139 / 255)
    static let primary = Color(red: 37 / 255, green: 99 / 255, blue: 235 / 255)
}
```

- [ ] **Step 5: Build and inspect the project**

Run:

```bash
xcodebuild -project ios/DaysLite/DaysLite.xcodeproj -list
xcodebuild -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'generic/platform=iOS Simulator' build
```

Expected: scheme listing contains `DaysLite`; build ends with `** BUILD SUCCEEDED **`.

- [ ] **Step 6: Update progress and commit**

Record the build command and result, then commit:

```bash
git add ios/DaysLite
git commit -m "build: scaffold DaysLite iOS project"
```

---

### Task 2: Implement Local Dates, Events, and Countdown Calculation

**Files:**
- Create: `ios/DaysLite/DaysLite/Models/LocalDate.swift`
- Create: `ios/DaysLite/DaysLite/Models/CountdownEvent.swift`
- Create: `ios/DaysLite/DaysLite/Services/CountdownCalculator.swift`
- Create: `ios/DaysLite/DaysLiteTests/LocalDateTests.swift`
- Create: `ios/DaysLite/DaysLiteTests/CountdownCalculatorTests.swift`
- Delete: `ios/DaysLite/DaysLiteTests/ProjectSmokeTests.swift`
- Modify: `ios/DaysLite/docs/progress.md`

**Interfaces:**
- Produces: `LocalDate`, `CountdownEvent`, `CountdownStatus`, and `CountdownCalculator`.
- Consumes: only Foundation calendar APIs.

- [ ] **Step 1: Write failing local-date tests**

```swift
import XCTest
@testable import DaysLite

final class LocalDateTests: XCTestCase {
    func testComparableUsesYearMonthAndDay() {
        XCTAssertLessThan(LocalDate(year: 2026, month: 1, day: 31), LocalDate(year: 2026, month: 2, day: 1))
    }

    func testNonLeapFebruary29FallsBackToFebruary28() {
        let leapDay = LocalDate(year: 2024, month: 2, day: 29)
        XCTAssertEqual(leapDay.withYear(2025), LocalDate(year: 2025, month: 2, day: 28))
    }
}
```

- [ ] **Step 2: Write failing calculator tests**

```swift
import XCTest
@testable import DaysLite

final class CountdownCalculatorTests: XCTestCase {
    private let calculator = CountdownCalculator(calendar: Calendar(identifier: .gregorian))
    private let today = LocalDate(year: 2026, month: 8, day: 6)

    func testFutureTodayAndPastStatuses() {
        XCTAssertEqual(calculator.status(for: event(date: .init(year: 2026, month: 8, day: 7)), today: today), .future(1))
        XCTAssertEqual(calculator.status(for: event(date: today), today: today), .today)
        XCTAssertEqual(calculator.status(for: event(date: .init(year: 2026, month: 8, day: 5)), today: today), .past(1))
    }

    func testYearlyEventMovesToNextOccurrence() {
        let yearly = event(date: .init(year: 2020, month: 8, day: 5), repeatYearly: true)
        XCTAssertEqual(calculator.displayDate(for: yearly, today: today), .init(year: 2027, month: 8, day: 5))
    }

    func testYearlyLeapDayUsesFebruary28InNonLeapYear() {
        let yearly = event(date: .init(year: 2024, month: 2, day: 29), repeatYearly: true)
        XCTAssertEqual(calculator.displayDate(for: yearly, today: .init(year: 2025, month: 1, day: 1)), .init(year: 2025, month: 2, day: 28))
    }

    private func event(date: LocalDate, repeatYearly: Bool = false) -> CountdownEvent {
        CountdownEvent(id: UUID(), title: "Event", targetDate: date, note: "", colorHex: "#2563EB", repeatYearly: repeatYearly, createdAt: .distantPast, updatedAt: .distantPast)
    }
}
```

- [ ] **Step 3: Run tests and verify type-not-found failures**

Run:

```bash
xcodebuild test -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'platform=iOS Simulator,name=iPhone 16 Pro,OS=latest' -only-testing:DaysLiteTests/LocalDateTests -only-testing:DaysLiteTests/CountdownCalculatorTests
```

Expected: FAIL because the date, event, and calculator types do not exist.

- [ ] **Step 4: Implement the pure model interfaces**

```swift
struct LocalDate: Codable, Hashable, Comparable, Sendable {
    let year: Int
    let month: Int
    let day: Int

    init(year: Int, month: Int, day: Int) {
        self.year = year
        self.month = month
        self.day = day
    }

    static func < (lhs: Self, rhs: Self) -> Bool {
        (lhs.year, lhs.month, lhs.day) < (rhs.year, rhs.month, rhs.day)
    }

    init(_ date: Date, calendar: Calendar = .autoupdatingCurrent) {
        let components = calendar.dateComponents([.year, .month, .day], from: date)
        self.init(year: components.year!, month: components.month!, day: components.day!)
    }

    func date(in calendar: Calendar = .autoupdatingCurrent) -> Date? {
        calendar.date(from: DateComponents(year: year, month: month, day: day))
    }

    func adding(days: Int, calendar: Calendar = .autoupdatingCurrent) -> LocalDate? {
        guard let date = date(in: calendar), let result = calendar.date(byAdding: .day, value: days, to: date) else { return nil }
        return LocalDate(result, calendar: calendar)
    }

    func withYear(_ year: Int, calendar: Calendar = Calendar(identifier: .gregorian)) -> LocalDate {
        let requested = DateComponents(year: year, month: month, day: day)
        if let date = calendar.date(from: requested) {
            let result = calendar.dateComponents([.year, .month, .day], from: date)
            if result.year == year, result.month == month, result.day == day {
                return LocalDate(year: year, month: month, day: day)
            }
        }
        return LocalDate(year: year, month: month, day: 28)
    }
}
```

```swift
struct CountdownEvent: Identifiable, Codable, Equatable, Sendable {
    let id: UUID
    var title: String
    var targetDate: LocalDate
    var note: String
    var colorHex: String
    var repeatYearly: Bool
    let createdAt: Date
    var updatedAt: Date
}

enum CountdownStatus: Equatable {
    case today
    case future(Int)
    case past(Int)
}
```

Implement `CountdownCalculator` with these exact signatures:

```swift
struct CountdownCalculator: Sendable {
    let calendar: Calendar
    func displayDate(for event: CountdownEvent, today: LocalDate) -> LocalDate
    func status(for event: CountdownEvent, today: LocalDate) -> CountdownStatus
    func isPast(_ event: CountdownEvent, today: LocalDate) -> Bool
}
```

Use `calendar.dateComponents([.day], from:to:)` after converting both `LocalDate` values to local start-of-day dates. Do not divide seconds by 86,400.

- [ ] **Step 5: Run model tests**

Run the Task 2 test command again.

Expected: all `LocalDateTests` and `CountdownCalculatorTests` pass.

- [ ] **Step 6: Update progress and commit**

```bash
git add ios/DaysLite
git commit -m "feat: add iOS countdown domain model"
```

---

### Task 3: Implement Loss-Tolerant Local Persistence and App State

**Files:**
- Create: `ios/DaysLite/DaysLite/Services/AppLanguage.swift`
- Create: `ios/DaysLite/DaysLite/Services/CountdownStore.swift`
- Create: `ios/DaysLite/DaysLite/App/AppModel.swift`
- Create: `ios/DaysLite/DaysLiteTests/CountdownStoreTests.swift`
- Create: `ios/DaysLite/DaysLiteTests/AppModelTests.swift`
- Modify: `ios/DaysLite/DaysLite/App/DaysLiteApp.swift`
- Modify: `ios/DaysLite/docs/progress.md`

**Interfaces:**
- Produces: `AppLanguage`, `KeyValueStoring`, `CountdownStore`, `StoreError`, and `AppModel` mutation methods.
- Consumes: `LocalDate`, `CountdownEvent`, and `CountdownCalculator` from Task 2.

- [ ] **Step 1: Write failing persistence tests**

```swift
import XCTest
@testable import DaysLite

final class CountdownStoreTests: XCTestCase {
    func testLoadEventsSkipsOnlyCorruptEntries() throws {
        let defaults = try XCTUnwrap(UserDefaults(suiteName: #function))
        defaults.removePersistentDomain(forName: #function)
        defaults.set(Data(#"[{"id":"00000000-0000-0000-0000-000000000001","title":"One","targetDate":{"year":2026,"month":1,"day":1},"note":"","colorHex":"#2563EB","repeatYearly":false,"createdAt":0,"updatedAt":0},{"title":"Broken"}]"#.utf8), forKey: "events")

        let store = CountdownStore(defaults: defaults)
        XCTAssertEqual(store.loadEvents().map(\.title), ["One"])
    }

    func testLanguageDefaultsToEnglishAndPersistsChinese() throws {
        let defaults = try XCTUnwrap(UserDefaults(suiteName: #function))
        defaults.removePersistentDomain(forName: #function)
        let store = CountdownStore(defaults: defaults)
        XCTAssertEqual(store.loadLanguage(), .english)
        store.saveLanguage(.chinese)
        XCTAssertEqual(store.loadLanguage(), .chinese)
    }
}
```

- [ ] **Step 2: Write failing app-model mutation tests**

```swift
@MainActor
final class AppModelTests: XCTestCase {
    func testAddUpdateAndDeletePersistImmediately() throws {
        let defaults = try XCTUnwrap(UserDefaults(suiteName: #function))
        defaults.removePersistentDomain(forName: #function)
        let model = AppModel(store: CountdownStore(defaults: defaults), now: { Date(timeIntervalSince1970: 10) })
        let event = model.add(title: "Trip", targetDate: .init(year: 2026, month: 9, day: 1), note: "", colorHex: "#2563EB", repeatYearly: false)
        XCTAssertEqual(CountdownStore(defaults: defaults).loadEvents().count, 1)
        model.delete(id: event.id)
        XCTAssertTrue(CountdownStore(defaults: defaults).loadEvents().isEmpty)
    }
}
```

- [ ] **Step 3: Run and observe missing-type failures**

Run:

```bash
xcodebuild test -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'platform=iOS Simulator,name=iPhone 16 Pro,OS=latest' -only-testing:DaysLiteTests/CountdownStoreTests -only-testing:DaysLiteTests/AppModelTests
```

Expected: FAIL because persistence and app-state types do not exist.

- [ ] **Step 4: Implement persistence**

```swift
enum AppLanguage: String, Codable, CaseIterable, Sendable {
    case english = "en"
    case chinese = "zh-Hans"
    mutating func toggle() { self = self == .english ? .chinese : .english }
}

enum StoreError: Error, Equatable {
    case encodingFailed
    case writeVerificationFailed
}
```

Implement `CountdownStore` with:

```swift
struct CountdownStore {
    init(defaults: UserDefaults = .standard)
    func loadEvents() -> [CountdownEvent]
    @discardableResult func saveEvents(_ events: [CountdownEvent]) -> Result<Void, StoreError>
    func loadLanguage() -> AppLanguage
    func saveLanguage(_ language: AppLanguage)
}
```

Encode the event array as JSON. For loading, parse the outer array with `JSONSerialization`, re-encode each dictionary, and decode each event independently with `JSONDecoder`; use `compactMap` so one corrupt event cannot discard valid neighbors. After `UserDefaults.set`, compare the immediately read data with the encoded data and return `.writeVerificationFailed` if they differ.

- [ ] **Step 5: Implement observable app state**

Create `@MainActor @Observable final class AppModel` with:

```swift
init(
    store: CountdownStore = CountdownStore(),
    calculator: CountdownCalculator = CountdownCalculator(calendar: .autoupdatingCurrent),
    calendar: Calendar = .autoupdatingCurrent,
    now: @escaping () -> Date = Date.init
)
private(set) var events: [CountdownEvent]
var language: AppLanguage
var storeError: StoreError?
func add(title: String, targetDate: LocalDate, note: String, colorHex: String, repeatYearly: Bool) -> CountdownEvent
func update(id: UUID, title: String, targetDate: LocalDate, note: String, colorHex: String, repeatYearly: Bool)
func delete(id: UUID)
func toggleLanguage()
func sortedEvents(today: LocalDate) -> [CountdownEvent]
func upcomingCount(today: LocalDate) -> Int
```

Inject `now: () -> Date` for deterministic tests. Every mutation calls a private `persist()` and sets `storeError` if persistence returns failure. Initialize one `AppModel` in `DaysLiteApp` with `@State` and place it in the SwiftUI environment.

- [ ] **Step 6: Run persistence and app-state tests**

Run the Task 3 test command again.

Expected: all persistence and app-state tests pass.

- [ ] **Step 7: Update progress and commit**

```bash
git add ios/DaysLite
git commit -m "feat: persist iOS countdowns locally"
```

---

### Task 4: Add Complete Bilingual Copy and Formatting

**Files:**
- Create: `ios/DaysLite/DaysLite/Services/AppText.swift`
- Create: `ios/DaysLite/DaysLiteTests/AppTextTests.swift`
- Modify: `ios/DaysLite/docs/progress.md`

**Interfaces:**
- Produces: `AppText` labels, `daysLabel(for:)`, `dateLabel(_:)`, and `summary(upcoming:)`.
- Consumes: `AppLanguage`, `CountdownStatus`, and `LocalDate`.

- [ ] **Step 1: Write failing copy tests**

```swift
import XCTest
@testable import DaysLite

final class AppTextTests: XCTestCase {
    func testEnglishDayGrammar() {
        let text = AppText(language: .english)
        XCTAssertEqual(text.daysLabel(for: .future(1)), "1 day left")
        XCTAssertEqual(text.daysLabel(for: .future(2)), "2 days left")
        XCTAssertEqual(text.daysLabel(for: .past(1)), "1 day ago")
        XCTAssertEqual(text.daysLabel(for: .today), "Today")
    }

    func testChineseLabelsAndDate() {
        let text = AppText(language: .chinese)
        XCTAssertEqual(text.daysLabel(for: .future(3)), "还剩 3 天")
        XCTAssertEqual(text.dateLabel(.init(year: 2026, month: 8, day: 6)), "2026年8月6日")
    }
}
```

- [ ] **Step 2: Run and verify missing-type failure**

```bash
xcodebuild test -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'platform=iOS Simulator,name=iPhone 16 Pro,OS=latest' -only-testing:DaysLiteTests/AppTextTests
```

Expected: FAIL because `AppText` does not exist.

- [ ] **Step 3: Implement centralized copy**

Create an immutable `AppText` value that accepts `AppLanguage`. It must expose every visible string used by the Android app plus iOS persistence-error copy. Keep formatting functions deterministic:

```swift
struct AppText {
    let language: AppLanguage
    var settings: String { language == .chinese ? "设置" : "Settings" }
    var addCountdown: String { language == .chinese ? "添加倒计时" : "Add countdown" }
    var save: String { language == .chinese ? "保存" : "Save" }
    var cancel: String { language == .chinese ? "取消" : "Cancel" }
    func daysLabel(for status: CountdownStatus) -> String
    func dateLabel(_ date: LocalDate) -> String
    func summary(upcoming: Int) -> String
}
```

Include labels for empty state, event fields, yearly repeat, edit title, delete confirmation, privacy, local storage, language, version, required title, and save failure. Match the existing Android meaning; use natural iOS capitalization.

- [ ] **Step 4: Run copy tests and the full suite**

```bash
xcodebuild test -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'platform=iOS Simulator,name=iPhone 16 Pro,OS=latest'
```

Expected: all tests pass.

- [ ] **Step 5: Update progress and commit**

```bash
git add ios/DaysLite
git commit -m "feat: add bilingual iOS copy"
```

---

### Task 5: Build the Home Screen and Event Cards

**Files:**
- Create: `ios/DaysLite/DaysLite/Views/HomeView.swift`
- Create: `ios/DaysLite/DaysLite/Views/CountdownCardView.swift`
- Modify: `ios/DaysLite/DaysLite/Views/RootView.swift`
- Modify: `ios/DaysLite/DaysLite/Views/Theme.swift`
- Modify: `ios/DaysLite/DaysLiteTests/AppModelTests.swift`
- Modify: `ios/DaysLite/docs/progress.md`

**Interfaces:**
- Produces: `HomeView`, `CountdownCardView`, and navigation routes to editor and settings.
- Consumes: environment `AppModel`, `AppText`, `CountdownCalculator`, `Theme`, and event sorting from Task 3.

- [ ] **Step 1: Extend failing presentation-state tests**

Add tests proving `sortedEvents(today:)` sorts yearly events by their next occurrence and `upcomingCount(today:)` excludes non-repeating past events while including yearly events.

```swift
let titles = model.sortedEvents(today: .init(year: 2026, month: 8, day: 6)).map(\.title)
XCTAssertEqual(titles, ["Tomorrow", "Next Year"])
XCTAssertEqual(model.upcomingCount(today: .init(year: 2026, month: 8, day: 6)), 2)
```

- [ ] **Step 2: Run the new tests and verify failure**

Run `AppModelTests` only. Expected: FAIL until sorting and count semantics exactly match the design.

- [ ] **Step 3: Implement the home UI**

Build `HomeView` with `NavigationStack`, `ScrollView`, and `LazyVStack`. Use these stable accessibility identifiers for manual and future UI tests:

```text
home.title
home.summary
home.settings
home.empty
home.add
event.00000000-0000-0000-0000-000000000001
```

The runtime format is `event.` followed by the event UUID; for example, `event.00000000-0000-0000-0000-000000000001`.

Show title and summary at the top, a settings toolbar button, empty-state card when no events exist, sorted event cards otherwise, and a prominent add button. Cards display title, localized status, localized date, color indicator, yearly badge, and note. Non-repeating past events use muted colors.

- [ ] **Step 4: Wire navigation without persistence logic in views**

Define a private route enum in `RootView` or use sheet item state:

```swift
enum EditorSelection: Identifiable {
    case new
    case existing(UUID)
    var id: String {
        switch self {
        case .new: "new"
        case .existing(let id): id.uuidString
        }
    }
}
```

Home actions only set navigation state. They must not encode data or touch UserDefaults. Observe `AppModel.storeError` at the root and show the localized save-failure message in a system alert; dismissing the alert clears the model error.

- [ ] **Step 5: Build and run the full test suite**

```bash
xcodebuild -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'generic/platform=iOS Simulator' build
xcodebuild test -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'platform=iOS Simulator,name=iPhone 16 Pro,OS=latest'
```

Expected: build succeeds and all tests pass.

- [ ] **Step 6: Record simulator evidence and commit**

Capture a simulator screenshot of the empty home state and a populated home state under `ios/DaysLite/StoreAssets/screenshots/development/`, record paths in progress, then commit:

```bash
git add ios/DaysLite
git commit -m "feat: build DaysLite iOS home screen"
```

---

### Task 6: Build the Add and Edit Countdown Flow

**Files:**
- Create: `ios/DaysLite/DaysLite/Models/CountdownDraft.swift`
- Create: `ios/DaysLite/DaysLite/Views/CountdownEditorView.swift`
- Create: `ios/DaysLite/DaysLiteTests/CountdownDraftTests.swift`
- Modify: `ios/DaysLite/DaysLite/Views/RootView.swift`
- Modify: `ios/DaysLite/DaysLite/App/AppModel.swift`
- Modify: `ios/DaysLite/docs/progress.md`

**Interfaces:**
- Produces: `CountdownDraft`, `CountdownEditorView`, validation behavior, and delete confirmation.
- Consumes: `AppModel.add`, `AppModel.update`, `AppModel.delete`, `LocalDate`, `AppText`, and the six approved color hex values.

- [ ] **Step 1: Write failing draft tests**

```swift
import XCTest
@testable import DaysLite

final class CountdownDraftTests: XCTestCase {
    func testNewDraftDefaultsToTomorrowAndBlue() {
        let draft = CountdownDraft.new(today: .init(year: 2026, month: 8, day: 6), calendar: Calendar(identifier: .gregorian))
        XCTAssertEqual(draft.targetDate, .init(year: 2026, month: 8, day: 7))
        XCTAssertEqual(draft.colorHex, "#2563EB")
    }

    func testTrimmedEmptyTitleIsInvalid() {
        var draft = CountdownDraft.new(today: .init(year: 2026, month: 8, day: 6), calendar: Calendar(identifier: .gregorian))
        draft.title = "   "
        XCTAssertFalse(draft.isValid)
    }
}
```

- [ ] **Step 2: Run and verify missing-type failure**

Run `CountdownDraftTests`. Expected: FAIL because the draft type does not exist.

- [ ] **Step 3: Implement the draft model**

```swift
struct CountdownDraft: Equatable {
    var title: String
    var targetDate: LocalDate
    var note: String
    var colorHex: String
    var repeatYearly: Bool
    var isValid: Bool { !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty }
    static func new(today: LocalDate, calendar: Calendar) -> CountdownDraft
    init(event: CountdownEvent)
}
```

Use exactly these colors: `#2563EB`, `#10B981`, `#F97316`, `#EC4899`, `#7C3AED`, `#475569`.

- [ ] **Step 4: Implement the editor UI**

Use a SwiftUI `Form` with a title `TextField`, graphical or compact `DatePicker` bridged to `LocalDate`, horizontal color choices, multi-line note field, annual repeat `Toggle`, Save toolbar item, and Cancel action. New mode uses tomorrow; edit mode copies the selected event. Save trims title and note before calling `AppModel`.

Use accessibility identifiers:

```text
editor.title
editor.date
editor.color.2563EB
editor.note
editor.yearly
editor.save
editor.delete
```

The six concrete color identifiers are `editor.color.2563EB`, `editor.color.10B981`, `editor.color.F97316`, `editor.color.EC4899`, `editor.color.7C3AED`, and `editor.color.475569`.

If title is empty, show the localized validation message and retain focus. In edit mode, show a destructive Delete button and a system confirmation dialog.

- [ ] **Step 5: Build, test, and manually verify mutations**

Run the full test suite, then verify on a simulator: new event, edit every field, cancel without saving, delete cancel, delete confirm, and keyboard dismissal.

Expected: data changes only on Save or confirmed Delete and survive app relaunch.

- [ ] **Step 6: Update progress and commit**

```bash
git add ios/DaysLite
git commit -m "feat: add iOS countdown editor"
```

---

### Task 7: Add Settings, Privacy, Language Switching, and Accessibility

**Files:**
- Create: `ios/DaysLite/DaysLite/Views/SettingsView.swift`
- Create: `ios/DaysLite/DaysLite/Views/PrivacyPolicyView.swift`
- Create: `ios/DaysLite/DaysLite/Resources/privacy-policy.en.md`
- Create: `ios/DaysLite/DaysLite/Resources/privacy-policy.zh-Hans.md`
- Modify: `ios/DaysLite/DaysLite/Views/RootView.swift`
- Modify: `ios/DaysLite/DaysLite/Views/HomeView.swift`
- Modify: `ios/DaysLite/DaysLite/Views/CountdownCardView.swift`
- Modify: `ios/DaysLite/docs/progress.md`

**Interfaces:**
- Produces: Settings and in-app privacy screens; immediate language switching.
- Consumes: environment `AppModel`, `AppText`, bundle version metadata, and bundled Markdown privacy files.

- [ ] **Step 1: Add failing language-persistence coverage**

Extend `AppModelTests`:

```swift
model.toggleLanguage()
XCTAssertEqual(model.language, .chinese)
let reloaded = AppModel(store: CountdownStore(defaults: defaults))
XCTAssertEqual(reloaded.language, .chinese)
```

Run the test and confirm failure if the model does not yet persist language immediately.

- [ ] **Step 2: Implement settings and privacy views**

Settings displays privacy policy, local-storage explanation, language, and version. Language is a `Picker` with English and 简体中文 rather than a hidden one-way toggle. Version reads `CFBundleShortVersionString`.

Privacy loads the bundled language-specific Markdown and renders it with `Text(AttributedString(markdown:))` in a scroll view. If parsing fails, display the plain bundled string. The screen must not require network access.

- [ ] **Step 3: Update privacy text for both platforms**

State that countdown data is stored locally and can be deleted inside the app or by uninstalling it. Do not claim that an iOS user can clear app storage through Android settings. Keep the public contact email and explain that no personal data is collected, transmitted, sold, or shared.

- [ ] **Step 4: Complete accessibility and appearance checks**

Add meaningful VoiceOver labels and values to event colors, yearly status, settings, add, save, and delete actions. Avoid fixed-height text containers. Verify Extra Extra Large and Accessibility Extra Extra Extra Large Dynamic Type. Ensure text remains readable in both system appearances; if a view forces a light card background, explicitly use dark text on that card.

- [ ] **Step 5: Build, run full tests, and capture evidence**

Capture settings and privacy screenshots in both languages. Record manual checks for VoiceOver focus order, Dynamic Type, and dark appearance in progress.

- [ ] **Step 6: Commit**

```bash
git add ios/DaysLite docs/privacy-policy.md docs/privacy-policy.html docs/privacy-policy.zh.html
git commit -m "feat: add iOS settings and privacy"
```

Only include the root privacy-policy files in the commit if their actual content changed.

---

### Task 8: Add App Icon, Privacy Manifest, and Store Materials

**Files:**
- Create: `ios/DaysLite/scripts/generate-app-icon.swift`
- Create: `ios/DaysLite/DaysLite/Resources/Assets.xcassets/AppIcon.appiconset/Contents.json`
- Create: `ios/DaysLite/DaysLite/Resources/Assets.xcassets/AppIcon.appiconset/AppIcon-1024.png`
- Create: `ios/DaysLite/DaysLite/Resources/PrivacyInfo.xcprivacy`
- Create: `ios/DaysLite/StoreAssets/app-store-listing.md`
- Create: `ios/DaysLite/StoreAssets/review-notes.md`
- Create: `ios/DaysLite/StoreAssets/screenshots/README.md`
- Create: `ios/DaysLite/docs/publishing-playbook.md`
- Create: `ios/DaysLite/docs/app-store-checklist.md`
- Modify: `ios/DaysLite/docs/progress.md`

**Interfaces:**
- Produces: valid App Store icon, bundled privacy manifest, localized metadata drafts, review notes, and reusable publishing documentation.
- Consumes: confirmed product behavior and existing Android brand colors/icon concept.

- [ ] **Step 1: Generate a deterministic 1024×1024 icon**

Write a Swift CoreGraphics script that renders an opaque `#2563EB` square and a centered calendar/countdown mark matching the Android icon concept. The PNG must be RGB with no alpha channel. Run:

```bash
swift ios/DaysLite/scripts/generate-app-icon.swift ios/DaysLite/DaysLite/Resources/Assets.xcassets/AppIcon.appiconset/AppIcon-1024.png
sips -g pixelWidth -g pixelHeight -g hasAlpha ios/DaysLite/DaysLite/Resources/Assets.xcassets/AppIcon.appiconset/AppIcon-1024.png
```

Expected: width `1024`, height `1024`, and `hasAlpha: no`.

- [ ] **Step 2: Add the AppIcon catalog declaration**

```json
{
  "images": [
    {
      "filename": "AppIcon-1024.png",
      "idiom": "universal",
      "platform": "ios",
      "size": "1024x1024"
    }
  ],
  "info": { "author": "xcode", "version": 1 }
}
```

- [ ] **Step 3: Add the privacy manifest**

Use this exact content because the app stores only app-scoped values in UserDefaults:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
  <key>NSPrivacyTracking</key>
  <false/>
  <key>NSPrivacyTrackingDomains</key>
  <array/>
  <key>NSPrivacyCollectedDataTypes</key>
  <array/>
  <key>NSPrivacyAccessedAPITypes</key>
  <array>
    <dict>
      <key>NSPrivacyAccessedAPIType</key>
      <string>NSPrivacyAccessedAPICategoryUserDefaults</string>
      <key>NSPrivacyAccessedAPITypeReasons</key>
      <array><string>CA92.1</string></array>
    </dict>
  </array>
</dict>
</plist>
```

- [ ] **Step 4: Write real store metadata**

Draft English and Simplified Chinese name, subtitle, description, keywords, promotional text, version notes, support URL, privacy URL, and category. State exactly: no account, no ads, no analytics, no data collection, local storage only. Do not use unverifiable superlatives.

`review-notes.md` must explain yearly recurrence, manual language switching, local-only data, no login, and how to exercise all functions. `screenshots/README.md` must list the required device size, language, scenario, source simulator, and final filename for every screenshot.

- [ ] **Step 5: Write the reusable publishing playbook and checklist**

Document the actual sequence and purpose of Apple Account, paid membership, Team ID, certificates, Bundle ID, automatic signing, app record, version/build numbers, Archive, Organizer validation, TestFlight, privacy answers, age rating, screenshots, review submission, release mode, and post-release checks. Mark which actions require the user to handle legal or account confirmations.

- [ ] **Step 6: Validate bundled resources and commit**

```bash
plutil -lint ios/DaysLite/DaysLite/Resources/PrivacyInfo.xcprivacy
xcodebuild -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'generic/platform=iOS Simulator' build
git add ios/DaysLite
git commit -m "docs: prepare DaysLite iOS store submission"
```

Expected: privacy manifest is valid and the application builds with its final icon resources.

---

### Task 9: Complete Automated, Simulator, and Unsigned Release Verification

**Files:**
- Modify: `ios/DaysLite/docs/progress.md`
- Modify: `ios/DaysLite/docs/app-store-checklist.md`
- Modify: `ios/DaysLite/docs/publishing-playbook.md`

**Interfaces:**
- Produces: verified test/build evidence and an unsigned Release archive proving the project is structurally distributable.
- Consumes: all application and test targets from Tasks 1–8.

- [ ] **Step 1: Run all unit tests**

```bash
xcodebuild test -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -destination 'platform=iOS Simulator,name=iPhone 16 Pro,OS=latest'
```

Expected: `** TEST SUCCEEDED **` with no failed tests.

- [ ] **Step 2: Run clean Debug and Release builds**

```bash
xcodebuild clean build -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -configuration Debug -destination 'generic/platform=iOS Simulator'
xcodebuild clean build -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -configuration Release -destination 'generic/platform=iOS Simulator'
```

Expected: both end with `** BUILD SUCCEEDED **`.

- [ ] **Step 3: Create an unsigned archive dry run**

```bash
xcodebuild archive -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -configuration Release -destination 'generic/platform=iOS' -archivePath /tmp/DaysLite-unsigned.xcarchive CODE_SIGNING_ALLOWED=NO
```

Expected: `** ARCHIVE SUCCEEDED **`. Inspect `Info.plist` in the archive and verify bundle ID, marketing version, build number, deployment target, icon, and privacy manifest.

- [ ] **Step 4: Perform the manual simulator matrix**

Verify empty, populated, today, past, yearly, leap-day, add, edit, delete, restart persistence, both languages, large text, dark appearance, and VoiceOver. Record device/runtime and evidence for each row in progress.

- [ ] **Step 5: Update documentation and commit verification evidence**

Do not commit DerivedData or `.xcarchive` bundles. Commit only text results and approved screenshots:

```bash
git add ios/DaysLite/docs ios/DaysLite/StoreAssets
git commit -m "test: verify DaysLite iOS release candidate"
```

---

### Task 10: Activate Signing, TestFlight, App Review, and Publication

**Files:**
- Modify: `ios/DaysLite/DaysLite.xcodeproj/project.pbxproj`
- Modify: `ios/DaysLite/docs/progress.md`
- Modify: `ios/DaysLite/docs/app-store-checklist.md`
- Modify: `ios/DaysLite/docs/publishing-playbook.md`
- Modify: `ios/DaysLite/StoreAssets/` screenshots and final metadata only as evidence requires

**Interfaces:**
- Produces: signed App Store archive, TestFlight build, complete App Store Connect record, submitted review, public release, and final reusable retrospective.
- Consumes: active personal Apple Developer Program membership, Team ID, a connected iPhone, App Store Connect access, and the verified release candidate.

- [ ] **Step 1: Confirm membership activation without recording secrets**

User verifies Developer Program status is Active, role is Account Holder, and Team ID is visible. Record only status, date, and the non-secret Team ID if needed for Xcode signing. Do not record account credentials.

- [ ] **Step 2: Configure automatic signing and verify on a physical device**

Select the personal team in Xcode Signing & Capabilities, allow Xcode to register `com.dayslite.countdown.ios`, and run on the connected iPhone. Verify install, launch, add/edit/delete, relaunch persistence, language, and privacy screen.

- [ ] **Step 3: Create the App Store Connect app record**

Create a new iOS app named DaysLite with default language English (U.S.), the confirmed bundle ID, SKU `dayslite-ios-001`, and full user access. If the bundle ID is unavailable, choose a unique reverse-domain identifier and update the Xcode project, checklist, progress record, and playbook before continuing.

- [ ] **Step 4: Create and validate the signed archive**

```bash
xcodebuild archive -project ios/DaysLite/DaysLite.xcodeproj -scheme DaysLite -configuration Release -destination 'generic/platform=iOS' -archivePath /tmp/DaysLite-1.0.0-1.xcarchive -allowProvisioningUpdates
```

Open Organizer, run Validate App, and resolve all errors before upload. Record the archive version, build number, Xcode version, SDK, and validation result.

- [ ] **Step 5: Upload and test with TestFlight**

Upload through Organizer, wait for processing, answer export-compliance questions accurately, add internal testing notes, install from TestFlight on the physical iPhone, and repeat the core flow. Record the processed build identifier and test result.

- [ ] **Step 6: Complete metadata and compliance answers**

Upload final localized screenshots and copy. Set App Privacy to “No, we do not collect data from this app,” provide the public privacy-policy URL and support URL, complete the current age-rating questionnaire honestly, choose the Productivity category, declare no ads or in-app purchases, and add the prepared review notes.

- [ ] **Step 7: Submit for review**

Select build `1.0.0 (1)`, complete the final checklist, let the user perform the legal confirmation and Submit for Review action, then record submission time and review status.

- [ ] **Step 8: Handle review feedback with evidence**

If rejected, copy the exact guideline number and reviewer message into progress, reproduce the issue, create a targeted fix and new build number, rerun verification, and respond factually. Do not make speculative changes unrelated to the review message.

- [ ] **Step 9: Publish and complete the retrospective**

After approval, release according to the selected manual or automatic release setting. Verify the public product page, installation, support URL, privacy URL, seller name, screenshots, and version. Mark all milestones complete and add a retrospective covering actual timing, blockers, fixes, and the reusable sequence for the next iOS project.

- [ ] **Step 10: Commit final documentation**

```bash
git add ios/DaysLite/docs ios/DaysLite/StoreAssets ios/DaysLite/DaysLite.xcodeproj/project.pbxproj
git commit -m "docs: complete DaysLite iOS publishing guide"
```
