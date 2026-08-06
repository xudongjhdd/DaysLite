import XCTest
@testable import DaysLite

final class AppTextTests: XCTestCase {
    func testEnglishDayGrammar() {
        let text = AppText(language: .english)

        XCTAssertEqual(text.daysLabel(for: .future(1)), "1 day left")
        XCTAssertEqual(text.daysLabel(for: .future(2)), "2 days left")
        XCTAssertEqual(text.daysLabel(for: .past(1)), "1 day ago")
        XCTAssertEqual(text.daysLabel(for: .past(2)), "2 days ago")
        XCTAssertEqual(text.daysLabel(for: .today), "Today")
    }

    func testEnglishSummaryGrammar() {
        let text = AppText(language: .english)

        XCTAssertEqual(text.summary(upcoming: 0), "0 upcoming days")
        XCTAssertEqual(text.summary(upcoming: 1), "1 upcoming day")
        XCTAssertEqual(text.summary(upcoming: 2), "2 upcoming days")
    }

    func testChineseLabelsDateAndSummary() {
        let text = AppText(language: .chinese)

        XCTAssertEqual(text.daysLabel(for: .future(3)), "还剩 3 天")
        XCTAssertEqual(text.daysLabel(for: .past(3)), "已过 3 天")
        XCTAssertEqual(text.dateLabel(.init(year: 2026, month: 8, day: 6)), "2026年8月6日")
        XCTAssertEqual(text.summary(upcoming: 2), "2 个即将到来的日子")
    }

    func testEssentialEditorSettingsAndErrorCopyExistsInBothLanguages() {
        for language in AppLanguage.allCases {
            let text = AppText(language: language)

            XCTAssertFalse(text.eventName.isEmpty)
            XCTAssertFalse(text.repeatYearly.isEmpty)
            XCTAssertFalse(text.deleteCountdownQuestion.isEmpty)
            XCTAssertFalse(text.privacyPolicy.isEmpty)
            XCTAssertFalse(text.localStorage.isEmpty)
            XCTAssertFalse(text.languageLabel.isEmpty)
            XCTAssertFalse(text.version.isEmpty)
            XCTAssertFalse(text.titleRequired.isEmpty)
            XCTAssertFalse(text.saveFailure.isEmpty)
        }
    }
}
