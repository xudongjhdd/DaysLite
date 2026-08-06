import XCTest
@testable import DaysLite

final class ProjectSmokeTests: XCTestCase {
    func testProductNameIsDaysLite() {
        _ = RootView()
        XCTAssertEqual(
            Bundle.main.object(forInfoDictionaryKey: "CFBundleDisplayName") as? String,
            "DaysLite"
        )
    }
}
