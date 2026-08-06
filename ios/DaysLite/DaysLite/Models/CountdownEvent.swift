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

    private enum CodingKeys: String, CodingKey {
        case id
        case title
        case targetDate
        case note
        case colorHex
        case repeatYearly
        case createdAt
        case updatedAt
    }

    init(
        id: UUID,
        title: String,
        targetDate: LocalDate,
        note: String,
        colorHex: String,
        repeatYearly: Bool,
        createdAt: Date,
        updatedAt: Date
    ) {
        self.id = id
        self.title = title
        self.targetDate = targetDate
        self.note = note
        self.colorHex = colorHex
        self.repeatYearly = repeatYearly
        self.createdAt = createdAt
        self.updatedAt = updatedAt
    }

    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let fallbackDate = Date(timeIntervalSinceReferenceDate: 0)

        id = try container.decode(UUID.self, forKey: .id)
        title = try container.decode(String.self, forKey: .title)
        targetDate = try container.decode(LocalDate.self, forKey: .targetDate)
        note = try container.decodeIfPresent(String.self, forKey: .note) ?? ""
        colorHex = try container.decodeIfPresent(String.self, forKey: .colorHex) ?? "#2563EB"
        repeatYearly = try container.decodeIfPresent(Bool.self, forKey: .repeatYearly) ?? false
        createdAt = try container.decodeIfPresent(Date.self, forKey: .createdAt) ?? fallbackDate
        updatedAt = try container.decodeIfPresent(Date.self, forKey: .updatedAt) ?? createdAt
    }

    func encode(to encoder: any Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(id, forKey: .id)
        try container.encode(title, forKey: .title)
        try container.encode(targetDate, forKey: .targetDate)
        try container.encode(note, forKey: .note)
        try container.encode(colorHex, forKey: .colorHex)
        try container.encode(repeatYearly, forKey: .repeatYearly)
        try container.encode(createdAt, forKey: .createdAt)
        try container.encode(updatedAt, forKey: .updatedAt)
    }
}

enum CountdownStatus: Equatable, Sendable {
    case today
    case future(Int)
    case past(Int)
}
