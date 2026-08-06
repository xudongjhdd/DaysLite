import XCTest
@testable import DaysLite

final class CountdownCalculatorTests: XCTestCase {
    private let calculator = CountdownCalculator(
        calendar: Calendar(identifier: .gregorian)
    )
    private let today = LocalDate(year: 2026, month: 8, day: 6)

    func testFutureTodayAndPastStatuses() {
        XCTAssertEqual(
            calculator.status(
                for: event(date: LocalDate(year: 2026, month: 8, day: 7)),
                today: today
            ),
            .future(1)
        )
        XCTAssertEqual(
            calculator.status(for: event(date: today), today: today),
            .today
        )
        XCTAssertEqual(
            calculator.status(
                for: event(date: LocalDate(year: 2026, month: 8, day: 5)),
                today: today
            ),
            .past(1)
        )
    }

    func testYearlyEventMovesToNextOccurrence() {
        let yearly = event(
            date: LocalDate(year: 2020, month: 8, day: 5),
            repeatYearly: true
        )

        XCTAssertEqual(
            calculator.displayDate(for: yearly, today: today),
            LocalDate(year: 2027, month: 8, day: 5)
        )
    }

    func testYearlyEventUsesTodayWhenOccurrenceIsToday() {
        let yearly = event(
            date: LocalDate(year: 2020, month: 8, day: 6),
            repeatYearly: true
        )

        XCTAssertEqual(calculator.displayDate(for: yearly, today: today), today)
    }

    func testYearlyLeapDayUsesFebruary28InNonLeapYear() {
        let yearly = event(
            date: LocalDate(year: 2024, month: 2, day: 29),
            repeatYearly: true
        )

        XCTAssertEqual(
            calculator.displayDate(
                for: yearly,
                today: LocalDate(year: 2025, month: 1, day: 1)
            ),
            LocalDate(year: 2025, month: 2, day: 28)
        )
    }

    func testPastIgnoresYearlyEvents() {
        let yearly = event(
            date: LocalDate(year: 2020, month: 1, day: 1),
            repeatYearly: true
        )

        XCTAssertFalse(calculator.isPast(yearly, today: today))
    }

    private func event(
        date: LocalDate,
        repeatYearly: Bool = false
    ) -> CountdownEvent {
        CountdownEvent(
            id: UUID(),
            title: "Event",
            targetDate: date,
            note: "",
            colorHex: "#2563EB",
            repeatYearly: repeatYearly,
            createdAt: .distantPast,
            updatedAt: .distantPast
        )
    }
}
