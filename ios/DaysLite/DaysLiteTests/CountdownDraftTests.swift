import XCTest
@testable import DaysLite

final class CountdownDraftTests: XCTestCase {
    func testNewDraftDefaultsToTomorrowAndBlue() {
        let draft = CountdownDraft.new(
            today: .init(year: 2026, month: 8, day: 6),
            calendar: Calendar(identifier: .gregorian)
        )

        XCTAssertEqual(draft.targetDate, .init(year: 2026, month: 8, day: 7))
        XCTAssertEqual(draft.colorHex, "#2563EB")
        XCTAssertFalse(draft.repeatYearly)
    }

    func testTrimmedEmptyTitleIsInvalid() {
        var draft = CountdownDraft.new(
            today: .init(year: 2026, month: 8, day: 6),
            calendar: Calendar(identifier: .gregorian)
        )
        draft.title = "   \n"

        XCTAssertFalse(draft.isValid)
    }

    func testExistingEventCopiesEveryEditableField() {
        let event = CountdownEvent(
            id: UUID(),
            title: "Birthday",
            targetDate: .init(year: 2027, month: 2, day: 3),
            note: "Cake",
            colorHex: "#EC4899",
            repeatYearly: true,
            createdAt: .distantPast,
            updatedAt: .distantPast
        )

        let draft = CountdownDraft(event: event)

        XCTAssertEqual(draft.title, event.title)
        XCTAssertEqual(draft.targetDate, event.targetDate)
        XCTAssertEqual(draft.note, event.note)
        XCTAssertEqual(draft.colorHex, event.colorHex)
        XCTAssertEqual(draft.repeatYearly, event.repeatYearly)
    }

    func testApprovedColorsMatchProductSpecification() {
        XCTAssertEqual(
            CountdownDraft.approvedColors,
            ["#2563EB", "#10B981", "#F97316", "#EC4899", "#7C3AED", "#475569"]
        )
    }
}
