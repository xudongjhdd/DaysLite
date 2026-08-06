import XCTest
@testable import DaysLite

final class PrivacyPolicyDocumentTests: XCTestCase {
    func testMarkdownBecomesSeparateHeadingAndParagraphBlocks() {
        let markdown = """
        # Privacy Policy

        Effective date: June 5, 2026

        ## Information stored on your device

        Countdowns stay on this device.
        """

        let document = PrivacyPolicyDocument(markdown: markdown)

        XCTAssertEqual(
            document.blocks,
            [
                .heading(level: 1, text: "Privacy Policy"),
                .paragraph("Effective date: June 5, 2026"),
                .heading(level: 2, text: "Information stored on your device"),
                .paragraph("Countdowns stay on this device.")
            ]
        )
    }
}
