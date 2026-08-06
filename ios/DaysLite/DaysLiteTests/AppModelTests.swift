import XCTest
@testable import DaysLite

@MainActor
final class AppModelTests: XCTestCase {
    func testAddUpdateAndDeletePersistImmediately() throws {
        let defaults = try makeDefaults()
        let store = CountdownStore(defaults: defaults)
        let model = AppModel(
            store: store,
            now: { Date(timeIntervalSinceReferenceDate: 10) }
        )

        let event = model.add(
            title: "Trip",
            targetDate: LocalDate(year: 2026, month: 9, day: 1),
            note: "",
            colorHex: "#2563EB",
            repeatYearly: false
        )
        XCTAssertEqual(store.loadEvents().map(\.title), ["Trip"])

        model.update(
            id: event.id,
            title: "Birthday",
            targetDate: LocalDate(year: 2026, month: 10, day: 1),
            note: "Cake",
            colorHex: "#EC4899",
            repeatYearly: true
        )
        XCTAssertEqual(store.loadEvents().first?.title, "Birthday")
        XCTAssertEqual(store.loadEvents().first?.updatedAt, Date(timeIntervalSinceReferenceDate: 10))

        model.delete(id: event.id)
        XCTAssertTrue(store.loadEvents().isEmpty)
    }

    func testToggleLanguagePersistsImmediately() throws {
        let defaults = try makeDefaults()
        let store = CountdownStore(defaults: defaults)
        let model = AppModel(store: store)

        model.toggleLanguage()

        XCTAssertEqual(model.language, .chinese)
        XCTAssertEqual(store.loadLanguage(), .chinese)
    }

    func testSortedEventsUsesNextOccurrenceForYearlyEvents() throws {
        let model = AppModel(store: CountdownStore(defaults: try makeDefaults()))
        model.add(
            title: "Next Year",
            targetDate: .init(year: 2020, month: 8, day: 5),
            note: "",
            colorHex: "#2563EB",
            repeatYearly: true
        )
        model.add(
            title: "Tomorrow",
            targetDate: .init(year: 2026, month: 8, day: 7),
            note: "",
            colorHex: "#10B981",
            repeatYearly: false
        )

        let titles = model.sortedEvents(today: .init(year: 2026, month: 8, day: 6)).map(\.title)

        XCTAssertEqual(titles, ["Tomorrow", "Next Year"])
    }

    func testUpcomingCountExcludesPastOneOffAndIncludesYearlyEvents() throws {
        let model = AppModel(store: CountdownStore(defaults: try makeDefaults()))
        model.add(
            title: "Past",
            targetDate: .init(year: 2026, month: 8, day: 5),
            note: "",
            colorHex: "#64748B",
            repeatYearly: false
        )
        model.add(
            title: "Tomorrow",
            targetDate: .init(year: 2026, month: 8, day: 7),
            note: "",
            colorHex: "#10B981",
            repeatYearly: false
        )
        model.add(
            title: "Yearly",
            targetDate: .init(year: 2020, month: 8, day: 5),
            note: "",
            colorHex: "#EC4899",
            repeatYearly: true
        )

        XCTAssertEqual(model.upcomingCount(today: .init(year: 2026, month: 8, day: 6)), 2)
    }

    private func makeDefaults() throws -> UserDefaults {
        let suiteName = "DaysLiteTests.\(name)"
        let defaults = try XCTUnwrap(UserDefaults(suiteName: suiteName))
        defaults.removePersistentDomain(forName: suiteName)
        return defaults
    }
}
