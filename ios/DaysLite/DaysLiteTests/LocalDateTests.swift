import XCTest
@testable import DaysLite

final class LocalDateTests: XCTestCase {
    func testComparableUsesYearMonthAndDay() {
        XCTAssertLessThan(
            LocalDate(year: 2026, month: 1, day: 31),
            LocalDate(year: 2026, month: 2, day: 1)
        )
    }

    func testNonLeapFebruary29FallsBackToFebruary28() {
        let leapDay = LocalDate(year: 2024, month: 2, day: 29)

        XCTAssertEqual(
            leapDay.withYear(2025),
            LocalDate(year: 2025, month: 2, day: 28)
        )
    }

    func testAddingOneDayCrossesMonthBoundary() {
        let date = LocalDate(year: 2026, month: 8, day: 31)

        XCTAssertEqual(
            date.adding(days: 1, calendar: Calendar(identifier: .gregorian)),
            LocalDate(year: 2026, month: 9, day: 1)
        )
    }
}
