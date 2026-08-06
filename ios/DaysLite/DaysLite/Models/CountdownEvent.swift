import Foundation

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

enum CountdownStatus: Equatable, Sendable {
    case today
    case future(Int)
    case past(Int)
}
