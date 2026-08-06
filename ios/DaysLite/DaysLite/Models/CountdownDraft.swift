import Foundation

struct CountdownDraft: Equatable, Sendable {
    static let approvedColors = [
        "#2563EB",
        "#10B981",
        "#F97316",
        "#EC4899",
        "#7C3AED",
        "#475569"
    ]

    var title: String
    var targetDate: LocalDate
    var note: String
    var colorHex: String
    var repeatYearly: Bool

    var isValid: Bool {
        !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }

    static func new(today: LocalDate, calendar: Calendar) -> CountdownDraft {
        CountdownDraft(
            title: "",
            targetDate: today.adding(days: 1, calendar: calendar) ?? today,
            note: "",
            colorHex: approvedColors[0],
            repeatYearly: false
        )
    }

    init(event: CountdownEvent) {
        title = event.title
        targetDate = event.targetDate
        note = event.note
        colorHex = event.colorHex
        repeatYearly = event.repeatYearly
    }

    private init(
        title: String,
        targetDate: LocalDate,
        note: String,
        colorHex: String,
        repeatYearly: Bool
    ) {
        self.title = title
        self.targetDate = targetDate
        self.note = note
        self.colorHex = colorHex
        self.repeatYearly = repeatYearly
    }
}
