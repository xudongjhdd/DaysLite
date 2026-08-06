enum AppLanguage: String, Codable, CaseIterable, Sendable {
    case english = "en"
    case chinese = "zh-Hans"

    mutating func toggle() {
        self = self == .english ? .chinese : .english
    }
}
