import XCTest
@testable import DaysLite

final class CountdownStoreTests: XCTestCase {
    func testLoadEventsSkipsOnlyCorruptEntries() throws {
        let defaults = try makeDefaults()
        defaults.set(
            Data(
                ##"[{"id":"00000000-0000-0000-0000-000000000001","title":"One","targetDate":{"year":2026,"month":1,"day":1},"note":"","colorHex":"#2563EB","repeatYearly":false,"createdAt":0,"updatedAt":0},{"title":"Broken"}]"##.utf8
            ),
            forKey: "events"
        )

        let events = CountdownStore(defaults: defaults).loadEvents()

        XCTAssertEqual(events.map(\.title), ["One"])
    }

    func testLoadEventUsesDefaultsForFieldsAddedAfterInitialSchema() throws {
        let defaults = try makeDefaults()
        defaults.set(
            Data(
                #"[{"id":"00000000-0000-0000-0000-000000000001","title":"Legacy","targetDate":{"year":2026,"month":1,"day":1}}]"#.utf8
            ),
            forKey: "events"
        )

        let event = try XCTUnwrap(CountdownStore(defaults: defaults).loadEvents().first)

        XCTAssertEqual(event.note, "")
        XCTAssertEqual(event.colorHex, "#2563EB")
        XCTAssertFalse(event.repeatYearly)
    }

    func testSaveAndLoadRoundTripsEvents() throws {
        let defaults = try makeDefaults()
        let store = CountdownStore(defaults: defaults)
        let event = CountdownEvent(
            id: UUID(uuidString: "00000000-0000-0000-0000-000000000001")!,
            title: "Trip",
            targetDate: LocalDate(year: 2026, month: 9, day: 1),
            note: "Pack light",
            colorHex: "#10B981",
            repeatYearly: false,
            createdAt: Date(timeIntervalSinceReferenceDate: 1),
            updatedAt: Date(timeIntervalSinceReferenceDate: 2)
        )

        if case .failure(let error) = store.saveEvents([event]) {
            XCTFail("Expected save to succeed, got \(error)")
        }
        XCTAssertEqual(store.loadEvents(), [event])
    }

    func testSaveDetectsRejectedWrite() {
        let store = CountdownStore(storage: RejectingStorage())

        switch store.saveEvents([]) {
        case .success:
            XCTFail("Expected rejected storage to fail")
        case .failure(let error):
            XCTAssertEqual(error, .writeVerificationFailed)
        }
    }

    func testLanguageDefaultsToEnglishAndPersistsChinese() throws {
        let defaults = try makeDefaults()
        let store = CountdownStore(defaults: defaults)

        XCTAssertEqual(store.loadLanguage(), .english)
        store.saveLanguage(.chinese)
        XCTAssertEqual(store.loadLanguage(), .chinese)
    }

    private func makeDefaults() throws -> UserDefaults {
        let suiteName = "DaysLiteTests.\(name)"
        let defaults = try XCTUnwrap(UserDefaults(suiteName: suiteName))
        defaults.removePersistentDomain(forName: suiteName)
        return defaults
    }
}

private final class RejectingStorage: KeyValueStoring {
    func data(forKey defaultName: String) -> Data? { nil }
    func string(forKey defaultName: String) -> String? { nil }
    func set(_ value: Any?, forKey defaultName: String) {}
}
